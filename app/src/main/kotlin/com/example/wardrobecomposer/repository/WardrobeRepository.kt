package com.example.wardrobecomposer.repository

import com.example.wardrobecomposer.api.HuggingFaceApiService
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.Look
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WardrobeRepository @Inject constructor(
    private val remoteServices: RemoteServices,
    private val huggingFaceApi: HuggingFaceApiService
) {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: Flow<List<Item>> = _items.asStateFlow()

    suspend fun getAllItems(): List<Item> = _items.value

    suspend fun addItem(item: Item) {
        _items.update { current -> current + item }
    }

    // --- Методы для генерации образов ---
    suspend fun generateLooksFromItem(baseItem: Item): List<Look> {
        return _items.value
            .filter { it.id != baseItem.id && isItemsCompatible(baseItem, it) }
            .map { companionItem -> Look.fromPair(baseItem, companionItem) }
    }

    suspend fun generateLooksByColor(colorGroup: Item.Color.ColorGroup): List<Look> {
        return _items.value
            .filter { it.color.colorGroup == colorGroup }
            .flatMap { baseItem ->
                _items.value
                    .filter { it.id != baseItem.id && it.category != baseItem.category }
                    .map { companionItem -> Look.fromPair(baseItem, companionItem) }
            }
    }

    // --- Метод для генерации палитры цветов ---
    suspend fun generateColorPalette(baseColor: String): List<String> {
        return remoteServices.generateColorPalette(baseColor = baseColor)
    }

    // --- Существующие методы ---
    suspend fun generateOutfitFromExisting(baseItem: Item): List<Look> {
        return _items.value
            .filter { it.id != baseItem.id }
            .filter { isItemsCompatible(baseItem, it) }
            .map { companionItem -> Look.fromPair(baseItem, companionItem) }
    }

    suspend fun generateOutfitFromNewItem(newItem: Item): List<Look> {
        return _items.value
            .filter { isItemsCompatible(newItem, it) }
            .map { companionItem -> Look.fromPair(newItem, companionItem) }
    }

    suspend fun getStyleAdvice(itemName: String): String {
        return huggingFaceApi.getStyleAdvice(itemName)
    }

    // --- Вспомогательная логика ---
    private fun isItemsCompatible(item1: Item, item2: Item): Boolean {
        val colorCompatible = when {
            item1.color.colorGroup == item2.color.colorGroup -> true
            item1.color.colorGroup == Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> true
            item2.color.colorGroup == Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> true
            else -> item1.color.isWarm && item2.color.isWarm || item1.color.isCool && item2.color.isCool
        }

        val styleCompatible = item2.style == item1.style ||
                item2.style == Item.Style.ПОВСЕДНЕВНЫЙ ||
                item1.style == Item.Style.ПОВСЕДНЕВНЫЙ

        val categoryCompatible = when (item1.category) {
            Item.Category.ВЕРХ -> item2.category != Item.Category.ВЕРХ
            Item.Category.НИЗ -> item2.category != Item.Category.НИЗ
            else -> true
        }

        return colorCompatible && styleCompatible && categoryCompatible
    }
}