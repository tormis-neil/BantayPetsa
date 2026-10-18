package com.bantaypetsa.app.feature.settings

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreen
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite
import com.bantaypetsa.app.core.util.SampleInventoryState

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    var urgentDaysText by remember { mutableStateOf(SampleInventoryState.urgentThresholdDays.toString()) }
    var approachingDaysText by remember { mutableStateOf(SampleInventoryState.approachingThresholdDays.toString()) }
    var pinLockEnabled by remember { mutableStateOf(false) }

    var successMessage by remember { mutableStateOf<String?>(null) }

    val urgentVal = urgentDaysText.toIntOrNull()
    val approachingVal = approachingDaysText.toIntOrNull()

    val isValid = urgentVal != null && approachingVal != null && urgentVal >= 0 && approachingVal > urgentVal

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftWhite)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp,
                color = CharcoalGreen
            )
            Text(
                text = "Configure warning thresholds and owner security preferences.",
                style = MaterialTheme.typography.bodyMedium,
                color = CharcoalGreen.copy(alpha = 0.7f)
            )
        }

        // Thresholds Card
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PaleGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "Expiry Thresholds (Days)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )
                }

                Text(
                    text = "Configure how many days in advance a batch is classified as Urgent or Approaching.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalGreen.copy(alpha = 0.7f)
                )

                OutlinedTextField(
                    value = urgentDaysText,
                    onValueChange = {
                        urgentDaysText = it
                        successMessage = null
                    },
                    label = { Text("Urgent Threshold (Days, default: 7)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = approachingDaysText,
                    onValueChange = {
                        approachingDaysText = it
                        successMessage = null
                    },
                    label = { Text("Approaching Threshold (Days, default: 30)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                if (!isValid) {
                    Text(
                        text = "Note: Urgent threshold must be non-negative (U >= 0) and Approaching must be greater than Urgent (A > U).",
                        style = MaterialTheme.typography.bodySmall,
                        color = ExpenseRed
                    )
                }

                if (successMessage != null) {
                    Text(
                        text = successMessage!!,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }

                Button(
                    onClick = {
                        if (isValid) {
                            SampleInventoryState.urgentThresholdDays = urgentVal!!
                            SampleInventoryState.approachingThresholdDays = approachingVal!!
                            successMessage = "Thresholds successfully saved!"
                        }
                    },
                    enabled = isValid,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text("Save Thresholds", fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }

        // Owner Access Security Card (FR-01 preview)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PaleGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "Owner Access Protection",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                        Text(
                            text = "Lock with PIN or Biometrics",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = CharcoalGreen
                        )
                        Text(
                            text = "Require local authentication before opening inventory data (Integrated in Phase 5).",
                            style = MaterialTheme.typography.bodySmall,
                            color = CharcoalGreen.copy(alpha = 0.7f)
                        )
                    }

                    Switch(
                        checked = pinLockEnabled,
                        onCheckedChange = { pinLockEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = PrimaryGreen,
                            checkedBorderColor = PrimaryGreen,
                            uncheckedThumbColor = MutedGreen,
                            uncheckedTrackColor = PaleGreen,
                            uncheckedBorderColor = BorderSubtle
                        )
                    )
                }
            }
        }

        // App Information
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = PaleGreen),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "About BantayPetsa",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalGreen
                )
                Text(
                    text = "Version 1.0.0 (MVP) • Offline-First Retail Inventory Assistant",
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalGreen.copy(alpha = 0.7f)
                )
                Text(
                    text = "Bantay sa petsa. Alaga sa paninda.",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryGreen
                )
            }
        }
    }
}
