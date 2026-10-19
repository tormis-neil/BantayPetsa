package com.bantaypetsa.app.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bantaypetsa.app.R
import com.bantaypetsa.app.core.designsystem.component.bantayTextFieldColors
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite
import com.bantaypetsa.app.core.util.SampleInventoryState
import com.bantaypetsa.app.domain.engine.DomainValidators

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 0: Sign In, 1: Sign Up
    var selectedTab by remember { mutableIntStateOf(0) }

    // Sign In Fields (Prefilled with sample data for instant zero-friction login)
    var signInEmail by remember { mutableStateOf("nena.santos@gmail.com") }
    var signInPin by remember { mutableStateOf("1234") }
    var isSignInPasswordVisible by remember { mutableStateOf(false) }

    // Sign Up Fields
    var signUpStoreName by remember { mutableStateOf("Aling Nena's Sari-Sari Store") }
    var signUpOwnerName by remember { mutableStateOf("Maria Santos") }
    var signUpPhone by remember { mutableStateOf("09171234567") }
    var signUpCategory by remember { mutableStateOf("Sari-Sari Store / Micro-Retail") }
    var signUpPin by remember { mutableStateOf("1234") }
    var isSignUpPasswordVisible by remember { mutableStateOf(false) }

    var phoneError by remember { mutableStateOf<String?>(null) }
    var pinError by remember { mutableStateOf<String?>(null) }
    var storeNameError by remember { mutableStateOf<String?>(null) }
    var ownerNameError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftWhite)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Brand Logo & Header
        Surface(
            modifier = Modifier
                .size(76.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(18.dp),
                    spotColor = PrimaryGreen.copy(alpha = 0.25f)
                ),
            shape = RoundedCornerShape(18.dp),
            color = SurfaceWhite
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "BantayPetsa Logo",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(14.dp))
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "BantayPetsa",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = CharcoalGreen
            )
            Text(
                text = "Retail Inventory Assistant",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryGreen
            )
            Text(
                text = "Bantay sa petsa. Alaga sa paninda.",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = PrimaryGreen.copy(alpha = 0.8f)
            )
        }

        // Tab Switcher (Sign In vs Create Store Account)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(PaleGreen)
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selectedTab == 0) PrimaryGreen else Color.Transparent)
                    .clickable { selectedTab = 0 }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign In",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (selectedTab == 0) Color.White else CharcoalGreen
                )
            }

            Box(
                modifier = Modifier
                    .weight(1.3f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selectedTab == 1) PrimaryGreen else Color.Transparent)
                    .clickable { selectedTab = 1 }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Create Store Account",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (selectedTab == 1) Color.White else CharcoalGreen
                )
            }
        }

        // Main Card Form
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (selectedTab == 0) {
                    // Sign In Form
                    Text(
                        text = "Sign In to Your Store",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )

                    OutlinedTextField(
                        value = signInEmail,
                        onValueChange = { signInEmail = it },
                        label = { Text("Store Owner Email or Phone") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = PrimaryGreen)
                        },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = signInPin,
                        onValueChange = { input ->
                            signInPin = input.filter { it.isDigit() }.take(6)
                        },
                        label = { Text("Security PIN or Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryGreen)
                        },
                        trailingIcon = {
                            val icon = if (isSignInPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                            val description = if (isSignInPasswordVisible) "Hide password" else "Show password"
                            IconButton(onClick = { isSignInPasswordVisible = !isSignInPasswordVisible }) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = description,
                                    tint = MutedGreen
                                )
                            }
                        },
                        visualTransformation = if (isSignInPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = onAuthSuccess,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Sign In / Open Store",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = onAuthSuccess,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Quick Demo Access (One Tap)",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = CharcoalGreen
                        )
                    }
                } else {
                    // Sign Up Form
                    Text(
                        text = "Register Your Store Profile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )

                    OutlinedTextField(
                        value = signUpStoreName,
                        onValueChange = {
                            signUpStoreName = it
                            storeNameError = null
                        },
                        label = { Text("Store / Business Name *") },
                        leadingIcon = {
                            Icon(Icons.Default.Store, contentDescription = null, tint = PrimaryGreen)
                        },
                        isError = storeNameError != null,
                        supportingText = storeNameError?.let { { Text(it, color = ExpenseRed) } },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = signUpOwnerName,
                        onValueChange = {
                            signUpOwnerName = it
                            ownerNameError = null
                        },
                        label = { Text("Owner Full Name *") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryGreen)
                        },
                        isError = ownerNameError != null,
                        supportingText = ownerNameError?.let { { Text(it, color = ExpenseRed) } },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = signUpPhone,
                        onValueChange = { input ->
                            // Strictly allow only numbers and maximum 11 digits
                            val digitsOnly = input.filter { it.isDigit() }.take(11)
                            signUpPhone = digitsOnly
                            phoneError = null
                        },
                        label = { Text("Contact Phone *") },
                        placeholder = { Text("09XX XXX XXXX") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = PrimaryGreen)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = phoneError != null,
                        supportingText = {
                            if (phoneError != null) {
                                Text(phoneError!!, color = ExpenseRed)
                            } else {
                                Text("${signUpPhone.length}/11 digits (e.g. 09171234567)", color = MutedGreen)
                            }
                        },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = signUpCategory,
                        onValueChange = { signUpCategory = it },
                        label = { Text("Retail Business Type *") },
                        placeholder = { Text("e.g. Sari-Sari Store, Minimart, Kiosk") },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = signUpPin,
                        onValueChange = { input ->
                            // 4-digit PIN only
                            signUpPin = input.filter { it.isDigit() }.take(4)
                            pinError = null
                        },
                        label = { Text("Create 4-Digit PIN *") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryGreen)
                        },
                        trailingIcon = {
                            val icon = if (isSignUpPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                            val description = if (isSignUpPasswordVisible) "Hide PIN" else "Show PIN"
                            IconButton(onClick = { isSignUpPasswordVisible = !isSignUpPasswordVisible }) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = description,
                                    tint = MutedGreen
                                )
                            }
                        },
                        visualTransformation = if (isSignUpPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        isError = pinError != null,
                        supportingText = {
                            if (pinError != null) {
                                Text(pinError!!, color = ExpenseRed)
                            } else {
                                Text("${signUpPin.length}/4 digits", color = MutedGreen)
                            }
                        },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            // Validation
                            var hasError = false
                            if (signUpStoreName.trim().isEmpty()) {
                                storeNameError = "Store name is required."
                                hasError = true
                            }
                            if (signUpOwnerName.trim().isEmpty()) {
                                ownerNameError = "Owner name is required."
                                hasError = true
                            }
                            val phoneValidation = DomainValidators.validatePhoneNumber(signUpPhone)
                            if (phoneValidation is DomainValidators.ValidationResult.Invalid) {
                                phoneError = phoneValidation.firstErrorMessage
                                hasError = true
                            }
                            if (signUpPin.length < 4) {
                                pinError = "PIN must be exactly 4 digits."
                                hasError = true
                            }

                            if (hasError) return@Button

                            SampleInventoryState.updateUserProfile(
                                storeName = signUpStoreName.trim(),
                                ownerName = signUpOwnerName.trim(),
                                email = "owner@bantaypetsa.ph",
                                phone = signUpPhone.trim(),
                                businessType = signUpCategory.trim().ifEmpty { "Sari-Sari Store" },
                                location = "Philippines"
                            )
                            onAuthSuccess()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Create Store Account",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Text(
            text = "Offline-First Store Protection \u2022 Local Device Storage",
            style = MaterialTheme.typography.bodySmall,
            color = MutedGreen,
            textAlign = TextAlign.Center
        )
    }
}