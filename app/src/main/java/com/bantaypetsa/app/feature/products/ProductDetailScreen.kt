package com.bantaypetsa.app.feature.products

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bantaypetsa.app.core.designsystem.component.ConfirmationDialog
import com.bantaypetsa.app.core.designsystem.component.EmptyStateView
import com.bantaypetsa.app.core.designsystem.component.ExpiryStatusBadge
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite
import com.bantaypetsa.app.core.util.SampleInventoryState
import com.bantaypetsa.app.domain.model.BatchItem
import java.time.format.DateTimeFormatter

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
    val product = remember(productId, products) { products.find { it.productId == productId } }

    val batches by SampleInventoryState.batches.collectAsState()
    val productBatches = remember(productId, batches) {
        SampleInventoryState.getProductBatchesFEFO(productId)
    }
    val totalQuantity = remember(productId, batches) {
        SampleInventoryState.getProductTotalQuantity(productId)
    }

    var showArchiveDialog by remember { mutableStateOf(false) }

    if (product == null) {
        Scaffold(
            containerColor = SoftWhite,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Product Details",
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
                    }
                )
            }
        ) { padding ->
            EmptyStateView(
                title = "Product Not Found",
                message = "This product may have been deleted or does not exist.",
                actionLabel = "Back to Products",
                onActionClick = onNavigateBack,
                modifier = Modifier.padding(padding)
            )
        }
        return
    }

    Scaffold(
        containerColor = SoftWhite,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = product.name,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalGreen,
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
                        Row(
                            modifier = Modifier.padding(end = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { onEditProduct(product.productId) },
                                modifier = Modifier
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Product",
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = { showArchiveDialog = true },
                                modifier = Modifier
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = if (product.isArchived) Icons.Default.Unarchive else Icons.Default.Archive,
                                    contentDescription = if (product.isArchived) "Restore" else "Archive",
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
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
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = product.category,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MutedGreen
                            )
                            if (product.isArchived) {
                                Text(
                                    text = "Archived",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ExpenseRed,
                                    modifier = Modifier
                                        .background(ExpenseRed.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = CharcoalGreen
                        )

                        if (product.description.isNotBlank()) {
                            Text(
                                text = product.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = CharcoalGreen.copy(alpha = 0.8f)
                            )
                        }

                        HorizontalDivider(thickness = 0.8.dp, color = BorderSubtle)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Available Quantity",
                                    style = MaterialTheme.typography.labelSmall,
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
                    Text("Stock Action", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }
    }
}