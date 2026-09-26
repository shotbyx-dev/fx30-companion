package com.fx30companion.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val FxOrange = Color(0xFFFF6A00)
val FxAmber = Color(0xFFFFB300)
val FxGreen = Color(0xFF2ECC40)
val FxRed = Color(0xFFFF4136)
val FxBlue = Color(0xFF40C4FF)

private val DarkColors = darkColorScheme(
    primary = FxOrange,
    onPrimary = Color.Black,
    secondary = FxAmber,
    tertiary = FxBlue,
    background = Color(0xFF0E0E10),
    surface = Color(0xFF17171A),
    surfaceVariant = Color(0xFF232327),
    onBackground = Color(0xFFF2F2F2),
    onSurface = Color(0xFFF2F2F2),
    onSurfaceVariant = Color(0xFFB9B9C0),
    error = FxRed
)

private val AppTypography = Typography(
    headlineSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 17.sp),
    titleSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
    bodyMedium = TextStyle(fontSize = 14.5.sp, lineHeight = 21.sp),
    bodySmall = TextStyle(fontSize = 13.sp, lineHeight = 19.sp),
    labelSmall = TextStyle(fontSize = 11.5.sp, color = Color(0xFFB9B9C0))
)

@Composable
fun Fx30CompanionTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = AppTypography,
        content = content
    )
}
