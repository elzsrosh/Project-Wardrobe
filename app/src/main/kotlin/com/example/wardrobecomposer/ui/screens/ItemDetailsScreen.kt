package com.example.wardrobecomposer.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ItemDetailsScreen(
    itemId: String,
    onBackClick: () -> Unit
) {
    Text("Детали вещи: $itemId")
}