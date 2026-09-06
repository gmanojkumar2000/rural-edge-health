package org.ruraledgehealth.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Keeping app colors in one place prevents screens from inventing inconsistent styles.
private val RuralEdgeHealthColors = lightColorScheme(
    primary = Color(0xFF256D4A),
    onPrimary = Color.White,
    secondary = Color(0xFF4E6356),
    background = Color(0xFFF7FBF8),
    surface = Color(0xFFF7FBF8),
)

@Composable
fun RuralEdgeHealthTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RuralEdgeHealthColors,
        content = content,
    )
}
