package com.bantaypetsa.app.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bantaypetsa.app.core.util.SampleInventoryState
import com.bantaypetsa.app.domain.model.BatchItem
import com.bantaypetsa.app.domain.model.ProductItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ProductsUiState(
    val searchQuery: String = "",
    val selectedFilter: String = "Active", // "Active", "All", "Archived"
    val products: List<ProductItem> = emptyList(),
    val batches: List<BatchItem> = emptyList(),
    val isLoading: Boolean = false
)

class ProductsViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow("Active")

    val uiState: StateFlow<ProductsUiState> = combine(
        SampleInventoryState.products,
        SampleInventoryState.batches,
        _searchQuery,
        _selectedFilter
    ) { allProducts, allBatches, query, filter ->
        val filtered = allProducts.filter { product ->
            val matchesFilter = when (filter) {
                "Active" -> !product.isArchived
                "Archived" -> product.isArchived
                else -> true
            }
            val matchesQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true)

            matchesFilter && matchesQuery
        }

        ProductsUiState(
            searchQuery = query,
            selectedFilter = filter,
            products = filtered,
            batches = allBatches,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProductsUiState(isLoading = true)
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onFilterChange(newFilter: String) {
        _selectedFilter.value = newFilter
    }
}
