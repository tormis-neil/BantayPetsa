package com.bantaypetsa.app.feature.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bantaypetsa.app.core.designsystem.component.ConfirmationDialog
import com.bantaypetsa.app.core.designsystem.component.bantayTextFieldColors
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite
import com.bantaypetsa.app.domain.engine.DomainValidators

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userProfile = uiState.userProfile

    var showEditDialog by remember { mutableStateOf(false) }
    var showArchiveDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = SoftWhite
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
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Store Profile",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp,
                    color = CharcoalGreen
                )
                Text(
                    text = "Manage your store identity and owner credentials.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalGreen.copy(alpha = 0.7f)
                )
            }

            // Profile Header Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(PaleGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = userProfile.storeName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalGreen
                        )
                        Text(
                            text = userProfile.businessType,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MutedGreen
                        )
                        if (userProfile.isArchived) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "STORE ACCOUNT ARCHIVED",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ExpenseRed,
                                modifier = Modifier
                                    .background(PaleGreen, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Button(
                        onClick = { showEditDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Edit Store Profile", fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }

            // Profile Details Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Owner & Store Info",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )

                    ProfileInfoRow(icon = Icons.Default.Person, label = "Store Owner", value = userProfile.ownerName)
                    HorizontalDivider(thickness = 0.5.dp, color = BorderSubtle)
                    ProfileInfoRow(icon = Icons.Default.Phone, label = "Contact Phone", value = userProfile.phone)
                    HorizontalDivider(thickness = 0.5.dp, color = BorderSubtle)
                    ProfileInfoRow(icon = Icons.Default.Email, label = "Email Address", value = userProfile.email)
                    HorizontalDivider(thickness = 0.5.dp, color = BorderSubtle)
                    ProfileInfoRow(icon = Icons.Default.LocationOn, label = "Location", value = userProfile.location)
                    HorizontalDivider(thickness = 0.5.dp, color = BorderSubtle)
                    ProfileInfoRow(icon = Icons.Default.Business, label = "Store Category", value = userProfile.businessType)
                }
            }

            // Account Actions Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Store Maintenance Actions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )

                    // Archive / Restore
                    OutlinedButton(
                        onClick = { showArchiveDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(
                            imageVector = if (userProfile.isArchived) Icons.Default.Unarchive else Icons.Default.Archive,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = if (userProfile.isArchived) "Restore Store Account" else "Archive Store Account",
                            fontWeight = FontWeight.SemiBold,
                            color = CharcoalGreen
                        )
                    }

                    // Reset Profile
                    OutlinedButton(
                        onClick = { showResetDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null, tint = ExpenseRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Reset Profile to Sample Defaults", fontWeight = FontWeight.SemiBold, color = ExpenseRed)
                    }

                    HorizontalDivider(thickness = 0.8.dp, color = BorderSubtle)

                    // Sign Out
                    Button(
                        onClick = { showSignOutDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ExpenseRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Sign Out", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditDialog) {
        var editStoreName by remember(userProfile) { mutableStateOf(userProfile.storeName) }
        var editOwnerName by remember(userProfile) { mutableStateOf(userProfile.ownerName) }
        var editBusinessType by remember(userProfile) { mutableStateOf(userProfile.businessType) }
        var editEmail by remember(userProfile) { mutableStateOf(userProfile.email) }
        var editPhone by remember(userProfile) { mutableStateOf(userProfile.phone.filter { it.isDigit() }.take(11)) }
        var editLocation by remember(userProfile) { mutableStateOf(userProfile.location) }
        var editPhoneError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    text = "Edit Store Profile",
                    fontWeight = FontWeight.Bold,
                    color = CharcoalGreen
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = editStoreName,
                        onValueChange = { editStoreName = it },
                        label = { Text("Store Name") },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editOwnerName,
                        onValueChange = { editOwnerName = it },
                        label = { Text("Owner Full Name") },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editBusinessType,
                        onValueChange = { editBusinessType = it },
                        label = { Text("Business Category") },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email Address") },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { input ->
                            // Digits only, max 11 numbers
                            editPhone = input.filter { it.isDigit() }.take(11)
                            editPhoneError = null
                        },
                        label = { Text("Contact Phone") },
                        placeholder = { Text("09XX XXX XXXX") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = editPhoneError != null,
                        supportingText = {
                            if (editPhoneError != null) {
                                Text(editPhoneError!!, color = ExpenseRed)
                            } else {
                                Text("${editPhone.length}/11 digits", color = MutedGreen)
                            }
                        },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editLocation,
                        onValueChange = { editLocation = it },
                        label = { Text("Location") },
                        colors = bantayTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val phoneVal = DomainValidators.validatePhoneNumber(editPhone)
                        if (phoneVal is DomainValidators.ValidationResult.Invalid) {
                            editPhoneError = phoneVal.firstErrorMessage
                            return@Button
                        }

                        viewModel.updateUserProfile(
                            storeName = editStoreName.trim(),
                            ownerName = editOwnerName.trim(),
                            email = editEmail.trim(),
                            phone = editPhone.trim(),
                            businessType = editBusinessType.trim(),
                            location = editLocation.trim()
                        )
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Save Changes", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = CharcoalGreen)
                }
            }
        )
    }

    // Archive Profile Confirmation
    if (showArchiveDialog) {
        ConfirmationDialog(
            title = if (userProfile.isArchived) "Restore Store Account?" else "Archive Store Account?",
            message = if (userProfile.isArchived) {
                "The store profile '${userProfile.storeName}' will be restored to active status."
            } else {
                "The store profile '${userProfile.storeName}' will be marked as archived. Store stock records will remain intact."
            },
            confirmLabel = if (userProfile.isArchived) "Restore" else "Archive",
            isDestructive = !userProfile.isArchived,
            onConfirm = {
                viewModel.toggleArchive()
                showArchiveDialog = false
            },
            onDismiss = { showArchiveDialog = false }
        )
    }

    // Reset Account Confirmation
    if (showResetDialog) {
        ConfirmationDialog(
            title = "Reset Profile Data?",
            message = "This will reset the store profile information back to the initial sample defaults.",
            confirmLabel = "Reset",
            isDestructive = true,
            onConfirm = {
                viewModel.resetProfile()
                showResetDialog = false
            },
            onDismiss = { showResetDialog = false }
        )
    }

    // Sign Out Confirmation
    if (showSignOutDialog) {
        ConfirmationDialog(
            title = "Sign Out?",
            message = "Are you sure you want to sign out from ${userProfile.storeName}? You can sign back in anytime.",
            confirmLabel = "Sign Out",
            isDestructive = true,
            onConfirm = {
                showSignOutDialog = false
                onSignOut()
            },
            onDismiss = { showSignOutDialog = false }
        )
    }
}

@Composable
fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MutedGreen
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = CharcoalGreen
            )
        }
    }
}