package com.bantaypetsa.app.feature.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bantaypetsa.app.core.designsystem.component.EmptyStateView
import com.bantaypetsa.app.core.designsystem.component.ExpiryStatusBadge
import com.bantaypetsa.app.core.designsystem.theme.AmberApproaching
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.OrangeAlert
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreen
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite
import com.bantaypetsa.app.core.util.SampleInventoryState
import com.bantaypetsa.app.domain.model.BatchItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun AlertsScreen(
    initialFilter: String = "All",
    onRecordStockAction: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AlertsViewModel = viewModel()
) {
    LaunchedEffect(initialFilter) {
        if (initialFilter.isNotBlank()) {
            viewModel.setInitialFilter(initialFilter)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftWhite)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Expiry Alerts",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp,
                color = CharcoalGreen
            )
            Text(
                text = "Prioritizing items nearing expiration date (FEFO).",
                style = MaterialTheme.typography.bodyMedium,
                color = CharcoalGreen.copy(alpha = 0.7f)
            )
        }

        // Filter chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("All", "Expired", "Urgent", "Approaching", "Safe")) { filter ->
                FilterChip(
                    selected = uiState.selectedFilter == filter,
                    onClick = { viewModel.onFilterChange(filter) },
                    label = {
                        Text(
                            filter,
                            fontWeight = if (uiState.selectedFilter == filter) FontWeight.Bold else FontWeight.Medium
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

        if (uiState.alertBatches.isEmpty()) {
            EmptyStateView(
                title = "All Stock Is In Order!",
                message = "No batches matching the selected filter '${uiState.selectedFilter}'.",
                icon = Icons.Default.CheckCircle
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.alertBatches, key = { it.batchId }) { batch ->
                    val prod = uiState.products.find { it.productId == batch.productId }
                    AlertBatchCard(
                        batch = batch,
                        productName = prod?.name ?: "Unknown Product",
                        unit = prod?.unit ?: "units",
                        onStockAction = { onRecordStockAction(batch.batchId) }
                    )
                }
            }
        }
    }
}

@Composable
fun AlertBatchCard(
    batch: BatchItem,
    productName: String,
    unit: String,
    onStockAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val status = SampleInventoryState.calculateStatus(batch.expirationDate)
    val formatter = remember { DateTimeFormatter.ofPattern("MMM dd, yyyy") }

    val daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), batch.expirationDate)
    val daysRemainingText = when {
        daysRemaining < 0 -> "${-daysRemaining} day(s) overdue (Expired)"
        daysRemaining == 0L -> "Expires today (0 days left)"
        daysRemaining == 1L -> "1 day left before expiration"
        else -> "$daysRemaining days left before expiration"
    }
    val daysRemainingColor = when {
        daysRemaining < 0 -> ExpenseRed
        daysRemaining <= 7 -> OrangeAlert
        daysRemaining <= 30 -> AmberApproaching
        else -> SuccessGreen
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalGreen,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                ExpiryStatusBadge(status = status)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Expiration: ${batch.expirationDate.format(formatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = CharcoalGreen
                )
                Text(
                    text = "Stock: ${if (batch.quantity % 1.0 == 0.0) batch.quantity.toInt() else batch.quantity} $unit",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalGreen
                )
            }

            if (batch.lotNumber.isNotBlank()) {
                Text(
                    text = "Lot Number: ${batch.lotNumber}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedGreen
                )
            }

            // Exact remaining days countdown below expiration & lot number
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = daysRemainingColor,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "Remaining:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = CharcoalGreen.copy(alpha = 0.8f)
                )
                Text(
                    text = daysRemainingText,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = daysRemainingColor
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onStockAction,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "Record Stock Action",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}