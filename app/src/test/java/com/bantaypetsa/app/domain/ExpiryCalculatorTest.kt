package com.bantaypetsa.app.domain

import com.bantaypetsa.app.domain.engine.ExpiryCalculator
import com.bantaypetsa.app.domain.engine.ExpiryThresholds
import com.bantaypetsa.app.domain.model.ExpiryStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDate

class ExpiryCalculatorTest {

    private val referenceDate = LocalDate.of(2026, 10, 20)

    @Test
    fun testExpiredYesterday() {
        val expiry = referenceDate.minusDays(1)
        val status = ExpiryCalculator.calculateStatus(expiry, referenceDate)
        assertEquals(ExpiryStatus.EXPIRED, status)
        assertEquals(-1L, ExpiryCalculator.daysRemaining(expiry, referenceDate))
    }

    @Test
    fun testExpiredThirtyDaysAgo() {
        val expiry = referenceDate.minusDays(30)
        val status = ExpiryCalculator.calculateStatus(expiry, referenceDate)
        assertEquals(ExpiryStatus.EXPIRED, status)
        assertEquals(-30L, ExpiryCalculator.daysRemaining(expiry, referenceDate))
    }

    @Test
    fun testExpiringTodayIsUrgent() {
        val expiry = referenceDate
        val status = ExpiryCalculator.calculateStatus(expiry, referenceDate)
        assertEquals(ExpiryStatus.URGENT, status)
        assertEquals(0L, ExpiryCalculator.daysRemaining(expiry, referenceDate))
    }

    @Test
    fun testExpiringTomorrowIsUrgent() {
        val expiry = referenceDate.plusDays(1)
        val status = ExpiryCalculator.calculateStatus(expiry, referenceDate)
        assertEquals(ExpiryStatus.URGENT, status)
        assertEquals(1L, ExpiryCalculator.daysRemaining(expiry, referenceDate))
    }

    @Test
    fun testUrgentBoundaryUpperSevenDays() {
        val expiry = referenceDate.plusDays(7)
        val status = ExpiryCalculator.calculateStatus(expiry, referenceDate)
        assertEquals(ExpiryStatus.URGENT, status)
        assertEquals(7L, ExpiryCalculator.daysRemaining(expiry, referenceDate))
    }

    @Test
    fun testApproachingBoundaryLowerEightDays() {
        val expiry = referenceDate.plusDays(8)
        val status = ExpiryCalculator.calculateStatus(expiry, referenceDate)
        assertEquals(ExpiryStatus.APPROACHING, status)
        assertEquals(8L, ExpiryCalculator.daysRemaining(expiry, referenceDate))
    }

    @Test
    fun testApproachingMiddleFifteenDays() {
        val expiry = referenceDate.plusDays(15)
        val status = ExpiryCalculator.calculateStatus(expiry, referenceDate)
        assertEquals(ExpiryStatus.APPROACHING, status)
    }

    @Test
    fun testApproachingBoundaryUpperThirtyDays() {
        val expiry = referenceDate.plusDays(30)
        val status = ExpiryCalculator.calculateStatus(expiry, referenceDate)
        assertEquals(ExpiryStatus.APPROACHING, status)
        assertEquals(30L, ExpiryCalculator.daysRemaining(expiry, referenceDate))
    }

    @Test
    fun testSafeBoundaryThirtyOneDays() {
        val expiry = referenceDate.plusDays(31)
        val status = ExpiryCalculator.calculateStatus(expiry, referenceDate)
        assertEquals(ExpiryStatus.SAFE, status)
        assertEquals(31L, ExpiryCalculator.daysRemaining(expiry, referenceDate))
    }

    @Test
    fun testSafeOneHundredEightyDays() {
        val expiry = referenceDate.plusDays(180)
        val status = ExpiryCalculator.calculateStatus(expiry, referenceDate)
        assertEquals(ExpiryStatus.SAFE, status)
    }

    @Test
    fun testCustomThresholds() {
        // Custom: Urgent <= 3 days, Approaching <= 14 days
        val custom = ExpiryThresholds(urgentDays = 3L, approachingDays = 14L)

        // Day 4 should be APPROACHING under custom, but would have been URGENT under default (7)
        val day4 = referenceDate.plusDays(4)
        assertEquals(ExpiryStatus.APPROACHING, ExpiryCalculator.calculateStatus(day4, referenceDate, custom))

        // Day 15 should be SAFE under custom, but would have been APPROACHING under default (30)
        val day15 = referenceDate.plusDays(15)
        assertEquals(ExpiryStatus.SAFE, ExpiryCalculator.calculateStatus(day15, referenceDate, custom))
    }

    @Test
    fun testThresholdValidationConstraints() {
        // Approaching cannot be less than urgent
        assertThrows(IllegalArgumentException::class.java) {
            ExpiryThresholds(urgentDays = 10L, approachingDays = 5L)
        }
    }

    @Test
    fun testDurationDescriptionFormatting() {
        assertEquals("Yesterday", ExpiryCalculator.getDurationDescription(referenceDate.minusDays(1), referenceDate))
        assertEquals("Expires Today", ExpiryCalculator.getDurationDescription(referenceDate, referenceDate))
        assertEquals("1 day left", ExpiryCalculator.getDurationDescription(referenceDate.plusDays(1), referenceDate))
        assertEquals("5 days left", ExpiryCalculator.getDurationDescription(referenceDate.plusDays(5), referenceDate))
        assertEquals("10 days ago", ExpiryCalculator.getDurationDescription(referenceDate.minusDays(10), referenceDate))
    }
}
