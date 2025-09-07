package ru.practicum.android.diploma.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    background = NeutralDark,
    onBackground = NeutralLight,
    surface = NeutralDark,
    onSurface = NeutralLight,
    surfaceVariant = Gray,
    onSurfaceVariant = NeutralLight,
    secondary = Blue,
    onSecondary = Color.White,
    outline = LightGray,
    surfaceTint = Blue,
)

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    background = NeutralLight,
    onBackground = NeutralDark,
    surface = NeutralLight,
    onSurface = NeutralDark,
    surfaceVariant = LightGray,
    onSurfaceVariant = Gray,
    secondary = Blue,
    onSecondary = Color.White,
    outline = LightGray,
    surfaceTint = Blue,
)

@Composable
fun DiplomaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
