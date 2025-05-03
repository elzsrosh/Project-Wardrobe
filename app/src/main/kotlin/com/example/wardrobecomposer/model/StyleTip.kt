package com.example.wardrobecomposer.model.item

data class StyleTip(
    val id: String,
    val title: String,
    val description: String,
    val relatedStyles: List<Item.Style>,
    val relatedColors: List<Item.Color.ColorGroup>,
    val priority: Int
)