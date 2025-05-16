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

    companion object {
        fun fromPair(
            item1: Item,
            item2: Item,
        ): Look {
            val style =
                listOf(item1.style, item2.style)
                    .firstOrNull { it != Item.Style.ПОВСЕДНЕВНЫЙ } ?: Item.Style.ПОВСЕДНЕВНЫЙ

            val colorScheme = analyzeColorScheme(item1, item2)

            return Look(
                id = "${item1.id}_${item2.id}_${System.currentTimeMillis()}",
                name = "${item1.name} + ${item2.name}",
                items = listOf(item1, item2),
                style = style,
                colorScheme = colorScheme,
                compatibilityReason = getCompatibilityReason(item1, item2),
            )
        }

        private fun analyzeColorScheme(
            item1: Item,
            item2: Item,
        ): ColorScheme {
            val primary = if (item1.color.colorGroup != Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ) item1.color else item2.color
            val secondary = if (primary == item1.color) item2.color else item1.color

            val isComplementary = areColorsHarmonious(item1, item2)
            val isAnalogous = item1.color.isWarm && item2.color.isWarm || item1.color.isCool && item2.color.isCool
            val isMonochromatic = item1.color.colorGroup == item2.color.colorGroup

            return ColorScheme(
                primaryColor = primary,
                secondaryColors = listOf(secondary),
                isComplementary = isComplementary,
                isAnalogous = isAnalogous,
                isMonochromatic = isMonochromatic,
            )
        }

        private fun getCompatibilityReason(
            item1: Item,
            item2: Item,
        ): String =
            buildString {
                append("Совместимость: ")
                if (item1.color.colorGroup == item2.color.colorGroup) append("цветовая гамма, ")
                if (item1.style == item2.style) append("стиль")
            }.removeSuffix(", ")

        private fun areColorsHarmonious(
            item1: Item,
            item2: Item,
        ): Boolean =
            when {
                item1.color.colorGroup == item2.color.colorGroup -> true
                item1.color.colorGroup == Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> true
                item2.color.colorGroup == Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> true
                item1.color.isWarm && item2.color.isWarm -> true
                item1.color.isCool && item2.color.isCool -> true
                else -> false
            }
    }
}
