package com.example.wardrobecomposer.data.repository

import com.example.wardrobecomposer.data.model.Item
import com.example.wardrobecomposer.data.model.Look
import com.example.wardrobecomposer.data.model.StyleTip

object WardrobeRepository {

    suspend fun generateLookByFilters(color: String, style: String, material: String): Look {
        val items = listOf(
            Item("Пальто", color, style, material),
            Item("Шарф", color, style, "шерсть"),
            Item("Ботинки", "коричневый", style, "кожа")
        )
        val colorScheme = RemoteServices.getPantonePaletteForColor(color)
        val advice = getStyleTip(style)

        return Look(items, colorScheme, advice)
    }

    private fun getStyleTip(style: String): String {
        val tips = listOf(
            StyleTip("кэжуал", "Добавьте аксессуары, чтобы завершить образ."),
            StyleTip("офисный", "Выбирайте нейтральные оттенки."),
            StyleTip("спортивный", "Комфорт — главный критерий.")
        )
        return tips.find { it.style.equals(style, ignoreCase = true) }?.tip
            ?: "Будьте уверены в своём стиле!"
    }
}
