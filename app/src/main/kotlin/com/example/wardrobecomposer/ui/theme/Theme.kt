package com.example.wardrobecomposer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2196F3),
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black
)

@Composable
fun WardrobeComposerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}