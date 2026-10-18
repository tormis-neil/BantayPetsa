package com.bantaypetsa.app.domain.engine

import com.bantaypetsa.app.domain.model.BatchItem
import com.bantaypetsa.app.domain.model.ExpiryStatus
import java.time.LocalDate

/**
 * Pure domain engine implementing First Expired, First Out (FEFO) inventory ordering.
 * FEFO minimizes spoilage by surfacing and prioritizing batches nearest to expiration.
 */
object FefoEngine {

    /**
     * Sorts batches deterministically following FEFO rules:
     * 1. Primary: expirationDate ascending (earliest expiry first)
     * 2. Tie-breaker 1: dateReceived ascending (oldest stock intake first)
     * 3. Tie-breaker 2: createdAt ascending
     * 4. Tie-breaker 3: batchId ascending (absolute determinism)
     */
    fun sortBatches(batches: List<BatchItem>): List<BatchItem> {
        return batches.sortedWith(
            compareBy<BatchItem> { it.expirationDate }
                .thenBy { it.dateReceived }
                .thenBy { it.createdAt }
                .thenBy { it.batchId }
        )
    }

    /**
     * Filters and returns priority attention batches that need immediate merchant action:
     * EXPIRED, URGENT, or APPROACHING, capped at [limit].
     */
    fun prioritizeAttentionBatches(
        batches: List<BatchItem>,
        referenceDate: LocalDate = LocalDate.now(),
        thresholds: ExpiryThresholds = ExpiryCalculator.DEFAULT_THRESHOLDS,
        limit: Int = 5
    ): List<BatchItem> {
        return sortBatches(batches).filter { batch ->
            val status = ExpiryCalculator.calculateStatus(batch.expirationDate, referenceDate, thresholds)
            status == ExpiryStatus.EXPIRED || status == ExpiryStatus.URGENT || status == ExpiryStatus.APPROACHING
        }.take(limit)
    }

    /**
     * Groups batches by their ExpiryStatus for summary metrics.
     */
    fun categorizeBatches(
        batches: List<BatchItem>,
        referenceDate: LocalDate = LocalDate.now(),
        thresholds: ExpiryThresholds = ExpiryCalculator.DEFAULT_THRESHOLDS
    ): Map<ExpiryStatus, List<BatchItem>> {
        return batches.groupBy { batch ->
            ExpiryCalculator.calculateStatus(batch.expirationDate, referenceDate, thresholds)
        }
    }
}
