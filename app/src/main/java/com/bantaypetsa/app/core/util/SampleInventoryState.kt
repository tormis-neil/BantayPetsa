package com.bantaypetsa.app.core.util

import com.bantaypetsa.app.domain.model.BatchItem
import com.bantaypetsa.app.domain.model.ExpiryStatus
import com.bantaypetsa.app.domain.model.ProductItem
import com.bantaypetsa.app.domain.model.UserProfile
import com.bantaypetsa.app.domain.model.StockActionRecord
import com.bantaypetsa.app.domain.model.StockActionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID

object SampleInventoryState {
    var urgentThresholdDays: Int = 7
    var approachingThresholdDays: Int = 30

    private val _products = MutableStateFlow<List<ProductItem>>(
        listOf(
            ProductItem(
                productId = "prod-1",
                name = "Bear Brand Fortified Powdered Milk",
                category = "Dairy & Milk",
                unit = "packs (33g)",
                description = "Single-serve fortified powdered milk sachets"
            ),
            ProductItem(
                productId = "prod-2",
                name = "Lucky Me! Instant Pancit Canton Kalamansi",
                category = "Noodles",
                unit = "pouches (60g)",
                description = "Citrus flavored instant noodles"
            ),
            ProductItem(
                productId = "prod-3",
                name = "Century Tuna Flakes in Oil",
                category = "Canned Goods",
                unit = "cans (155g)",
                description = "Flaked light tuna in vegetable oil"
            ),
            ProductItem(
                productId = "prod-4",
                name = "Eden Cheese Original",
                category = "Spreads & Cheese",
                unit = "blocks (160g)",
                description = "Processed cheddar cheese block"
            )
        )
    )
    val products: StateFlow<List<ProductItem>> = _products.asStateFlow()

    private val today = LocalDate.now()

    private val _batches = MutableStateFlow<List<BatchItem>>(
        listOf(
            BatchItem(
                batchId = "batch-1",
                productId = "prod-1",
                lotNumber = "LOT-2026-BB1",
                quantity = 15.0,
                expirationDate = today.plusDays(3), // URGENT (3 days)
                dateReceived = today.minusDays(30)
            ),
            BatchItem(
                batchId = "batch-2",
                productId = "prod-1",
                lotNumber = "LOT-2026-BB2",
                quantity = 40.0,
                expirationDate = today.plusDays(20), // APPROACHING (20 days)
                dateReceived = today.minusDays(10)
            ),
            BatchItem(
                batchId = "batch-3",
                productId = "prod-2",
                lotNumber = "LOT-2026-LM1",
                quantity = 24.0,
                expirationDate = today.plusDays(0), // URGENT (Expires Today)
                dateReceived = today.minusDays(45)
            ),
            BatchItem(
                batchId = "batch-4",
                productId = "prod-3",
                lotNumber = "LOT-2026-CT1",
                quantity = 12.0,
                expirationDate = today.minusDays(2), // EXPIRED
                dateReceived = today.minusDays(90)
            ),
            BatchItem(
                batchId = "batch-5",
                productId = "prod-4",
                lotNumber = "LOT-2026-ED1",
                quantity = 8.0,
                expirationDate = today.plusDays(60), // SAFE (60 days)
                dateReceived = today.minusDays(5)
            ),
            BatchItem(
                batchId = "batch-6",
                productId = "prod-2",
                lotNumber = "LOT-2026-LM2",
                quantity = 30.0,
                expirationDate = today.plusDays(15), // APPROACHING (15 days)
                dateReceived = today.minusDays(1)
            )
        )
    )
    val batches: StateFlow<List<BatchItem>> = _batches.asStateFlow()

    private val _history = MutableStateFlow<List<StockActionRecord>>(
        listOf(
            StockActionRecord(
                actionId = "act-1",
                batchId = "batch-1",
                productName = "Bear Brand Fortified Powdered Milk",
                actionType = StockActionType.ADD,
                quantityChange = 25.0,
                quantityBefore = 0.0,
                quantityAfter = 25.0,
                reason = "Delivery restock from distributor",
                timestamp = today.minusDays(35).format(DateTimeFormatter.ISO_DATE)
            ),
            StockActionRecord(
                actionId = "act-2",
                batchId = "batch-1",
                productName = "Bear Brand Fortified Powdered Milk",
                actionType = StockActionType.REMOVE,
                quantityChange = -10.0,
                quantityBefore = 25.0,
                quantityAfter = 15.0,
                reason = "Prioritized sales (FEFO promotion)",
                timestamp = today.minusDays(5).format(DateTimeFormatter.ISO_DATE)
            ),
            StockActionRecord(
                actionId = "act-3",
                batchId = "batch-4",
                productName = "Century Tuna Flakes in Oil",
                actionType = StockActionType.REMOVE,
                quantityChange = -2.0,
                quantityBefore = 14.0,
                quantityAfter = 12.0,
                reason = "Damaged packaging / return to supplier",
                timestamp = today.minusDays(2).format(DateTimeFormatter.ISO_DATE)
            ),
            StockActionRecord(
                actionId = "act-4",
                batchId = "batch-3",
                productName = "Lucky Me! Instant Pancit Canton Kalamansi",
                actionType = StockActionType.ADD,
                quantityChange = 24.0,
                quantityBefore = 0.0,
                quantityAfter = 24.0,
                reason = "New delivery intake",
                timestamp = today.minusDays(15).format(DateTimeFormatter.ISO_DATE)
            ),
            StockActionRecord(
                actionId = "act-5",
                batchId = "batch-6",
                productName = "Lucky Me! Instant Pancit Canton Kalamansi",
                actionType = StockActionType.ADD,
                quantityChange = 30.0,
                quantityBefore = 0.0,
                quantityAfter = 30.0,
                reason = "Fresh stock delivery",
                timestamp = today.minusDays(1).format(DateTimeFormatter.ISO_DATE)
            )
        )
    )
    val history: StateFlow<List<StockActionRecord>> = _history.asStateFlow()

    // Expiry calculation helper (4-Tier Model)
    fun calculateStatus(expirationDate: LocalDate): ExpiryStatus {
        val daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), expirationDate)
        return when {
            daysUntil < 0 -> ExpiryStatus.EXPIRED
            daysUntil <= urgentThresholdDays -> ExpiryStatus.URGENT
            daysUntil <= approachingThresholdDays -> ExpiryStatus.APPROACHING
            else -> ExpiryStatus.SAFE
        }
    }

    // Product mutations
    fun addProduct(name: String, category: String, unit: String, description: String): String {
        val id = UUID.randomUUID().toString()
        val item = ProductItem(
            productId = id,
            name = name,
            category = category,
            unit = unit,
            description = description
        )
        _products.value = _products.value + item
        return id
    }

    fun updateProduct(productId: String, name: String, category: String, unit: String, description: String) {
        _products.value = _products.value.map {
            if (it.productId == productId) {
                it.copy(name = name, category = category, unit = unit, description = description)
            } else it
        }
    }

    fun toggleArchiveProduct(productId: String) {
        _products.value = _products.value.map {
            if (it.productId == productId) it.copy(isArchived = !it.isArchived) else it
        }
    }

    // Batch mutations
    fun addBatch(productId: String, lotNumber: String, quantity: Double, expirationDate: LocalDate, dateReceived: LocalDate) {
        val batchId = UUID.randomUUID().toString()
        val newBatch = BatchItem(
            batchId = batchId,
            productId = productId,
            lotNumber = lotNumber,
            quantity = quantity,
            expirationDate = expirationDate,
            dateReceived = dateReceived
        )
        _batches.value = _batches.value + newBatch

        val product = _products.value.find { it.productId == productId }
        val record = StockActionRecord(
            actionId = UUID.randomUUID().toString(),
            batchId = batchId,
            productName = product?.name ?: "Unknown Product",
            actionType = StockActionType.ADD,
            quantityChange = quantity,
            quantityBefore = 0.0,
            quantityAfter = quantity,
            reason = "New batch arrival",
            timestamp = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        )
        _history.value = listOf(record) + _history.value
    }

    fun updateBatch(batchId: String, lotNumber: String, quantity: Double, expirationDate: LocalDate, dateReceived: LocalDate) {
        _batches.value = _batches.value.map {
            if (it.batchId == batchId) {
                it.copy(
                    lotNumber = lotNumber,
                    quantity = quantity,
                    expirationDate = expirationDate,
                    dateReceived = dateReceived
                )
            } else it
        }
    }

    // Stock Action mutation
    fun recordStockAction(
        batchId: String,
        actionType: StockActionType,
        quantityChange: Double,
        reason: String
    ): Boolean {
        val currentBatch = _batches.value.find { it.batchId == batchId } ?: return false
        val before = currentBatch.quantity
        val after = when (actionType) {
            StockActionType.ADD -> before + quantityChange
            StockActionType.REMOVE -> before - quantityChange
            StockActionType.ADJUST -> quantityChange
        }

        if (after < 0) return false // Negative prevention

        val actualChange = after - before

        _batches.value = _batches.value.map {
            if (it.batchId == batchId) it.copy(quantity = after) else it
        }

        val product = _products.value.find { it.productId == currentBatch.productId }
        val record = StockActionRecord(
            actionId = UUID.randomUUID().toString(),
            batchId = batchId,
            productName = product?.name ?: "Unknown Product",
            actionType = actionType,
            quantityChange = actualChange,
            quantityBefore = before,
            quantityAfter = after,
            reason = reason,
            timestamp = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        )
        _history.value = listOf(record) + _history.value
        return true
    }

    private val _userProfile = MutableStateFlow(
        UserProfile(
            storeName = "Aling Nena's Sari-Sari Store",
            ownerName = "Maria Santos",
            email = "nena.santos@gmail.com",
            phone = "+63 917 123 4567",
            businessType = "Sari-Sari Store / Micro-Retail",
            location = "Barangay Poblacion, Batangas",
            memberSince = "January 2026",
            isArchived = false
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun updateUserProfile(
        storeName: String,
        ownerName: String,
        email: String,
        phone: String,
        businessType: String,
        location: String
    ) {
        _userProfile.value = _userProfile.value.copy(
            storeName = storeName,
            ownerName = ownerName,
            email = email,
            phone = phone,
            businessType = businessType,
            location = location
        )
    }

    fun toggleArchiveUserProfile() {
        _userProfile.value = _userProfile.value.copy(
            isArchived = !_userProfile.value.isArchived
        )
    }

    fun resetUserProfile() {
        _userProfile.value = UserProfile()
    }

    // Derived helpers
    fun getProductTotalQuantity(productId: String): Double {
        return _batches.value.filter { it.productId == productId }.sumOf { it.quantity }
    }

    fun getProductBatchesFEFO(productId: String): List<BatchItem> {
        return _batches.value
            .filter { it.productId == productId }
            .sortedBy { it.expirationDate }
    }

    fun getAllBatchesFEFO(): List<BatchItem> {
        return _batches.value.sortedBy { it.expirationDate }
    }
}
