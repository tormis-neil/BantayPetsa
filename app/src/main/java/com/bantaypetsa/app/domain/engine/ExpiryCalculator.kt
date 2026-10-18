package com.bantaypetsa.app.domain.engine

import com.bantaypetsa.app.domain.model.ExpiryStatus
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Configurable thresholds for the 4-tier expiry model.
 * Defaults follow standard retail best practices for Philippine micro-retail:
 * - Urgent: 0 to 7 days left (immediate price reduction or promo bundling)
 * - Approaching: 8 to 30 days left (front-row display placement)
 */
data class ExpiryThresholds(
    val urgentDays: Long = 7L,
    val approachingDays: Long = 30L
) {
    init {
        require(urgentDays >= 0) { "Urgent threshold must be non-negative" }
        require(approachingDays >= urgentDays) { "Approaching threshold must be >= urgent threshold" }
    }
}

/**
 * Pure domain logic engine for expiry status determination and day-count mathematics.
 * Strictly decoupled from Android frameworks and UI state.
 */
object ExpiryCalculator {

    val DEFAULT_THRESHOLDS = ExpiryThresholds()

    /**
     * Calculates the exact number of days remaining until expiration relative to a reference date.
     * Returns negative values if the batch has already expired.
     */
    fun daysRemaining(
        expirationDate: LocalDate,
        referenceDate: LocalDate = LocalDate.now()
    ): Long {
        return ChronoUnit.DAYS.between(referenceDate, expirationDate)
    }

    /**
     * Determines the 4-tier ExpiryStatus based on days remaining and thresholds.
     * - EXPIRED: days < 0
     * - URGENT: 0 <= days <= urgentDays (e.g. 0 to 7 days)
     * - APPROACHING: urgentDays < days <= approachingDays (e.g. 8 to 30 days)
     * - SAFE: days > approachingDays (e.g. > 30 days)
     */
    fun calculateStatus(
        expirationDate: LocalDate,
        referenceDate: LocalDate = LocalDate.now(),
        thresholds: ExpiryThresholds = DEFAULT_THRESHOLDS
    ): ExpiryStatus {
        val days = daysRemaining(expirationDate, referenceDate)
        return when {
            days < 0 -> ExpiryStatus.EXPIRED
            days <= thresholds.urgentDays -> ExpiryStatus.URGENT
            days <= thresholds.approachingDays -> ExpiryStatus.APPROACHING
            else -> ExpiryStatus.SAFE
        }
    }

    /**
     * Human-readable label helper describing remaining or overdue duration.
     */
    fun getDurationDescription(
        expirationDate: LocalDate,
        referenceDate: LocalDate = LocalDate.now()
    ): String {
        val days = daysRemaining(expirationDate, referenceDate)
        return when {
            days < -1 -> "${-days} days ago"
            days == -1L -> "Yesterday"
            days == 0L -> "Expires Today"
            days == 1L -> "1 day left"
            else -> "$days days left"
        }
    }
}
