package com.bantaypetsa.app.feature.products

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bantaypetsa.app.core.designsystem.component.DatePickerField
import com.bantaypetsa.app.core.designsystem.component.bantayTextFieldColors
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.util.SampleInventoryState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    productId: String? = null,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val products by SampleInventoryState.products.collectAsState()
    val existingProduct = remember(productId, products) {
        products.find { it.productId == productId }
    }

    var name by remember(existingProduct) { mutableStateOf(existingProduct?.name ?: "") }
    var category by remember(existingProduct) { mutableStateOf(existingProduct?.category ?: "") }
    var unit by remember(existingProduct) { mutableStateOf(existingProduct?.unit ?: "") }
    var description by remember(existingProduct) { mutableStateOf(existingProduct?.description ?: "") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }
    var unitError by remember { mutableStateOf<String?>(null) }

    // Initial batch fields (for new products)
    var initialQuantityText by remember { mutableStateOf("") }
    var initialExpirationDate by remember { mutableStateOf(LocalDate.now().plusMonths(3)) }
    var initialDateReceived by remember { mutableStateOf(LocalDate.now()) }
    var initialLotNumber by remember { mutableStateOf("") }
    var initialQuantityError by remember { mutableStateOf<String?>(null) }

    val isEditing = existingProduct != null

    Scaffold(
        containerColor = SoftWhite,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = if (isEditing) "Edit Product" else "New Product",
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
                text = "Product Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CharcoalGreen
            )

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                },
                label = { Text("Product Name *") },
                placeholder = { Text("e.g. Bear Brand Fortified Powdered Milk") },
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it, color = ExpenseRed) } },
                colors = bantayTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = category,
                onValueChange = {
                    category = it
                    categoryError = null
                },
                label = { Text("Category *") },
                placeholder = { Text("e.g. Dairy & Milk, Canned Goods, Snacks") },
                isError = categoryError != null,
                supportingText = categoryError?.let { { Text(it, color = ExpenseRed) } },
                colors = bantayTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = unit,
                onValueChange = {
                    unit = it
                    unitError = null
                },
                label = { Text("Unit of Measure *") },
                placeholder = { Text("e.g. packs, cans, pcs, boxes") },
                isError = unitError != null,
                supportingText = unitError?.let { { Text(it, color = ExpenseRed) } },
                colors = bantayTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (Optional)") },
                placeholder = { Text("e.g. 320g pouch pack") },
                minLines = 2,
                maxLines = 4,
                colors = bantayTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Direct initial batch entry for new products (eliminates extra step)
            if (!isEditing) {
                HorizontalDivider(thickness = 0.8.dp, color = BorderSubtle)

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Initial Batch & Expiration (Optional)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )
                    Text(
                        text = "Record initial stock and expiry date directly without opening the batch screen.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CharcoalGreen.copy(alpha = 0.7f)
                    )
                }

                OutlinedTextField(
                    value = initialQuantityText,
                    onValueChange = {
                        initialQuantityText = it
                        initialQuantityError = null
                    },
                    label = { Text("Initial Quantity ${if (unit.isNotBlank()) "($unit)" else ""}") },
                    placeholder = { Text("e.g. 24") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = initialQuantityError != null,
                    supportingText = initialQuantityError?.let { { Text(it, color = ExpenseRed) } },
                    colors = bantayTextFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                DatePickerField(
                    label = "Expiration Date",
                    selectedDate = initialExpirationDate,
                    onDateSelected = { initialExpirationDate = it }
                )

                DatePickerField(
                    label = "Date Received",
                    selectedDate = initialDateReceived,
                    onDateSelected = { initialDateReceived = it }
                )

                OutlinedTextField(
                    value = initialLotNumber,
                    onValueChange = { initialLotNumber = it },
                    label = { Text("Lot Number / Batch Code (Optional)") },
                    placeholder = { Text("e.g. LOT-2026-A1") },
                    colors = bantayTextFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

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
                        var hasError = false
                        if (name.isBlank()) {
                            nameError = "Product name is required"
                            hasError = true
                        }
                        if (category.isBlank()) {
                            categoryError = "Category is required"
                            hasError = true
                        }
                        if (unit.isBlank()) {
                            unitError = "Unit is required"
                            hasError = true
                        }

                        val parsedInitialQty = initialQuantityText.toDoubleOrNull()
                        if (!isEditing && initialQuantityText.isNotBlank()) {
                            if (parsedInitialQty == null || parsedInitialQty <= 0.0) {
                                initialQuantityError = "Please enter a valid quantity greater than 0"
                                hasError = true
                            }
                        }

                        if (!hasError) {
                            if (isEditing) {
                                SampleInventoryState.updateProduct(
                                    productId = productId!!,
                                    name = name.trim(),
                                    category = category.trim(),
                                    unit = unit.trim(),
                                    description = description.trim()
                                )
                            } else {
                                val newProductId = SampleInventoryState.addProduct(
                                    name = name.trim(),
                                    category = category.trim(),
                                    unit = unit.trim(),
                                    description = description.trim()
                                )

                                // Automatically attach initial batch if quantity was specified
                                if (parsedInitialQty != null && parsedInitialQty > 0.0) {
                                    SampleInventoryState.addBatch(
                                        productId = newProductId,
                                        lotNumber = initialLotNumber.trim(),
                                        quantity = parsedInitialQty,
                                        expirationDate = initialExpirationDate,
                                        dateReceived = initialDateReceived
                                    )
                                }
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
                    Text(if (isEditing) "Save Changes" else "Save Product", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}