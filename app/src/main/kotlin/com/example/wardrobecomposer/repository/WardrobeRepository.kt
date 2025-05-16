package com.example.wardrobecomposer.repository

import com.example.wardrobecomposer.api.ColorApiService
import com.example.wardrobecomposer.api.TheColorApiService
import com.example.wardrobecomposer.api.HuggingFaceApiService
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.repository.RemoteServices
import com.example.wardrobecomposer.model.item.Look
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WardrobeRepository @Inject constructor(
    private val remoteServices: RemoteServices,
    private val huggingFaceApi: HuggingFaceApiService
) {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items = _items.asStateFlow()

    suspend fun getAllItems(): List<Item> = _items.value

    suspend fun addItem(item: Item) {
        _items.update { current -> current + item }
    }

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

    // Генерация палитры через TheColorApiService (генератор)
    suspend fun generateColorPalette(hex: String, mode: String): List<String> {
        return remoteServices.getColorScheme(hex, mode)
    }

    // Получение палитры деталей через ColorApiService (детали)
    suspend fun generateColorPaletteDetails(input: List<Int>): List<String> {
        return remoteServices.generateColorPalette(input)
    }

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

    private fun isItemsCompatible(item1: Item, item2: Item): Boolean {
        val colorCompatible = when {
            item1.color.colorGroup == item2.color.colorGroup -> true
            item1.color.colorGroup == Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> true
            item2.color.colorGroup == Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> true
            else -> item1.color.isWarm && item2.color.isWarm || item1.color.isCool && item2.color.isCool
        }

        val styleCompatible = item1.style == item2.style

        return colorCompatible && styleCompatible
    }
}
