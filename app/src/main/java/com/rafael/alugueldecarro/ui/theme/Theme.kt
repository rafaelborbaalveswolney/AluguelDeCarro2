package com.rafael.alugueldecarro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(

    primary = Color(0xFF1565C0),
    onPrimary = Color.White,

    primaryContainer = Color(0xFFD9E9FF),
    onPrimaryContainer = Color(0xFF0D3A67),

    secondary = Color(0xFF1976D2),
    onSecondary = Color.White,

    secondaryContainer = Color(0xFFE3F2FD),
    onSecondaryContainer = Color(0xFF123A5C),

    background = Color(0xFFF6F8FB),
    onBackground = Color(0xFF1A1C1E),

    surface = Color.White,
    onSurface = Color(0xFF1A1C1E),

    surfaceVariant = Color(0xFFEEF2F6),
    onSurfaceVariant = Color(0xFF50555A),

    error = Color(0xFFC62828),
    onError = Color.White
)

private val DarkColors = darkColorScheme(

    primary = Color(0xFF64B5F6),
    onPrimary = Color(0xFF002F51),

    primaryContainer = Color(0xFF164C73),
    onPrimaryContainer = Color(0xFFD4EAFF),

    secondary = Color(0xFF90CAF9),
    onSecondary = Color(0xFF00344F),

    secondaryContainer = Color(0xFF233E52),
    onSecondaryContainer = Color(0xFFD1E9F8),

    background = Color(0xFF101214),
    onBackground = Color(0xFFE3E3E3),

    surface = Color(0xFF1C1E20),
    onSurface = Color(0xFFF1F1F1),

    surfaceVariant = Color(0xFF292C2F),
    onSurfaceVariant = Color(0xFFC5C8CC),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun AluguelDeCarroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors =
        if (darkTheme) {
            DarkColors
        } else {
            LightColors
        }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}