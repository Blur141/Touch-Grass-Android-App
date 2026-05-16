package com.touchgrass.ui.theme

import android.app.Activity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GrassGreen,
    onPrimary = Color(0xFF052010),
    primaryContainer = Color(0xFF0F2218),
    onPrimaryContainer = NeonGreen,
    secondary = AccentBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF0F1A2E),
    onSecondaryContainer = AccentBlue,
    tertiary = AccentPurple,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = Color(0xFFFFFFFF),
    surface = DarkSurface,
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = DarkCardColor,
    onSurfaceVariant = Color(0xFFB4B4CC),
    outline = DarkTextMutedColor,
    outlineVariant = DarkDividerColor,
    error = AccentRed,
    onError = Color.White,
    inverseSurface = Color(0xFFE8E8F0),
    inverseOnSurface = DarkBackground,
)

private val LightColorScheme = lightColorScheme(
    primary = DeepGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCCF0D8),
    onPrimaryContainer = Color(0xFF002111),
    secondary = AccentBlue,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCard,
    onSurfaceVariant = LightTextSecondary,
    outline = LightTextMuted,
    outlineVariant = LightDivider,
    error = AccentRed,
    onError = Color.White,
)

@Composable
fun TouchGrassTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

// Theme-reactive aliases — use these everywhere instead of hardcoded dark colors.
// They read from MaterialTheme so they automatically adapt to light/dark mode.

val TextPrimary: Color
    @Composable get() = MaterialTheme.colorScheme.onBackground

val TextSecondary: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

val TextTertiary: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

val TextMuted: Color
    @Composable get() = MaterialTheme.colorScheme.outline

val DarkCard: Color
    @Composable get() = MaterialTheme.colorScheme.surfaceVariant

val DarkCardElevated: Color
    @Composable get() = MaterialTheme.colorScheme.surface

val DarkDivider: Color
    @Composable get() = MaterialTheme.colorScheme.outlineVariant
