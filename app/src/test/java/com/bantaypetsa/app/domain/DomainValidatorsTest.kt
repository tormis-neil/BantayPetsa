package com.bantaypetsa.app.domain

import com.bantaypetsa.app.domain.engine.DomainValidators
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class DomainValidatorsTest {

    private val baseDate = LocalDate.of(2026, 10, 20)

    @Test
    fun testValidProductPasses() {
        val result = DomainValidators.validateProduct(
            name = "Bear Brand Fortified",
            category = "Dairy & Powdered Milk",
            unit = "sachets"
        )
        assertTrue(result is DomainValidators.ValidationResult.Valid)
    }

    @Test
    fun testBlankProductNameFails() {
        val result = DomainValidators.validateProduct(
            name = "   ",
            category = "Canned Goods",
            unit = "cans"
        )
        assertTrue(result is DomainValidators.ValidationResult.Invalid)
    }

    @Test
    fun testBlankCategoryFails() {
        val result = DomainValidators.validateProduct(
            name = "Lucky Me! Pancit Canton",
            category = "",
            unit = "packs"
        )
        assertTrue(result is DomainValidators.ValidationResult.Invalid)
    }

    @Test
    fun testBlankUnitFails() {
        val result = DomainValidators.validateProduct(
            name = "San Miguel Pale Pilsen",
            category = "Beverages",
            unit = "   "
        )
        assertTrue(result is DomainValidators.ValidationResult.Invalid)
    }

    @Test
    fun testValidBatchPasses() {
        val result = DomainValidators.validateBatch(
            quantity = 24.0,
            expirationDate = baseDate.plusMonths(6),
            referenceDate = baseDate
        )
        assertTrue(result is DomainValidators.ValidationResult.Valid)
    }

    @Test
    fun testZeroOrNegativeBatchQuantityFails() {
        val zeroResult = DomainValidators.validateBatch(
            quantity = 0.0,
            expirationDate = baseDate.plusMonths(6),
            referenceDate = baseDate
        )
        assertTrue(zeroResult is DomainValidators.ValidationResult.Invalid)

        val negResult = DomainValidators.validateBatch(
            quantity = -10.0,
            expirationDate = baseDate.plusMonths(6),
            referenceDate = baseDate
        )
        assertTrue(negResult is DomainValidators.ValidationResult.Invalid)
    }

    @Test
    fun testPastExpirationDateIntakeFails() {
        val result = DomainValidators.validateBatch(
            quantity = 10.0,
            expirationDate = baseDate.minusDays(5),
            referenceDate = baseDate
        )
        assertTrue(result is DomainValidators.ValidationResult.Invalid)
    }

    @Test
    fun testValidUserProfilePasses() {
        val result = DomainValidators.validateUserProfile(
            storeName = "Nena's Sari-Sari Store",
            ownerName = "Maria Santos",
            email = "nena@example.com",
            phone = "09171234567"
        )
        assertTrue(result is DomainValidators.ValidationResult.Valid)
    }

    @Test
    fun testInvalidEmailUserProfileFails() {
        val result = DomainValidators.validateUserProfile(
            storeName = "Nena's Store",
            ownerName = "Maria Santos",
            email = "not-an-email",
            phone = "09171234567"
        )
        assertTrue(result is DomainValidators.ValidationResult.Invalid)
    }

    @Test
    fun testPhoneWithLettersFails() {
        val result = DomainValidators.validatePhoneNumber("0917abc4567")
        assertTrue(result is DomainValidators.ValidationResult.Invalid)
    }

    @Test
    fun testPhoneMoreThan11DigitsFails() {
        val result = DomainValidators.validatePhoneNumber("0917123456789")
        assertTrue(result is DomainValidators.ValidationResult.Invalid)
    }

    @Test
    fun testPhoneLessThan11DigitsFails() {
        val result = DomainValidators.validatePhoneNumber("09171234")
        assertTrue(result is DomainValidators.ValidationResult.Invalid)
    }

    @Test
    fun testPhoneNotStartingWith09Fails() {
        val result = DomainValidators.validatePhoneNumber("08171234567")
        assertTrue(result is DomainValidators.ValidationResult.Invalid)
    }

    @Test
    fun testValid11DigitPhonePasses() {
        val result = DomainValidators.validatePhoneNumber("09171234567")
        assertTrue(result is DomainValidators.ValidationResult.Valid)
    }
}