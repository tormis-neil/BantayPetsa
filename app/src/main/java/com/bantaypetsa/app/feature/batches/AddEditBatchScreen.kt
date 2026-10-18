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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bantaypetsa.app.core.designsystem.component.DatePickerField
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.util.SampleInventoryState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBatchScreen(
    productId: String,
    batchId: String? = null,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val products by SampleInventoryState.products.collectAsState()
    val product = remember(productId, products) { products.find { it.productId == productId } }

    val batches by SampleInventoryState.batches.collectAsState()
    val existingBatch = remember(batchId, batches) { batches.find { it.batchId == batchId } }

    val isEditing = existingBatch != null

    var lotNumber by remember(existingBatch) { mutableStateOf(existingBatch?.lotNumber ?: "") }
    var quantityText by remember(existingBatch) {
        mutableStateOf(existingBatch?.let { if (it.quantity % 1.0 == 0.0) it.quantity.toInt().toString() else it.quantity.toString() } ?: "")
    }
    var expirationDate by remember(existingBatch) {
        mutableStateOf(existingBatch?.expirationDate ?: LocalDate.now().plusMonths(3))
    }
    var dateReceived by remember(existingBatch) {
        mutableStateOf(existingBatch?.dateReceived ?: LocalDate.now())
    }

    var quantityError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = SoftWhite,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = if (isEditing) "Edit Batch" else "New Batch",
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
            Text(
                text = "Product: ${product?.name ?: "Unknown"}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CharcoalGreen
            )

            OutlinedTextField(
                value = quantityText,
                onValueChange = {
                    quantityText = it
                    quantityError = null
                },
                label = { Text("Initial Quantity (${product?.unit ?: "units"}) *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = quantityError != null,
                supportingText = quantityError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            DatePickerField(
                label = "Expiration Date *",
                selectedDate = expirationDate,
                onDateSelected = { expirationDate = it }
            )

            DatePickerField(
                label = "Date Received *",
                selectedDate = dateReceived,
                onDateSelected = { dateReceived = it }
            )

            OutlinedTextField(
                value = lotNumber,
                onValueChange = { lotNumber = it },
                label = { Text("Lot Number / Batch Code (Optional)") },
                placeholder = { Text("e.g. LOT-2026-A1") },
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
                        val qty = quantityText.toDoubleOrNull()
                        if (qty == null || qty <= 0.0) {
                            quantityError = "Please enter a valid quantity greater than 0"
                        } else {
                            if (isEditing) {
                                SampleInventoryState.updateBatch(
                                    batchId = batchId!!,
                                    lotNumber = lotNumber.trim(),
                                    quantity = qty,
                                    expirationDate = expirationDate,
                                    dateReceived = dateReceived
                                )
                            } else {
                                SampleInventoryState.addBatch(
                                    productId = productId,
                                    lotNumber = lotNumber.trim(),
                                    quantity = qty,
                                    expirationDate = expirationDate,
                                    dateReceived = dateReceived
                                )
                            }
                            onNavigateBack()
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
                    Text(if (isEditing) "Save Changes" else "Save Batch", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}