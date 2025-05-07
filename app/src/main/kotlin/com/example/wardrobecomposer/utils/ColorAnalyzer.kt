package com.example.wardrobecomposer.utils

import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.Look

object ColorAnalyzer {
    fun areColorsHarmonious(
        color1: Item.Color,
        color2: Item.Color,
    ): Boolean =
        when {
            color1.colorGroup == color2.colorGroup -> true
            color1.colorGroup == Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> true
            color2.colorGroup == Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> true
            color1.isWarm && color2.isWarm -> true
            color1.isCool && color2.isCool -> true
            else -> false
        }

    fun analyzeColorScheme(
        color1: Item.Color,
        color2: Item.Color,
    ): Look.ColorScheme {
        val primary = if (color1.colorGroup != Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ) color1 else color2
        val secondary = if (color1 == primary) color2 else color1

        return Look.ColorScheme(
            primaryColor = primary,
            secondaryColors = listOf(secondary),
            isComplementary = areComplementary(primary, secondary),
            isAnalogous = areAnalogous(primary, secondary),
            isMonochromatic = areMonochromatic(primary, secondary),
        )
    }

    private fun areComplementary(
        color1: Item.Color,
        color2: Item.Color,
    ): Boolean =
        when (color1.colorGroup) {
            Item.Color.ColorGroup.ТЁПЛЫЙ -> color2.colorGroup == Item.Color.ColorGroup.ХОЛОДНЫЙ
            Item.Color.ColorGroup.ХОЛОДНЫЙ -> color2.colorGroup == Item.Color.ColorGroup.ТЁПЛЫЙ
            else -> false
        }

    private fun areAnalogous(
        color1: Item.Color,
        color2: Item.Color,
    ): Boolean = color1.colorGroup == color2.colorGroup

    private fun areMonochromatic(
        color1: Item.Color,
        color2: Item.Color,
    ): Boolean =
        color1.colorGroup == color2.colorGroup &&
            color1.hex.substring(0, 4) == color2.hex.substring(0, 4)
}
