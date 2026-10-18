package com.bantaypetsa.app.domain.model

import java.time.LocalDate

enum class ExpiryStatus {
    EXPIRED,
    URGENT,
    APPROACHING,
    SAFE
}

enum class StockActionType {
    ADD,
    REMOVE,
    ADJUST
}

data class ProductItem(
    val productId: String,
    val name: String,
    val category: String,
    val unit: String,
    val description: String = "",
    val isArchived: Boolean = false,
    val createdAt: LocalDate = LocalDate.now()
)

data class BatchItem(
    val batchId: String,
    val productId: String,
    val lotNumber: String = "",
    val quantity: Double,
    val expirationDate: LocalDate,
    val dateReceived: LocalDate = LocalDate.now(),
    val createdAt: LocalDate = LocalDate.now()
)

data class StockActionRecord(
    val actionId: String,
    val batchId: String,
    val productName: String,
    val actionType: StockActionType,
    val quantityChange: Double,
    val quantityBefore: Double,
    val quantityAfter: Double,
    val reason: String = "",
    val timestamp: String
)

data class UserProfile(
    val storeName: String = "Aling Nena's Sari-Sari Store",
    val ownerName: String = "Maria Santos",
    val email: String = "nena.santos@gmail.com",
    val phone: String = "+63 917 123 4567",
    val businessType: String = "Sari-Sari Store / Micro-Retail",
    val location: String = "Barangay Poblacion, Batangas",
    val memberSince: String = "January 2026",
    val isArchived: Boolean = false
)
