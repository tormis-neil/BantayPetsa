package com.bantaypetsa.app.domain

import com.bantaypetsa.app.domain.engine.FefoEngine
import com.bantaypetsa.app.domain.model.BatchItem
import com.bantaypetsa.app.domain.model.ExpiryStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class FefoEngineTest {

    private val baseDate = LocalDate.of(2026, 10, 20)

    @Test
    fun testBatchesSortedChronologicallyByExpiryDate() {
        val b1 = BatchItem(batchId = "b1", productId = "p1", quantity = 10.0, expirationDate = baseDate.plusDays(10))
        val b2 = BatchItem(batchId = "b2", productId = "p1", quantity = 5.0, expirationDate = baseDate.plusDays(2))
        val b3 = BatchItem(batchId = "b3", productId = "p1", quantity = 8.0, expirationDate = baseDate.minusDays(1))

        val sorted = FefoEngine.sortBatches(listOf(b1, b2, b3))

        // b3 (expired) -> b2 (urgent) -> b1 (approaching)
        assertEquals("b3", sorted[0].batchId)
        assertEquals("b2", sorted[1].batchId)
        assertEquals("b1", sorted[2].batchId)
    }

    @Test
    fun testFefoTieBreakerOlderIntakeDateFirst() {
        // Both expire on the same day, but b_older was received 5 days ago, b_newer was received today
        val bNewer = BatchItem(
            batchId = "b_newer",
            productId = "p1",
            quantity = 5.0,
            expirationDate = baseDate.plusDays(5),
            dateReceived = baseDate
        )
        val bOlder = BatchItem(
            batchId = "b_older",
            productId = "p1",
            quantity = 5.0,
            expirationDate = baseDate.plusDays(5),
            dateReceived = baseDate.minusDays(5)
        )

        val sorted = FefoEngine.sortBatches(listOf(bNewer, bOlder))

        assertEquals("b_older", sorted[0].batchId)
        assertEquals("b_newer", sorted[1].batchId)
    }

    @Test
    fun testPrioritizeAttentionBatchesExcludesSafeBatches() {
        val expiredBatch = BatchItem(batchId = "b_exp", productId = "p1", quantity = 1.0, expirationDate = baseDate.minusDays(2))
        val urgentBatch = BatchItem(batchId = "b_urg", productId = "p1", quantity = 2.0, expirationDate = baseDate.plusDays(3))
        val approachingBatch = BatchItem(batchId = "b_app", productId = "p1", quantity = 3.0, expirationDate = baseDate.plusDays(14))
        val safeBatch = BatchItem(batchId = "b_safe", productId = "p1", quantity = 10.0, expirationDate = baseDate.plusDays(60))

        val attention = FefoEngine.prioritizeAttentionBatches(
            batches = listOf(safeBatch, approachingBatch, expiredBatch, urgentBatch),
            referenceDate = baseDate,
            limit = 5
        )

        assertEquals(3, attention.size)
        assertEquals("b_exp", attention[0].batchId)
        assertEquals("b_urg", attention[1].batchId)
        assertEquals("b_app", attention[2].batchId)
        assertTrue(attention.none { it.batchId == "b_safe" })
    }

    @Test
    fun testPrioritizeAttentionBatchesRespectsLimit() {
        val batches = (1..10).map { i ->
            BatchItem(
                batchId = "b_$i",
                productId = "p1",
                quantity = 1.0,
                expirationDate = baseDate.plusDays(i.toLong()) // days 1 to 10 (urgent & approaching)
            )
        }

        val attention = FefoEngine.prioritizeAttentionBatches(
            batches = batches,
            referenceDate = baseDate,
            limit = 3
        )

        assertEquals(3, attention.size)
        assertEquals("b_1", attention[0].batchId)
        assertEquals("b_2", attention[1].batchId)
        assertEquals("b_3", attention[2].batchId)
    }

    @Test
    fun testCategorizeBatchesIntoAllFourTiers() {
        val bExp = BatchItem(batchId = "b1", productId = "p1", quantity = 1.0, expirationDate = baseDate.minusDays(1))
        val bUrg = BatchItem(batchId = "b2", productId = "p1", quantity = 1.0, expirationDate = baseDate.plusDays(5))
        val bApp = BatchItem(batchId = "b3", productId = "p1", quantity = 1.0, expirationDate = baseDate.plusDays(20))
        val bSafe = BatchItem(batchId = "b4", productId = "p1", quantity = 1.0, expirationDate = baseDate.plusDays(40))

        val map = FefoEngine.categorizeBatches(listOf(bExp, bUrg, bApp, bSafe), referenceDate = baseDate)

        assertEquals(1, map[ExpiryStatus.EXPIRED]?.size)
        assertEquals(1, map[ExpiryStatus.URGENT]?.size)
        assertEquals(1, map[ExpiryStatus.APPROACHING]?.size)
        assertEquals(1, map[ExpiryStatus.SAFE]?.size)
    }
}
