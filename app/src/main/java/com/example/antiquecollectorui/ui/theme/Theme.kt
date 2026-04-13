package com.example.antiquecollectorui.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6D4C41),       // warm brown
    onPrimary = Color.White,
    secondary = Color(0xFFBCAAA4),
    background = Color(0xFFFDF6E3),    // parchment-like
    surface = Color(0xFFFFF8F0),
    onBackground = Color(0xFF3E2723),
    onSurface = Color(0xFF3E2723)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFBCAAA4),
    onPrimary = Color(0xFF3E2723),
    secondary = Color(0xFF6D4C41),
    background = Color(0xFF1C1209),
    surface = Color(0xFF2C1A0E),
    onBackground = Color(0xFFEDE0D4),
    onSurface = Color(0xFFEDE0D4)
)

@Composable
fun AntiqueCollectorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
