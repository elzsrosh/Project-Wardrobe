package com.example.wardrobecomposer.utils

import androidx.compose.ui.graphics.Color
import com.example.wardrobecomposer.model.item.Item

object ColorUtils {

    fun hexToColor(hex: String): Color {
        val strippedHex = hex.replace("#", "")
        val r = Integer.parseInt(strippedHex.substring(0, 2), 16) / 255f
        val g = Integer.parseInt(strippedHex.substring(2, 4), 16) / 255f
        val b = Integer.parseInt(strippedHex.substring(4, 6), 16) / 255f
        return Color(r, g, b)
    }

    fun colorForGroup(group: Item.Color.ColorGroup): Color = when (group) {
        Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> hexToColor("#B0BEC5")
        Item.Color.ColorGroup.ТЁПЛЫЙ -> hexToColor("#FFB300")
        Item.Color.ColorGroup.ХОЛОДНЫЙ -> hexToColor("#64B5F6")
        Item.Color.ColorGroup.ЗЕМЛЯНОЙ -> hexToColor("#8D6E63")
        Item.Color.ColorGroup.ПАСТЕЛЬНЫЙ -> hexToColor("#E8F5E9")
        Item.Color.ColorGroup.ЯРКИЙ -> hexToColor("#FF00FF")
        Item.Color.ColorGroup.КРАСНЫЙ -> hexToColor("#F44336")
        Item.Color.ColorGroup.ОРАНЖЕВЫЙ -> hexToColor("#FF9800")
        Item.Color.ColorGroup.ЖЁЛТЫЙ -> hexToColor("#FFEB3B")
        Item.Color.ColorGroup.ЗЕЛЁНЫЙ -> hexToColor("#4CAF50")
        Item.Color.ColorGroup.СИНИЙ -> hexToColor("#2196F3")
        Item.Color.ColorGroup.ФИОЛЕТОВЫЙ -> hexToColor("#9C27B0")
        Item.Color.ColorGroup.РОЗОВЫЙ -> hexToColor("#E91E63")
        Item.Color.ColorGroup.КОРИЧНЕВЫЙ -> hexToColor("#795548")
        Item.Color.ColorGroup.СЕРЫЙ -> Color(0xFF9E9E9E)
        Item.Color.ColorGroup.ЧЁРНЫЙ -> Color(0xFF000000)
        Item.Color.ColorGroup.БЕЛЫЙ -> Color(0xFFFFFFFF)
        Item.Color.ColorGroup.ЛОСОСЕВЫЙ -> hexToColor("#FF8A65")
        Item.Color.ColorGroup.БОРДОВЫЙ -> hexToColor("#880E4F")
        Item.Color.ColorGroup.ЛАВАНДА -> hexToColor("#CE93D8")
        Item.Color.ColorGroup.МЯТА -> hexToColor("#AED581")
        Item.Color.ColorGroup.ПЕСОЧНЫЙ -> hexToColor("#FFF176")
        Item.Color.ColorGroup.АКВА -> hexToColor("#4DD0E1")
    }

    fun getContrastColor(background: Color): Color {
        val luminance = (0.299 * background.red + 0.587 * background.green + 0.114 * background.blue)
        return if (luminance > 0.5f) Color.Black else Color.White
    }
}