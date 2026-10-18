package com.bantaypetsa.app.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bantaypetsa.app.core.util.SampleInventoryState
import com.bantaypetsa.app.domain.model.BatchItem
import com.bantaypetsa.app.domain.model.ExpiryStatus
import com.bantaypetsa.app.domain.model.ProductItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val expiredCount: Int = 0,
    val urgentCount: Int = 0,
    val approachingCount: Int = 0,
    val safeCount: Int = 0,
    val expiredPct: Int = 0,
    val urgentPct: Int = 0,
    val approachingPct: Int = 0,
    val safePct: Int = 0,
    val activeProductCount: Int = 0,
    val totalBatchesCount: Int = 0,
    val attentionBatches: List<BatchItem> = emptyList(),
    val products: List<ProductItem> = emptyList(),
    val isLoading: Boolean = false
)

class DashboardViewModel : ViewModel() {
    val uiState: StateFlow<DashboardUiState> = combine(
        SampleInventoryState.products,
        SampleInventoryState.batches
    ) { products, batches ->
        val totalBatches = batches.size
        val expired = batches.count { SampleInventoryState.calculateStatus(it.expirationDate) == ExpiryStatus.EXPIRED }
        val urgent = batches.count { SampleInventoryState.calculateStatus(it.expirationDate) == ExpiryStatus.URGENT }
        val approaching = batches.count { SampleInventoryState.calculateStatus(it.expirationDate) == ExpiryStatus.APPROACHING }
        val safe = batches.count { SampleInventoryState.calculateStatus(it.expirationDate) == ExpiryStatus.SAFE }

        val activeProducts = products.count { !it.isArchived }

        val expiredPct = if (totalBatches > 0) (expired * 100 / totalBatches) else 0
        val urgentPct = if (totalBatches > 0) (urgent * 100 / totalBatches) else 0
        val approachingPct = if (totalBatches > 0) (approaching * 100 / totalBatches) else 0
        val safePct = if (totalBatches > 0) (safe * 100 / totalBatches) else 0

        val attention = SampleInventoryState.getAllBatchesFEFO().filter {
            val s = SampleInventoryState.calculateStatus(it.expirationDate)
            s == ExpiryStatus.EXPIRED || s == ExpiryStatus.URGENT || s == ExpiryStatus.APPROACHING
        }.take(5)

        DashboardUiState(
            expiredCount = expired,
            urgentCount = urgent,
            approachingCount = approaching,
            safeCount = safe,
            expiredPct = expiredPct,
            urgentPct = urgentPct,
            approachingPct = approachingPct,
            safePct = safePct,
            activeProductCount = activeProducts,
            totalBatchesCount = totalBatches,
            attentionBatches = attention,
            products = products,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )
}
