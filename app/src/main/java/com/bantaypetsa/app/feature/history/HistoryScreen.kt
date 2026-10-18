package com.bantaypetsa.app.feature.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bantaypetsa.app.core.designsystem.component.EmptyStateView
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CategoryBlue
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreen
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite
import com.bantaypetsa.app.domain.model.StockActionRecord
import com.bantaypetsa.app.domain.model.StockActionType

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftWhite)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Screen Header
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "History & Reports",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp,
                color = CharcoalGreen
            )
            Text(
                text = "Aggregated stock reports and complete audit trail.",
                style = MaterialTheme.typography.bodyMedium,
                color = CharcoalGreen.copy(alpha = 0.7f)
            )
        }

        // 1. Time Range Filter Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(18.dp)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("All Time", "Last 7 Days", "Last 30 Days")) { timeOption ->
                    val isSelected = uiState.selectedTimeRange == timeOption
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.onTimeRangeChange(timeOption) },
                        label = {
                            Text(
                                text = timeOption,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryGreen,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // 2. Dynamic Basic Reports Summary Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(PaleGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = uiState.reportTitle,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalGreen
                            )
                            Text(
                                text = "Summary of recorded inventory movements",
                                style = MaterialTheme.typography.bodyMedium,
                                color = CharcoalGreen.copy(alpha = 0.75f),
                                fontSize = 13.sp
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PaleGreen
                    ) {
                        Text(
                            text = "${uiState.filteredRecords.size} Actions",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                HorizontalDivider(thickness = 0.8.dp, color = BorderSubtle)

                // 3-Column Metrics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ReportStatItem(
                        label = "Restocked",
                        value = "+${formatQuantity(uiState.totalAdditions)}",
                        color = SuccessGreen,
                        icon = Icons.Default.ArrowUpward,
                        modifier = Modifier.weight(1f)
                    )
                    ReportStatItem(
                        label = "Deducted/Sold",
                        value = "-${formatQuantity(uiState.totalRemovals)}",
                        color = ExpenseRed,
                        icon = Icons.Default.ArrowDownward,
                        modifier = Modifier.weight(1f)
                    )
                    ReportStatItem(
                        label = "Net Movement",
                        value = "${if (uiState.netMovement >= 0) "+" else ""}${formatQuantity(uiState.netMovement)}",
                        color = if (uiState.netMovement >= 0) PrimaryGreen else ExpenseRed,
                        icon = Icons.Default.Sync,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. Action Type Filter Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("All", "Removals", "Additions", "Adjustments")) { filter ->
                val isSelected = uiState.selectedActionType == filter
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.onActionTypeChange(filter) },
                    label = {
                        Text(
                            text = filter,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CharcoalGreen,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // 4. Audit Trail List
        if (uiState.filteredRecords.isEmpty()) {
            EmptyStateView(
                title = "No Stock Actions Recorded",
                message = "No inventory movements match '${uiState.selectedTimeRange}' and filter '${uiState.selectedActionType}'."
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.filteredRecords, key = { it.actionId }) { record ->
                    HistoryItemCard(record = record)
                }
            }
        }
    }
}

@Composable
fun ReportStatItem(
    label: String,
    value: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = CharcoalGreen.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = color,
            fontSize = 16.sp
        )
    }
}

@Composable
fun HistoryItemCard(
    record: StockActionRecord,
    modifier: Modifier = Modifier
) {
    val (actionLabel, actionColor) = when (record.actionType) {
        StockActionType.ADD -> "ADDITION" to SuccessGreen
        StockActionType.REMOVE -> "REMOVAL" to ExpenseRed
        StockActionType.ADJUST -> "ADJUSTMENT" to CategoryBlue
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = record.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalGreen
                )

                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = actionColor
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val deltaText = if (record.quantityChange > 0) "+${formatQuantity(record.quantityChange)}" else formatQuantity(record.quantityChange)
                Text(
                    text = "Balance: ${formatQuantity(record.quantityBefore)} \u2192 ${formatQuantity(record.quantityAfter)} ($deltaText)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = CharcoalGreen
                )
                Text(
                    text = record.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedGreen
                )
            }

            if (record.reason.isNotBlank()) {
                Text(
                    text = "Reason: ${record.reason}",
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalGreen.copy(alpha = 0.7f)
                )
            }
        }
    }
}

private fun formatQuantity(qty: Double): String {
    return if (qty % 1.0 == 0.0) qty.toInt().toString() else qty.toString()
}
