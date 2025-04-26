package com.example.wardrobecomposer

object RemoteServices {
    fun getPantonePaletteForColor(color: String): List<String> {
        // TODO: сделать запрос к Pantone API
        return emptyList()
    }

    fun generateColorSchemeFromImage(imagePath: String): List<String> {
        // TODO: сделать запрос к Adobe Color API
        return emptyList()
    }

    fun uploadImageToCloud(imagePath: String) {
        // TODO: сделать отправку в Dropbox / Google Drive
    }
}
