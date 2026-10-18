package com.bantaypetsa.app.domain.engine

import java.time.LocalDate

/**
 * Pure domain validation rules ensuring catalog, batch, and merchant integrity.
 */
object DomainValidators {

    sealed class ValidationResult {
        object Valid : ValidationResult()
        data class Invalid(val errors: List<String>) : ValidationResult() {
            val firstErrorMessage: String get() = errors.firstOrNull() ?: "Validation error"
        }
    }

    fun validateProduct(
        name: String,
        category: String,
        unit: String
    ): ValidationResult {
        val errors = mutableListOf<String>()

        if (name.isBlank()) {
            errors.add("Product name is required and cannot be blank.")
        } else if (name.trim().length < 2) {
            errors.add("Product name must be at least 2 characters.")
        }

        if (category.isBlank()) {
            errors.add("Product category is required.")
        }

        if (unit.isBlank()) {
            errors.add("Measurement unit is required (e.g., pcs, cans, sachets).")
        }

        return if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
    }

    fun validateBatch(
        quantity: Double,
        expirationDate: LocalDate,
        referenceDate: LocalDate = LocalDate.now()
    ): ValidationResult {
        val errors = mutableListOf<String>()

        if (quantity <= 0) {
            errors.add("Batch stock quantity must be strictly greater than zero.")
        }

        // Warning or error if intake is already past expiry date
        if (expirationDate.isBefore(referenceDate)) {
            errors.add("Cannot intake a batch that is already expired (expiry date is in the past).")
        }

        return if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
    }

    fun validateUserProfile(
        storeName: String,
        ownerName: String,
        email: String,
        phone: String
    ): ValidationResult {
        val errors = mutableListOf<String>()

        if (storeName.isBlank()) errors.add("Store name cannot be empty.")
        if (ownerName.isBlank()) errors.add("Owner name cannot be empty.")

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (email.isNotBlank() && !email.matches(emailRegex)) {
            errors.add("Invalid email address format.")
        }

        val phoneClean = phone.replace(Regex("[\\s-()]"), "")
        if (phone.isNotBlank() && phoneClean.length < 7) {
            errors.add("Contact phone number is too short.")
        }

        return if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
    }
}
