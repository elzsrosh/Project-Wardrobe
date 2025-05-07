package com.example.wardrobecomposer.model.item

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: String,
    val name: String,
    val category: Category,
    val color: Color,
    val style: Style,
    val material: Material,
    val imageUri: String,
    val isFavorite: Boolean = false,
    val lastWornDate: Long? = null,
) {
    @Serializable
    enum class Category {
        ВЕРХ,
        НИЗ,
        ПЛАТЬЕ,
        ВЕРХНЯЯ_ОДЕЖДА,
        ОБУВЬ,
        АКСЕССУАРЫ,
    }

    @Serializable
    enum class Style {
        ПОВСЕДНЕВНЫЙ,
        ОФИЦИАЛЬНЫЙ,
        СПОРТИВНЫЙ,
        ДЕЛОВОЙ,
        ВЕЧЕРНИЙ,
        УЛИЧНЫЙ,
    }

    @Serializable
    enum class Material {
        ХЛОПОК,
        ШЕРСТЬ,
        ШЁЛК,
        ДЖИНСА,
        КОЖА,
        СИНТЕТИКА,
    }

    @Serializable
    data class Color(
        val hex: String,
        val colorGroup: ColorGroup,
    ) {
        @Serializable
        enum class ColorGroup {
            НЕЙТРАЛЬНЫЙ,
            ТЁПЛЫЙ,
            ХОЛОДНЫЙ,
            ЗЕМЛЯНОЙ,
            ПАСТЕЛЬНЫЙ,
            ЯРКИЙ,
            КРАСНЫЙ,
            ОРАНЖЕВЫЙ,
            ЖЁЛТЫЙ,
            ЗЕЛЁНЫЙ,
            СИНИЙ,
            ФИОЛЕТОВЫЙ,
            РОЗОВЫЙ,
            КОРИЧНЕВЫЙ,
            СЕРЫЙ,
            ЧЁРНЫЙ,
            БЕЛЫЙ,
        }

        fun toHexWithoutHash(): String = hex.replace("#", "")

        companion object {
            fun fromHex(hex: String): Color {
                val group =
                    when {
                        hex.matches(Regex("(?i)^(B0BEC5|9E9E9E|000000|FFFFFF).*")) ->
                            ColorGroup.НЕЙТРАЛЬНЫЙ
                        hex.matches(Regex("(?i)^(FFB300|F44336|FF9800|FFEB3B|8D6E63|795548).*")) ->
                            ColorGroup.ТЁПЛЫЙ
                        hex.matches(Regex("(?i)^(64B5F6|4CAF50|2196F3|9C27B0|E040FB|E91E63).*")) ->
                            ColorGroup.ХОЛОДНЫЙ
                        else -> ColorGroup.НЕЙТРАЛЬНЫЙ
                    }
                return Color("#$hex", group)
            }
        }

        val isWarm: Boolean
            get() =
                colorGroup == ColorGroup.ТЁПЛЫЙ ||
                    colorGroup == ColorGroup.ЗЕМЛЯНОЙ ||
                    colorGroup == ColorGroup.КРАСНЫЙ ||
                    colorGroup == ColorGroup.ОРАНЖЕВЫЙ ||
                    colorGroup == ColorGroup.ЖЁЛТЫЙ

        val isCool: Boolean
            get() =
                colorGroup == ColorGroup.ХОЛОДНЫЙ ||
                    colorGroup == ColorGroup.ЯРКИЙ ||
                    colorGroup == ColorGroup.СИНИЙ ||
                    colorGroup == ColorGroup.ФИОЛЕТОВЫЙ ||
                    colorGroup == ColorGroup.ЗЕЛЁНЫЙ
    }
}
