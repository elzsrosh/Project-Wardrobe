package com.example.wardrobecomposer.repository

import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.Look
import com.example.wardrobecomposer.model.item.StyleTip
import javax.inject.Inject

class WardrobeRepository @Inject constructor(
    private val remoteServices: RemoteServices
) {
    suspend fun getAllItems(): List<Item> = remoteServices.getItems()

    suspend fun getFilteredItems(
        categories: List<Item.Category> = emptyList(),
        colors: List<Item.Color.ColorGroup> = emptyList(),
        styles: List<Item.Style> = emptyList(),
        materials: List<Item.Material> = emptyList()
    ): List<Item> {
        return getAllItems().filter { item ->
            (categories.isEmpty() || item.category in categories) &&
                    (colors.isEmpty() || item.color.colorGroup in colors) &&
                    (styles.isEmpty() || item.style in styles) &&
                    (materials.isEmpty() || item.material in materials)
        }
    }

    suspend fun generateLooks(
        baseItem: Item? = null,
        desiredStyle: Item.Style? = null,
        colorSchemeType: Look.ColorScheme? = null
    ): List<Look> {
        val allItems = getAllItems()
        val baseItems = if (baseItem != null) listOf(baseItem) else allItems

        return baseItems.flatMap { item ->
            val compatibleItems = allItems.filter { other ->
                other.id != item.id && isItemsCompatible(item, other, desiredStyle)
            }
            compatibleItems.map { companionItem ->
                createLookFromItems(item, companionItem, desiredStyle)
            }
        }.distinct()
    }

    private fun isItemsCompatible(
        item1: Item,
        item2: Item,
        desiredStyle: Item.Style?
    ): Boolean {
        val colorCompatible = when {
            item1.color.colorGroup == item2.color.colorGroup -> true
            item1.color.colorGroup == Item.Color.ColorGroup.NEUTRAL -> true
            item2.color.colorGroup == Item.Color.ColorGroup.NEUTRAL -> true
            else -> ColorAnalyzer.areColorsHarmonious(item1.color, item2.color)
        }

        val style = desiredStyle ?: item1.style
        val styleCompatible = item2.style == style ||
                item2.style == Item.Style.CASUAL ||
                item1.style == Item.Style.CASUAL

        val categoryCompatible = when (item1.category) {
            Item.Category.TOP -> item2.category != Item.Category.TOP
            Item.Category.BOTTOM -> item2.category != Item.Category.BOTTOM
            else -> true
        }

        return colorCompatible && styleCompatible && categoryCompatible
    }

    private fun createLookFromItems(
        item1: Item,
        item2: Item,
        desiredStyle: Item.Style?
    ): Look {
        val style = desiredStyle ?: listOf(item1.style, item2.style)
            .firstOrNull { it != Item.Style.CASUAL } ?: Item.Style.CASUAL

        val colorScheme = ColorAnalyzer.analyzeColorScheme(item1.color, item2.color)

        return Look(
            id = "${item1.id}_${item2.id}_${System.currentTimeMillis()}",
            name = "${item1.name} + ${item2.name}",
            items = listOf(item1, item2),
            style = style,
            colorScheme = colorScheme
        )
    }

    suspend fun getStyleTips(): List<StyleTip> = remoteServices.getStyleTips()

    object ColorAnalyzer {
        fun areColorsHarmonious(color1: Item.Color, color2: Item.Color): Boolean {
            return when {
                color1.colorGroup == color2.colorGroup -> true
                color1.colorGroup == Item.Color.ColorGroup.NEUTRAL -> true
                color2.colorGroup == Item.Color.ColorGroup.NEUTRAL -> true
                color1.isWarm && color2.isWarm -> true
                color1.isCool && color2.isCool -> true
                else -> false
            }
        }

        fun analyzeColorScheme(color1: Item.Color, color2: Item.Color): Look.ColorScheme {
            val primary = if (color1.colorGroup != Item.Color.ColorGroup.NEUTRAL) color1 else color2
            val secondary = if (color1 == primary) color2 else color1

            return Look.ColorScheme(
                primaryColor = primary,
                secondaryColors = listOf(secondary),
                isComplementary = areComplementary(primary, secondary),
                isAnalogous = areAnalogous(primary, secondary),
                isMonochromatic = areMonochromatic(primary, secondary)
            )
        }

        private fun areComplementary(color1: Item.Color, color2: Item.Color): Boolean {
            return when (color1.colorGroup) {
                Item.Color.ColorGroup.WARM -> color2.colorGroup == Item.Color.ColorGroup.COOL
                Item.Color.ColorGroup.COOL -> color2.colorGroup == Item.Color.ColorGroup.WARM
                else -> false
            }
        }

        private fun areAnalogous(color1: Item.Color, color2: Item.Color): Boolean {
            return color1.colorGroup == color2.colorGroup
        }

        private fun areMonochromatic(color1: Item.Color, color2: Item.Color): Boolean {
            return color1.colorGroup == color2.colorGroup &&
                    color1.hex.substring(0, 4) == color2.hex.substring(0, 4)
        }
    }
}