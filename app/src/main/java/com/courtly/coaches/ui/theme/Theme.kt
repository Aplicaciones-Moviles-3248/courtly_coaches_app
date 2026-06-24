package com.courtly.coaches.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val CourtlyLightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = DarkNavy,
    secondary = PrimaryDark,
    onSecondary = Color.White,
    background = Background,
    onBackground = TextPrimary,
    surface = Card,
    onSurface = TextPrimary,
    outline = Border,
    error = Color(0xFFFF5252)
)

@Composable
fun CourtlyCoachesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CourtlyLightColorScheme,
        typography = Typography,
        content = content
    )
}

object CourtlyButtonDefaults {
    val Shape = RoundedCornerShape(14.dp)

    @Composable
    fun colors() = ButtonDefaults.buttonColors(
        containerColor = Primary,
        contentColor = DarkNavy,
        disabledContainerColor = Primary.copy(alpha = 0.5f),
        disabledContentColor = DarkNavy.copy(alpha = 0.5f)
    )
}