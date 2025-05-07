package com.example.wardrobecomposer.repository

import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.Look
import com.example.wardrobecomposer.utils.ColorAnalyzer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WardrobeRepository
    @Inject
    constructor() {
        private val _items = MutableStateFlow<List<Item>>(emptyList())
        val items: Flow<List<Item>> = _items.asStateFlow()

        private val _looks = MutableStateFlow<List<Look>>(emptyList())
        val looks: Flow<List<Look>> = _looks.asStateFlow()

        suspend fun getAllItems(): List<Item> = _items.value

        suspend fun addItem(item: Item) {
            _items.update { current -> current + item }
        }

        suspend fun generateLooks(
            baseItem: Item? = null,
            colorScheme: Look.ColorScheme? = null,
        ): List<Look> {
            val allItems = _items.value
            return when {
                baseItem != null -> {
                    allItems
                        .filter { it.id != baseItem.id }
                        .filter { isItemsCompatible(baseItem, it) }
                        .map { companionItem ->
                            createLookFromItems(baseItem, companionItem)
                        }
                }
                colorScheme != null -> {
                    allItems
                        .filter { item ->
                            ColorAnalyzer.areColorsHarmonious(item.color, colorScheme.primaryColor)
                        }.chunked(2)
                        .map { items ->
                            createLookFromItems(items[0], items.getOrElse(1) { items[0] })
                        }
                }
                else -> emptyList()
            }
        }

        suspend fun generateOutfitFromExisting(baseItem: Item): List<Look> = generateLooks(baseItem = baseItem)

        suspend fun generateOutfitFromNewItem(newItem: Item): List<Look> = generateLooks(baseItem = newItem)

        private fun isItemsCompatible(
            item1: Item,
            item2: Item,
        ): Boolean {
            val colorCompatible =
                when {
                    item1.color.colorGroup == item2.color.colorGroup -> true
                    item1.color.colorGroup == Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> true
                    item2.color.colorGroup == Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> true
                    else -> ColorAnalyzer.areColorsHarmonious(item1.color, item2.color)
                }

            val styleCompatible =
                item2.style == item1.style ||
                    item2.style == Item.Style.ПОВСЕДНЕВНЫЙ ||
                    item1.style == Item.Style.ПОВСЕДНЕВНЫЙ

            val categoryCompatible =
                when (item1.category) {
                    Item.Category.ВЕРХ -> item2.category != Item.Category.ВЕРХ
                    Item.Category.НИЗ -> item2.category != Item.Category.НИЗ
                    else -> true
                }

            return colorCompatible && styleCompatible && categoryCompatible
        }

        private fun createLookFromItems(
            item1: Item,
            item2: Item,
        ): Look {
            val style =
                listOf(item1.style, item2.style)
                    .firstOrNull { it != Item.Style.ПОВСЕДНЕВНЫЙ } ?: Item.Style.ПОВСЕДНЕВНЫЙ

            val colorScheme = ColorAnalyzer.analyzeColorScheme(item1.color, item2.color)

            return Look(
                id = "${item1.id}_${item2.id}_${System.currentTimeMillis()}",
                name = "${item1.name} + ${item2.name}",
                items = listOf(item1, item2),
                style = style,
                colorScheme = colorScheme,
                compatibilityReason = getCompatibilityReason(item1, item2),
            )
        }

        private fun getCompatibilityReason(
            item1: Item,
            item2: Item,
        ): String =
            buildString {
                append("Совместимость: ")
                if (item1.color.colorGroup == item2.color.colorGroup) {
                    append("цветовая гамма, ")
                }
                if (item1.style == item2.style) {
                    append("стиль")
                }
            }.removeSuffix(", ")
    }
