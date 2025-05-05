package com.example.wardrobecomposer.utils

import androidx.compose.ui.graphics.Color
import com.example.wardrobecomposer.model.item.Item

object ColorUtils {
    fun getContrastTextColor(backgroundColor: Color): Color {
        val luminance = (0.299 * backgroundColor.red +
                0.587 * backgroundColor.green +
                0.114 * backgroundColor.blue)
        return if (luminance > 0.5) Color.Black else Color.White
    }

    fun colorForGroup(group: Item.Color.ColorGroup): String {
        return when (group) {
            Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> "#B0BEC5"
            Item.Color.ColorGroup.ТЁПЛЫЙ -> "#FFB300"
            Item.Color.ColorGroup.ХОЛОДНЫЙ -> "#64B5F6"
            Item.Color.ColorGroup.ЗЕМЛЯНОЙ -> "#8D6E63"
            Item.Color.ColorGroup.ПАСТЕЛЬНЫЙ -> "#E8F5E9"
            Item.Color.ColorGroup.ЯРКИЙ -> "#FF00FF"
            Item.Color.ColorGroup.КРАСНЫЙ -> "#F44336"
            Item.Color.ColorGroup.ОРАНЖЕВЫЙ -> "#FF9800"
            Item.Color.ColorGroup.ЖЁЛТЫЙ -> "#FFEB3B"
            Item.Color.ColorGroup.ЗЕЛЁНЫЙ -> "#4CAF50"
            Item.Color.ColorGroup.СИНИЙ -> "#2196F3"
            Item.Color.ColorGroup.ФИОЛЕТОВЫЙ -> "#9C27B0"
            Item.Color.ColorGroup.РОЗОВЫЙ -> "#E91E63"
            Item.Color.ColorGroup.КОРИЧНЕВЫЙ -> "#795548"
            Item.Color.ColorGroup.СЕРЫЙ -> "#9E9E9E"
            Item.Color.ColorGroup.ЧЁРНЫЙ -> "#000000"
            Item.Color.ColorGroup.БЕЛЫЙ -> "#FFFFFF"
            else -> "#000000" // Добавлен else для полноты when
        }
    }
}