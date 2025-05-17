package com.example.wardrobecomposer.utils

import androidx.compose.ui.graphics.Color
import com.example.wardrobecomposer.model.item.Item

object ColorUtils {

    val colorNameMap = mapOf(
        "#B0BEC5" to "Нейтральный",
        "#FFB300" to "Тёплый",
        "#64B5F6" to "Холодный",
        "#8D6E63" to "Земляной",
        "#E8F5E9" to "Пастельный",
        "#FF00FF" to "Яркий",
        "#F44336" to "Красный",
        "#FF9800" to "Оранжевый",
        "#FFEB3B" to "Жёлтый",
        "#4CAF50" to "Зелёный",
        "#2196F3" to "Синий",
        "#9C27B0" to "Фиолетовый",
        "#E91E63" to "Розовый",
        "#795548" to "Коричневый",
        "#9E9E9E" to "Серый",
        "#000000" to "Чёрный",
        "#FFFFFF" to "Белый",
        "#FF8A65" to "Лососевый",
        "#880E4F" to "Бордовый",
        "#CE93D8" to "Лаванда",
        "#AED581" to "Мята",
        "#FFF176" to "Песочный",
        "#4DD0E1" to "Аква",
        "#FFD700" to "Золотой",
        "#C0C0C0" to "Серебряный"
    )

    fun hexToColor(hex: String): Color {
        val strippedHex = hex.replace("#", "")
        val r = Integer.parseInt(strippedHex.substring(0, 2), 16) / 255f
        val g = Integer.parseInt(strippedHex.substring(2, 4), 16) / 255f
        val b = Integer.parseInt(strippedHex.substring(4, 6), 16) / 255f
        return Color(r, g, b)
    }

    fun colorForGroup(group: Item.Color.ColorGroup): Color =
        when (group) {
            Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> Color(0xFFB0BEC5)
            Item.Color.ColorGroup.ТЁПЛЫЙ -> Color(0xFFFFB300)
            Item.Color.ColorGroup.ХОЛОДНЫЙ -> Color(0xFF64B5F6)
            Item.Color.ColorGroup.ЗЕМЛЯНОЙ -> Color(0xFF8D6E63)
            Item.Color.ColorGroup.ПАСТЕЛЬНЫЙ -> Color(0xFFE8F5E9)
            Item.Color.ColorGroup.ЯРКИЙ -> Color(0xFFFF00FF)
            Item.Color.ColorGroup.КРАСНЫЙ -> Color(0xFFF44336)
            Item.Color.ColorGroup.ОРАНЖЕВЫЙ -> Color(0xFFFF9800)
            Item.Color.ColorGroup.ЖЁЛТЫЙ -> Color(0xFFFFEB3B)
            Item.Color.ColorGroup.ЗЕЛЁНЫЙ -> Color(0xFF4CAF50)
            Item.Color.ColorGroup.СИНИЙ -> Color(0xFF2196F3)
            Item.Color.ColorGroup.ФИОЛЕТОВЫЙ -> Color(0xFF9C27B0)
            Item.Color.ColorGroup.РОЗОВЫЙ -> Color(0xFFE91E63)
            Item.Color.ColorGroup.КОРИЧНЕВЫЙ -> Color(0xFF795548)
            Item.Color.ColorGroup.СЕРЫЙ -> Color(0xFF9E9E9E)
            Item.Color.ColorGroup.ЧЁРНЫЙ -> Color(0xFF000000)
            Item.Color.ColorGroup.БЕЛЫЙ -> Color(0xFFFFFFFF)
            Item.Color.ColorGroup.ЛАВАНДА -> Color(0xFFCE93D8)
            Item.Color.ColorGroup.МЯТА -> Color(0xFFAED581)
            Item.Color.ColorGroup.АКВА -> Color(0xFF4DD0E1)
            Item.Color.ColorGroup.ЛОСОСЕВЫЙ -> Color(0xFFFF8A65)
            Item.Color.ColorGroup.БОРДОВЫЙ -> Color(0xFF880E4F)
            Item.Color.ColorGroup.ПЕСОЧНЫЙ -> Color(0xFFFFF176)
            Item.Color.ColorGroup.ЗОЛОТОЙ -> Color(0xFFFFD700)
            Item.Color.ColorGroup.СЕРЕБРЯНЫЙ -> Color(0xFFC0C0C0)
        }

    fun getContrastColor(background: Color): Color {
        val luminance = (0.299 * background.red + 0.587 * background.green + 0.114 * background.blue)
        return if (luminance > 0.5f) Color.Black else Color.White
    }

    fun colorToHex(color: Color): String {
        val r = (color.red * 255).toInt().toString(16).padStart(2, '0')
        val g = (color.green * 255).toInt().toString(16).padStart(2, '0')
        val b = (color.blue * 255).toInt().toString(16).padStart(2, '0')
        return "#$r$g$b"
    }

    fun rgbToHex(r: Int, g: Int, b: Int): String {
        return String.format("#%02X%02X%02X", r, g, b)
    }

    fun hexToRgb(hex: String): List<Int> {
        val color = android.graphics.Color.parseColor(hex)
        return listOf(
            android.graphics.Color.red(color),
            android.graphics.Color.green(color),
            android.graphics.Color.blue(color)
        )
    }

    fun getColorNameFromColor(color: Color): String {
        val hex = colorToHex(color).uppercase()
        return colorNameMap[hex] ?: "Неизвестный"
    }
}
