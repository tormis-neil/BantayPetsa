package com.bantaypetsa.app.feature.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bantaypetsa.app.core.util.SampleInventoryState
import com.bantaypetsa.app.domain.model.BatchItem
import com.bantaypetsa.app.domain.model.ExpiryStatus
import com.bantaypetsa.app.domain.model.ProductItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class AlertsUiState(
    val selectedFilter: String = "All", // "All", "Expired", "Urgent", "Approaching", "Safe"
    val alertBatches: List<BatchItem> = emptyList(),
    val products: List<ProductItem> = emptyList(),
    val isLoading: Boolean = false
)

class AlertsViewModel : ViewModel() {
    private val _selectedFilter = MutableStateFlow("All")

    val uiState: StateFlow<AlertsUiState> = combine(
        SampleInventoryState.batches,
        SampleInventoryState.products,
        _selectedFilter
    ) { batches, products, filter ->
        // Only active inventory with stock on shelf triggers expiry alerts
        val sorted = SampleInventoryState.getAllBatchesFEFO().filter { it.quantity > 0.0 }
        val filtered = when (filter) {
            "Expired" -> sorted.filter { SampleInventoryState.calculateStatus(it.expirationDate) == ExpiryStatus.EXPIRED }
            "Urgent" -> sorted.filter { SampleInventoryState.calculateStatus(it.expirationDate) == ExpiryStatus.URGENT }
            "Approaching", "Approaching Expiry" -> sorted.filter { SampleInventoryState.calculateStatus(it.expirationDate) == ExpiryStatus.APPROACHING }
            "Safe" -> sorted.filter { SampleInventoryState.calculateStatus(it.expirationDate) == ExpiryStatus.SAFE }
            else -> sorted.filter { SampleInventoryState.calculateStatus(it.expirationDate) != ExpiryStatus.SAFE }
        }

        AlertsUiState(
            selectedFilter = filter,
            alertBatches = filtered,
            products = products,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AlertsUiState(isLoading = true)
    )

    fun onFilterChange(newFilter: String) {
        _selectedFilter.value = newFilter
    }

    fun setInitialFilter(filter: String) {
        _selectedFilter.value = filter
    }
}