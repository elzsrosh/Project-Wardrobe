package com.example.wardrobecomposer.model.item

data class Item(
    val id: String,
    val name: String,
    val category: Category,
    val color: Color,
    val style: Style,
    val material: Material,
    val imageUri: String,
    val isFavorite: Boolean = false,
    val lastWornDate: Long? = null
) {
    enum class Category { TOP, BOTTOM, DRESS, OUTERWEAR, SHOES, ACCESSORY }
    enum class Style { CASUAL, FORMAL, SPORT, BUSINESS, EVENING, STREET }
    enum class Material { COTTON, WOOL, SILK, DENIM, LEATHER, SYNTHETIC }

    data class Color(
        val hex: String,
        val colorGroup: ColorGroup
    ) {
        enum class ColorGroup { NEUTRAL, WARM, COOL, EARTH, PASTEL, BRIGHT }
        val isWarm: Boolean get() = colorGroup == ColorGroup.WARM || colorGroup == ColorGroup.EARTH
        val isCool: Boolean get() = colorGroup == ColorGroup.COOL || colorGroup == ColorGroup.BRIGHT
    }
}