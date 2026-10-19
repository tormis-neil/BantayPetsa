package com.bantaypetsa.app.core.designsystem.component

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite

/**
 * Standard high-contrast color styling for all OutlinedTextFields in BantayPetsa.
 * Guarantees crisp readability of entered text across both Light and Dark system settings.
 */
@Composable
fun bantayTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = CharcoalGreen,
        unfocusedTextColor = CharcoalGreen,
        focusedContainerColor = SurfaceWhite,
        unfocusedContainerColor = SurfaceWhite,
        disabledContainerColor = SurfaceWhite.copy(alpha = 0.5f),
        errorContainerColor = SurfaceWhite,
        cursorColor = PrimaryGreen,
        errorCursorColor = ExpenseRed,
        focusedBorderColor = PrimaryGreen,
        unfocusedBorderColor = BorderSubtle,
        errorBorderColor = ExpenseRed,
        focusedLabelColor = PrimaryGreen,
        unfocusedLabelColor = CharcoalGreen.copy(alpha = 0.7f),
        errorLabelColor = ExpenseRed,
        focusedPlaceholderColor = CharcoalGreen.copy(alpha = 0.4f),
        unfocusedPlaceholderColor = CharcoalGreen.copy(alpha = 0.4f),
        focusedLeadingIconColor = PrimaryGreen,
        unfocusedLeadingIconColor = PrimaryGreen,
        focusedTrailingIconColor = PrimaryGreen,
        unfocusedTrailingIconColor = MutedGreen
    )
}
