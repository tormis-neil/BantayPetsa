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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Store
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bantaypetsa.app.core.designsystem.component.ConfirmationDialog
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRedContainer
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreen
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreenContainer
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftWhite)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp,
                color = CharcoalGreen
            )
            Text(
                text = "Store owner identity and account settings.",
                style = MaterialTheme.typography.bodyMedium,
                color = CharcoalGreen.copy(alpha = 0.7f)
            )
        }

        // 1. Identity Banner Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Initials Avatar
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(PaleGreen),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = userProfile.ownerName
                        .split(" ")
                        .mapNotNull { it.firstOrNull()?.toString() }
                        .take(2)
                        .joinToString("")
                        .ifEmpty { "SO" }

                    Text(
                        text = initials,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = PrimaryGreen
                    )
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = userProfile.ownerName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )
                    Text(
                        text = userProfile.storeName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedGreen
                    )

                    // Status Badge
                    if (userProfile.isArchived) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ExpenseRedContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Archived Account",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ExpenseRed
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SuccessGreenContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(12.dp))
                            Text(
                                text = "Active Store Owner",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                    }
                }
            }
        }

        // 2. Detailed Store Information Card (View Mode)
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
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Store Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )

                    OutlinedButton(
                        onClick = { showEditDialog = true },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("Edit", color = PrimaryGreen, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }

                HorizontalDivider(thickness = 0.8.dp, color = BorderSubtle)

                ProfileInfoRow(icon = Icons.Default.Store, label = "Store Name", value = userProfile.storeName)
                ProfileInfoRow(icon = Icons.Default.Person, label = "Owner Full Name", value = userProfile.ownerName)
                ProfileInfoRow(icon = Icons.Default.Business, label = "Business Category", value = userProfile.businessType)
                ProfileInfoRow(icon = Icons.Default.Email, label = "Email Address", value = userProfile.email)
                ProfileInfoRow(icon = Icons.Default.Phone, label = "Contact Phone", value = userProfile.phone)
                ProfileInfoRow(icon = Icons.Default.LocationOn, label = "Store Location", value = userProfile.location)
                ProfileInfoRow(icon = Icons.Default.CheckCircle, label = "Member Since", value = userProfile.memberSince)
            }
        }

        // 3. Account Actions Card (Archive, Reset, Sign Out)
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Account Management",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalGreen
                )

                // Archive / Restore Account
                OutlinedButton(
                    onClick = { showArchiveDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(
                        imageVector = if (userProfile.isArchived) Icons.Default.Restore else Icons.Default.Archive,
                        contentDescription = null,
                        tint = if (userProfile.isArchived) PrimaryGreen else CharcoalGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = if (userProfile.isArchived) "Restore Store Account" else "Archive Store Account",
                        fontWeight = FontWeight.SemiBold,
                        color = if (userProfile.isArchived) PrimaryGreen else CharcoalGreen
                    )
                }

                // Reset Account Data
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

    // Edit Profile Dialog
    if (showEditDialog) {
        var editStoreName by remember { mutableStateOf(userProfile.storeName) }
        var editOwnerName by remember { mutableStateOf(userProfile.ownerName) }
        var editBusinessType by remember { mutableStateOf(userProfile.businessType) }
        var editEmail by remember { mutableStateOf(userProfile.email) }
        var editPhone by remember { mutableStateOf(userProfile.phone) }
        var editLocation by remember { mutableStateOf(userProfile.location) }

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
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editOwnerName,
                        onValueChange = { editOwnerName = it },
                        label = { Text("Owner Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editBusinessType,
                        onValueChange = { editBusinessType = it },
                        label = { Text("Business Category") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Contact Phone") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editLocation,
                        onValueChange = { editLocation = it },
                        label = { Text("Location") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
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
