package com.bantaypetsa.app.feature.products

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bantaypetsa.app.core.designsystem.component.ConfirmationDialog
import com.bantaypetsa.app.core.designsystem.component.EmptyStateView
import com.bantaypetsa.app.core.designsystem.component.ExpiryStatusBadge
import com.bantaypetsa.app.core.designsystem.theme.AmberApproaching
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.OrangeAlert
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreen
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite
import com.bantaypetsa.app.core.util.SampleInventoryState
import com.bantaypetsa.app.domain.engine.FefoEngine
import com.bantaypetsa.app.domain.model.BatchItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    onNavigateBack: () -> Unit,
    onEditProduct: (String) -> Unit,
    onAddBatch: (String) -> Unit,
    onEditBatch: (String, String) -> Unit,
    onRecordStockAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val products by SampleInventoryState.products.collectAsState()
    val allBatches by SampleInventoryState.batches.collectAsState()

    val product = products.find { it.productId == productId }
    val productBatches = remember(productId, allBatches) {
        FefoEngine.sortBatches(allBatches.filter { it.productId == productId })
    }

    var showArchiveDialog by remember { mutableStateOf(false) }

    if (product == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(SoftWhite)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Product not found.", color = ExpenseRed)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = onNavigateBack) {
                Text("Back to Products", color = CharcoalGreen)
            }
        }
        return
    }

    val totalQuantity = productBatches.sumOf { it.quantity }
    val nearestBatch = productBatches.minByOrNull { it.expirationDate }

    Scaffold(
        containerColor = SoftWhite,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = product.name,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalGreen,
                            maxLines = 1,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = PrimaryGreen
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { onEditProduct(product.productId) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Product", tint = PrimaryGreen)
                        }
                        IconButton(onClick = { showArchiveDialog = true }) {
                            Icon(
                                imageVector = if (product.isArchived) Icons.Default.Unarchive else Icons.Default.Archive,
                                contentDescription = if (product.isArchived) "Restore Product" else "Archive Product",
                                tint = if (product.isArchived) PrimaryGreen else ExpenseRed
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = SoftWhite
                    )
                )
                HorizontalDivider(thickness = 0.8.dp, color = BorderSubtle)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(SoftWhite)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Product summary card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = product.category,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedGreen
                                )
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = CharcoalGreen
                                )
                            }
                            if (product.isArchived) {
                                Text(
                                    text = "ARCHIVED",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ExpenseRed,
                                    modifier = Modifier
                                        .background(PaleGreen, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        if (product.description.isNotBlank()) {
                            Text(
                                text = product.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = CharcoalGreen.copy(alpha = 0.8f)
                            )
                        }

                        HorizontalDivider(thickness = 0.5.dp, color = BorderSubtle)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total On-Hand Stock",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CharcoalGreen.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "${if (totalQuantity % 1.0 == 0.0) totalQuantity.toInt() else totalQuantity} ${product.unit}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PrimaryGreen
                                )
                            }
                            Text(
                                text = "${productBatches.size} batch(es)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = CharcoalGreen
                            )
                        }

                        // Product-level Earliest Expiry Countdown
                        if (nearestBatch != null) {
                            val daysToNearest = ChronoUnit.DAYS.between(LocalDate.now(), nearestBatch.expirationDate)
                            val nearestDaysText = when {
                                daysToNearest < 0 -> "${-daysToNearest} day(s) overdue (Expired)"
                                daysToNearest == 0L -> "Expires today (0 days left)"
                                daysToNearest == 1L -> "1 day left before expiration"
                                else -> "$daysToNearest days left before expiration"
                            }
                            val nearestColor = when {
                                daysToNearest < 0 -> ExpenseRed
                                daysToNearest <= 7 -> OrangeAlert
                                daysToNearest <= 30 -> AmberApproaching
                                else -> SuccessGreen
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(PaleGreen.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = nearestColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Earliest Expiry:",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium,
                                        color = CharcoalGreen
                                    )
                                }
                                Text(
                                    text = nearestDaysText,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = nearestColor
                                )
                            }
                        }
                    }
                }
            }

            // Batches header and Add Batch button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Batches (FEFO Ordering)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalGreen
                        )
                        Text(
                            text = "Prioritized by earliest expiration date",
                            style = MaterialTheme.typography.bodySmall,
                            color = CharcoalGreen.copy(alpha = 0.7f)
                        )
                    }

                    Button(
                        onClick = { onAddBatch(product.productId) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("Add Batch", fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }

            // Batches list or Empty
            if (productBatches.isEmpty()) {
                item {
                    EmptyStateView(
                        title = "No Active Batches",
                        message = "Tap 'Add Batch' to record your first batch and its expiry date.",
                        actionLabel = "Add Batch",
                        onActionClick = { onAddBatch(product.productId) }
                    )
                }
            } else {
                items(productBatches, key = { it.batchId }) { batch ->
                    BatchItemCard(
                        batch = batch,
                        unit = product.unit,
                        onEdit = { onEditBatch(product.productId, batch.batchId) },
                        onStockAction = { onRecordStockAction(batch.batchId) }
                    )
                }
            }
        }
    }

    if (showArchiveDialog) {
        ConfirmationDialog(
            title = if (product.isArchived) "Restore Product?" else "Archive Product?",
            message = if (product.isArchived) {
                "The product '${product.name}' will become active again in the catalog."
            } else {
                "The product '${product.name}' will be hidden from the active list, but its stock history remains preserved."
            },
            confirmLabel = if (product.isArchived) "Restore" else "Archive",
            isDestructive = !product.isArchived,
            onConfirm = {
                SampleInventoryState.toggleArchiveProduct(product.productId)
                showArchiveDialog = false
            },
            onDismiss = { showArchiveDialog = false }
        )
    }
}

@Composable
fun BatchItemCard(
    batch: BatchItem,
    unit: String,
    onEdit: () -> Unit,
    onStockAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val status = SampleInventoryState.calculateStatus(batch.expirationDate)
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM dd, yyyy") }

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
                ExpiryStatusBadge(status = status)

                Text(
                    text = "${if (batch.quantity % 1.0 == 0.0) batch.quantity.toInt() else batch.quantity} $unit",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = CharcoalGreen
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Expiration: ${batch.expirationDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = CharcoalGreen
                )
                if (batch.lotNumber.isNotBlank()) {
                    Text(
                        text = "Lot: ${batch.lotNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedGreen
                    )
                }
            }

            Text(
                text = "Received on: ${batch.dateReceived.format(dateFormatter)}",
                style = MaterialTheme.typography.labelSmall,
                color = MutedGreen
            )

            // Exact remaining days indicator below expiration and received date
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
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = CharcoalGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.size(4.dp))
                    Text("Edit", style = MaterialTheme.typography.labelMedium, color = CharcoalGreen)
                }

                Spacer(modifier = Modifier.size(8.dp))

                Button(
                    onClick = onStockAction,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.History, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.size(4.dp))
                    Text("Record Stock Action", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }
    }
}