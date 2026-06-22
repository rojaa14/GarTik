package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.data.ThemeMode

val LocalThemeMode = staticCompositionLocalOf { ThemeMode.LIGHT }

private val DarkColorScheme = darkColorScheme(
    primary = GarTikPrimary,
    secondary = GarTikSecondary,
    tertiary = GarTikSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    primaryContainer = DarkPrimaryContainer,
    secondaryContainer = DarkSecondaryContainer,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = GarTikPrimary,
    secondary = GarTikSecondary,
    tertiary = GarTikTertiary,
    background = LightBackground,
    surface = LightSurface,
    primaryContainer = LightPrimaryContainer,
    secondaryContainer = LightSecondaryContainer,
    onPrimary = Color.White,
    onSecondary = Color(0xFF0F172A),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A)
)

@Composable
fun MyApplicationTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isSystemDark = isSystemInDarkTheme()

    val colorScheme = when (themeMode) {
        ThemeMode.LIGHT -> LightColorScheme
        ThemeMode.DARK -> DarkColorScheme
        ThemeMode.MATERIAL_YOU -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                dynamicLightColorScheme(context)
            } else {
                // Elegant fallback lilac-indigo theme
                lightColorScheme(
                    primary = Color(0xFF6366F1), // Indigo
                    secondary = Color(0xFF8B5CF6), // Violet
                    tertiary = Color(0xFFEC4899), // Pink
                    background = Color(0xFFF5F3FF),
                    surface = Color.White,
                    primaryContainer = Color(0xFFEDE9FE),
                    secondaryContainer = Color(0xFFF5F3FF),
                    onPrimary = Color.White,
                    onBackground = Color(0xFF1E1B4B),
                    onSurface = Color(0xFF1E1B4B)
                )
            }
        }
        ThemeMode.FROSTED_GLASS -> {
            // Elegant Cosmic dark schema with rose/cyan tinting accents
            darkColorScheme(
                primary = Color(0xFFFE2C55),
                secondary = Color(0xFF00ADB5),
                tertiary = Color(0xFF38BDF8),
                background = Color(0xFF0B0F19),
                surface = Color(0x22FFFFFF), // highly transparent for glass effect
                onPrimary = Color.White,
                onBackground = Color.White,
                onSurface = Color.White
            )
        }
        ThemeMode.GRADIENT_GLASS -> {
            // Intense energy schema
            darkColorScheme(
                primary = Color(0xFFEC4899),
                secondary = Color(0xFFF43F5E),
                tertiary = Color(0xFFF59E0B),
                background = Color(0xFF0F051D),
                surface = Color(0x1AFFFFFF),
                onPrimary = Color.White,
                onBackground = Color.White,
                onSurface = Color.White
            )
        }
        ThemeMode.LIQUID_GLASS -> {
            // Cool water cyan/aquamarine vibe
            darkColorScheme(
                primary = Color(0xFF10B981),
                secondary = Color(0xFF00ADB5),
                tertiary = Color(0xFF3B82F6),
                background = Color(0xFF040D21),
                surface = Color(0x2200ADB5),
                onPrimary = Color.White,
                onBackground = Color.White,
                onSurface = Color.White
            )
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val isDarkText = when (themeMode) {
                ThemeMode.LIGHT, ThemeMode.MATERIAL_YOU -> true
                else -> false
            }
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDarkText
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = isDarkText
        }
    }

    CompositionLocalProvider(LocalThemeMode provides themeMode) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
