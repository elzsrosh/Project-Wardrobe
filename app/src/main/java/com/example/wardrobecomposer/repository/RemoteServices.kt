package com.example.wardrobecomposer.data.repository

object RemoteServices {
    suspend fun getPantonePaletteForColor(color: String): List<String> {
        // Здесь будет реальный запрос к Pantone API
        return listOf("PANTONE 13-0647 Illuminating", "PANTONE 17-5104 Ultimate Gray")
    }

    suspend fun generateColorSchemeFromImage(imagePath: String): List<String> {
        // Запрос к Adobe Color API
        return listOf("#FFB6C1", "#FFD700", "#90EE90")
    }

    suspend fun uploadImageToCloud(imagePath: String) {
        // Загрузка в Dropbox или Google Drive
    }
}
