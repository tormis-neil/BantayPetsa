package com.bantaypetsa.app.feature.batches

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bantaypetsa.app.core.designsystem.component.EmptyStateView
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CategoryBlue
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRedContainer
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreen
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite
import com.bantaypetsa.app.core.util.SampleInventoryState
import com.bantaypetsa.app.domain.model.StockActionType
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordStockActionScreen(
    batchId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val batches by SampleInventoryState.batches.collectAsState()
    val batch = remember(batchId, batches) { batches.find { it.batchId == batchId } }

    val products by SampleInventoryState.products.collectAsState()
    val product = remember(batch, products) {
        batch?.let { b -> products.find { it.productId == b.productId } }
    }

    if (batch == null || product == null) {
        Scaffold(
            containerColor = SoftWhite,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Stock Action",
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
                title = "Batch Not Found",
                message = "The requested batch details could not be found.",
                actionLabel = "Go Back",
                onActionClick = onNavigateBack,
                modifier = Modifier.padding(padding)
            )
        }
        return
    }

    var selectedActionType by remember { mutableStateOf(StockActionType.REMOVE) }
    var quantityInput by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val parsedInput = quantityInput.toDoubleOrNull() ?: 0.0
    val beforeQty = batch.quantity
    val afterQty = when (selectedActionType) {
        StockActionType.ADD -> beforeQty + parsedInput
        StockActionType.REMOVE -> beforeQty - parsedInput
        StockActionType.ADJUST -> parsedInput
    }

    val isNegative = afterQty < 0.0

    Scaffold(
        containerColor = SoftWhite,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Record Stock Action",
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = SoftWhite
                    )
                )
                HorizontalDivider(thickness = 0.8.dp, color = BorderSubtle)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(SoftWhite)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Target batch summary card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )
                    Text(
                        text = "Current Batch Stock: ${if (beforeQty % 1.0 == 0.0) beforeQty.toInt() else beforeQty} ${product.unit}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryGreen
                    )
                    Text(
                        text = "Expires on: ${batch.expirationDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = CharcoalGreen.copy(alpha = 0.7f)
                    )
                }
            }

            Text(
                text = "Select Action Type",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalGreen
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedActionType == StockActionType.REMOVE,
                    onClick = {
                        selectedActionType = StockActionType.REMOVE
                        validationError = null
                    },
                    label = { Text("Deduct") },
                    leadingIcon = { Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ExpenseRed,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = selectedActionType == StockActionType.ADD,
                    onClick = {
                        selectedActionType = StockActionType.ADD
                        validationError = null
                    },
                    label = { Text("Add") },
                    leadingIcon = { Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SuccessGreen,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = selectedActionType == StockActionType.ADJUST,
                    onClick = {
                        selectedActionType = StockActionType.ADJUST
                        validationError = null
                    },
                    label = { Text("Set / Audit") },
                    leadingIcon = { Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CategoryBlue,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = quantityInput,
                onValueChange = {
                    quantityInput = it
                    validationError = null
                },
                label = {
                    Text(
                        if (selectedActionType == StockActionType.ADJUST)
                            "New Exact Stock Quantity (${product.unit}) *"
                        else
                            "Quantity to ${if (selectedActionType == StockActionType.ADD) "Add" else "Deduct"} (${product.unit}) *"
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = validationError != null || (quantityInput.isNotBlank() && isNegative),
                supportingText = {
                    when {
                        validationError != null -> Text(validationError!!)
                        quantityInput.isNotBlank() && isNegative -> Text("Deduction causes negative inventory ($afterQty ${product.unit}). Action blocked.")
                        else -> null
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Live resulting stock calculation preview
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isNegative) ExpenseRedContainer else PaleGreen
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Resulting Stock After Action:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = CharcoalGreen
                    )
                    Text(
                        text = "${if (afterQty % 1.0 == 0.0) afterQty.toInt() else afterQty} ${product.unit}",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isNegative) ExpenseRed else PrimaryGreen
                    )
                }
            }

            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("Reason for Stock Action *") },
                placeholder = {
                    Text(
                        when (selectedActionType) {
                            StockActionType.REMOVE -> "e.g. Sold in store, Discarded expired batch, Damaged"
                            StockActionType.ADD -> "e.g. Restock shipment arrival, Returned by customer"
                            StockActionType.ADJUST -> "e.g. Physical inventory count reconciliation"
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Cancel", color = CharcoalGreen)
                }

                Button(
                    onClick = {
                        val qty = quantityInput.toDoubleOrNull()
                        if (qty == null || qty <= 0.0) {
                            validationError = "Please enter a valid positive quantity"
                            return@Button
                        }
                        if (reason.isBlank()) {
                            validationError = "Please specify a reason for audit history"
                            return@Button
                        }
                        if (isNegative) {
                            validationError = "Negative inventory is strictly prohibited"
                            return@Button
                        }

                        val success = SampleInventoryState.recordStockAction(
                            batchId = batchId,
                            actionType = selectedActionType,
                            quantityChange = qty,
                            reason = reason.trim()
                        )

                        if (success) {
                            onNavigateBack()
                        } else {
                            validationError = "Unable to record action due to negative inventory constraint"
                        }
                    },

                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Confirm Action", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}