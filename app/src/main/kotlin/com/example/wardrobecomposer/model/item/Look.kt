package com.example.wardrobecomposer.model.item

data class Look(
    val id: String,
    val name: String,
    val items: List<Item>,
    val style: Item.Style,
    val colorScheme: ColorScheme,
    val rating: Float = 0f,
    val lastWornDate: Long? = null,
    val compatibilityReason: String = "",
) {
    data class ColorScheme(
        val primaryColor: Item.Color,
        val secondaryColors: List<Item.Color>,
        val isComplementary: Boolean,
        val isAnalogous: Boolean,
        val isMonochromatic: Boolean,
    )
}
