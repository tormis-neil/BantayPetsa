package com.bantaypetsa.app.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = SageSecondary,
    onPrimaryContainer = ForestGreenDark,
    secondary = ForestGreenLight,
    onSecondary = Color.White,
    secondaryContainer = SageContainer,
    onSecondaryContainer = ForestGreenDark,
    background = BackgroundCream,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,
    error = StatusUrgent,
    onError = Color.White,
    errorContainer = StatusUrgentContainer,
    onErrorContainer = StatusUrgent
)

private val DarkColorScheme = darkColorScheme(
    primary = ForestGreenLight,
    onPrimary = Color.White,
    primaryContainer = ForestGreenDark,
    onPrimaryContainer = SageSecondary,
    secondary = SageSecondary,
    onSecondary = ForestGreenDark,
    secondaryContainer = ForestGreenDark,
    onSecondaryContainer = SageSecondary,
    background = Color(0xFF141915),
    onBackground = Color(0xFFE5EAE6),
    surface = Color(0xFF1B221D),
    onSurface = Color(0xFFE5EAE6),
    surfaceVariant = Color(0xFF252E28),
    onSurfaceVariant = Color(0xFFB5C1B8),
    error = Color(0xFFF97066),
    onError = Color(0xFF55100B),
    errorContainer = Color(0xFF7A1D1A),
    onErrorContainer = Color(0xFFFEE4E2)
)

@Composable
fun BantayPetsaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BantayTypography,
        content = content
    )
}
