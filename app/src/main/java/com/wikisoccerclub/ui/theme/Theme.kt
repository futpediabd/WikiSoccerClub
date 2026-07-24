package com.wikisoccerclub.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val WscColorScheme = lightColorScheme(
    primary = WscGreenDark,
    onPrimary = WscWhite,
    primaryContainer = WscGreenCard,
    onPrimaryContainer = WscWhite,
    secondary = WscYellow,
    onSecondary = WscBlueDark,
    secondaryContainer = WscYellow,
    onSecondaryContainer = WscBlueDark,
    tertiary = WscBlue,
    onTertiary = WscWhite,
    background = WscGreen,
    onBackground = WscWhite,
    surface = WscWhite,
    onSurface = WscBlueDark,
    surfaceVariant = WscSurfaceSoft,
    onSurfaceVariant = WscTextMuted,
    outline = WscOutline,
    error = WscRed,
    onError = WscWhite
)

private val WscShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun WikiSoccerClubTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WscColorScheme,
        typography = WscTypography,
        shapes = WscShapes,
        content = content
    )
}
