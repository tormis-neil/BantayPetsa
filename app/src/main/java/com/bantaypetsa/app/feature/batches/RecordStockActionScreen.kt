package com.bantaypetsa.app.feature.batches

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bantaypetsa.app.core.designsystem.component.bantayTextFieldColors
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CategoryBlue
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRedContainer
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreen
import com.bantaypetsa.app.core.util.SampleInventoryState
import com.bantaypetsa.app.domain.model.StockActionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordStockActionScreen(
    batchId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val batches by SampleInventoryState.batches.collectAsState()
    val products by SampleInventoryState.products.collectAsState()

    val batch = batches.find { it.batchId == batchId }
    val product = products.find { it.productId == batch?.productId }

    if (batch == null || product == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(SoftWhite)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Batch or product not found.", color = ExpenseRed)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = onNavigateBack) {
                Text("Return", color = CharcoalGreen)
            }
        }
        return
    }

    var selectedActionType by remember { mutableStateOf(StockActionType.REMOVE) }
    var quantityInput by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    // Live calculation
    val parsedQty = quantityInput.toDoubleOrNull() ?: 0.0
    val afterQty = when (selectedActionType) {
        StockActionType.ADD -> batch.quantity + parsedQty
        StockActionType.REMOVE -> batch.quantity - parsedQty
        StockActionType.ADJUST -> parsedQty
    }
    val isNegative = afterQty < 0.0

    Scaffold(
        containerColor = SoftWhite,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Record Stock Movement",
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
            // Batch Context Info Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PaleGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )
                    Text(
                        text = "Current Stock: ${if (batch.quantity % 1.0 == 0.0) batch.quantity.toInt() else batch.quantity} ${product.unit}  â€¢  Lot: ${batch.lotNumber.ifBlank { "N/A" }}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CharcoalGreen.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Expires: ${batch.expirationDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedGreen
                    )
                }
            }

            Text(
                text = "Select Action Type",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = CharcoalGreen
            )

            // Action Selection Tabs
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
                colors = bantayTextFieldColors(),
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
                colors = bantayTextFieldColors(),
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