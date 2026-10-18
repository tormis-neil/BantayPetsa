package com.bantaypetsa.app.core.designsystem.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmLabel: String = "Confirm",
    dismissLabel: String = "Cancel",
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        title = { Text(text = title, style = MaterialTheme.typography.titleMedium, color = CharcoalGreen) },
        text = { Text(text = message, style = MaterialTheme.typography.bodyMedium, color = CharcoalGreen.copy(alpha = 0.75f)) },
        shape = RoundedCornerShape(20.dp),
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp),
                colors = if (isDestructive) {
                    ButtonDefaults.buttonColors(containerColor = ExpenseRed)
                } else {
                    ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                }
            ) {
                Text(confirmLabel, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissLabel, color = CharcoalGreen)
            }
        },
        modifier = modifier
    )
}
