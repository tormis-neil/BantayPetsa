package com.bantaypetsa.app.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bantaypetsa.app.core.util.SampleInventoryState
import com.bantaypetsa.app.domain.model.StockActionRecord
import com.bantaypetsa.app.domain.model.StockActionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class HistoryUiState(
    val selectedTimeRange: String = "All Time",
    val selectedActionType: String = "All",
    val filteredRecords: List<StockActionRecord> = emptyList(),
    val totalAdditions: Double = 0.0,
    val totalRemovals: Double = 0.0,
    val totalAdjustments: Int = 0,
    val netMovement: Double = 0.0,
    val reportTitle: String = "All-Time Action & Expiry Report",
    val isLoading: Boolean = false
)

class HistoryViewModel : ViewModel() {
    private val _selectedTimeRange = MutableStateFlow("All Time")
    private val _selectedActionType = MutableStateFlow("All")

    val uiState: StateFlow<HistoryUiState> = combine(
        SampleInventoryState.history,
        _selectedTimeRange,
        _selectedActionType
    ) { historyList, timeRange, actionType ->
        val today = LocalDate.now()
        val timeFiltered = when (timeRange) {
            "Last 7 Days" -> historyList.filter { record ->
                try {
                    val date = LocalDate.parse(record.timestamp, DateTimeFormatter.ISO_DATE)
                    val daysBetween = ChronoUnit.DAYS.between(date, today)
                    daysBetween in 0..7
                } catch (e: Exception) {
                    true
                }
            }
            "Last 30 Days" -> historyList.filter { record ->
                try {
                    val date = LocalDate.parse(record.timestamp, DateTimeFormatter.ISO_DATE)
                    val daysBetween = ChronoUnit.DAYS.between(date, today)
                    daysBetween in 0..30
                } catch (e: Exception) {
                    true
                }
            }
            else -> historyList
        }

        val additions = timeFiltered.filter { it.actionType == StockActionType.ADD }.sumOf { it.quantityChange }
        val removals = timeFiltered.filter { it.actionType == StockActionType.REMOVE }.sumOf { -it.quantityChange }
        val adjustments = timeFiltered.count { it.actionType == StockActionType.ADJUST }
        val net = additions - removals

        val filtered = when (actionType) {
            "Additions" -> timeFiltered.filter { it.actionType == StockActionType.ADD }
            "Removals" -> timeFiltered.filter { it.actionType == StockActionType.REMOVE }
            "Adjustments" -> timeFiltered.filter { it.actionType == StockActionType.ADJUST }
            else -> timeFiltered
        }

        val title = when (timeRange) {
            "Last 7 Days" -> "Weekly Action Report (Last 7 Days)"
            "Last 30 Days" -> "Monthly Action Report (Last 30 Days)"
            else -> "All-Time Action & Expiry Report"
        }

        HistoryUiState(
            selectedTimeRange = timeRange,
            selectedActionType = actionType,
            filteredRecords = filtered,
            totalAdditions = additions,
            totalRemovals = removals,
            totalAdjustments = adjustments,
            netMovement = net,
            reportTitle = title,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryUiState(isLoading = true)
    )

    fun onTimeRangeChange(newTimeRange: String) {
        _selectedTimeRange.value = newTimeRange
    }

    fun onActionTypeChange(newActionType: String) {
        _selectedActionType.value = newActionType
    }
}
