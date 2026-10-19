package com.bantaypetsa.app.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = PaleGreen,
    onPrimaryContainer = CharcoalGreen,
    secondary = MutedGreen,
    onSecondary = Color.White,
    secondaryContainer = PaleGreen,
    onSecondaryContainer = CharcoalGreen,
    background = SoftWhite,
    onBackground = CharcoalGreen,
    surface = SurfaceWhite,
    onSurface = CharcoalGreen,
    surfaceVariant = PaleGreen,
    onSurfaceVariant = CharcoalGreen.copy(alpha = 0.7f),
    outline = BorderSubtle,
    error = ExpenseRed,
    onError = Color.White,
    errorContainer = ExpenseRedContainer,
    onErrorContainer = ExpenseRed
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E2D23),
    onPrimaryContainer = PaleGreen,
    secondary = MutedGreen,
    onSecondary = CharcoalGreen,
    secondaryContainer = Color(0xFF1E2D23),
    onSecondaryContainer = PaleGreen,
    background = SoftWhite,
    onBackground = CharcoalGreen,
    surface = SurfaceWhite,
    onSurface = CharcoalGreen,
    surfaceVariant = PaleGreen,
    onSurfaceVariant = CharcoalGreen.copy(alpha = 0.7f),
    outline = BorderSubtle,
    error = ExpenseRed,
    onError = Color.White,
    errorContainer = ExpenseRedContainer,
    onErrorContainer = ExpenseRed
)

@Composable
fun BantayPetsaTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BantayTypography,
        content = content
    )
}
