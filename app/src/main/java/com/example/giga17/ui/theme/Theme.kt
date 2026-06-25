package com.example.giga17.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGame,
    secondary = TextHighlight,
    tertiary = TextHighlight,
    background = BackgroundDark,
    surface = CardSurface,
    onPrimary = TextLight,
    onSecondary = BackgroundDark,
    onTertiary = TextLight,
    onBackground = TextLight,
    onSurface = TextLight
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGame,
    secondary = TextHighlight,
    tertiary = TextHighlight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = TextLight,
    onSecondary = BackgroundDark,
    onTertiary = TextLight,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun GIGA17Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Disabled dynamic color to force our custom gamified aesthetic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}