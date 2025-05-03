package com.example.wardrobecomposer.repository

import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.StyleTip

object RemoteServices {
    suspend fun getItems(): List<Item> = emptyList()

    suspend fun getPantonePaletteForColor(color: String): List<String> {
        return listOf("PANTONE 13-0647 Illuminating", "PANTONE 17-5104 Ultimate Gray")
    }

    suspend fun generateColorSchemeFromImage(imagePath: String): List<String> {
        return listOf("#FFB6C1", "#FFD700", "#90EE90")
    }

    suspend fun uploadImageToCloud(imagePath: String) {
        // TODO: Implement cloud upload
    }

    suspend fun getStyleTips(): List<StyleTip> = emptyList()
}