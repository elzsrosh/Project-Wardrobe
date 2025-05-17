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
        СЕРЕБРО,
        ЗОЛОТО,
    }

    @Serializable
    data class Color(
        val hex: String,
        val colorGroup: ColorGroup,
    ) {
        @Serializable
        enum class ColorGroup {
            НЕЙТРАЛЬНЫЙ,
            БЕЛЫЙ,
            ЧЁРНЫЙ,
            СЕРЫЙ,
            ПАСТЕЛЬНЫЙ,
            ТЁПЛЫЙ,
            ОРАНЖЕВЫЙ,
            ЖЁЛТЫЙ,
            КРАСНЫЙ,
            ЗЕМЛЯНОЙ,
            КОРИЧНЕВЫЙ,
            ХОЛОДНЫЙ,
            СИНИЙ,
            ФИОЛЕТОВЫЙ,
            ЗЕЛЁНЫЙ,
            ЯРКИЙ,
            ЛАВАНДА,
            МЯТА,
            АКВА,
            ЛОСОСЕВЫЙ,
            БОРДОВЫЙ,
            ПЕСОЧНЫЙ,
            РОЗОВЫЙ,
            ЗОЛОТОЙ,
            СЕРЕБРЯНЫЙ
        }

        fun toHexWithoutHash(): String = hex.replace("#", "")

        companion object {
            fun fromHex(hex: String): Color {
                val normalizedHex = if (hex.startsWith("#")) hex.substring(1) else hex
                val group = when {
                    // Нейтральные
                    normalizedHex.matches(Regex("(?i)^(B0BEC5|FFFFFF|000000|9E9E9E|E8F5E9).*")) ->
                        ColorGroup.НЕЙТРАЛЬНЫЙ

                    // Тёплые
                    normalizedHex.matches(Regex("(?i)^FFB300.*")) -> ColorGroup.ОРАНЖЕВЫЙ
                    normalizedHex.matches(Regex("(?i)^FFEB3B.*")) -> ColorGroup.ЖЁЛТЫЙ
                    normalizedHex.matches(Regex("(?i)^F44336.*")) -> ColorGroup.КРАСНЫЙ
                    normalizedHex.matches(Regex("(?i)^8D6E63.*")) -> ColorGroup.ЗЕМЛЯНОЙ
                    normalizedHex.matches(Regex("(?i)^795548.*")) -> ColorGroup.КОРИЧНЕВЫЙ
                    normalizedHex.matches(Regex("(?i)^(FFB300|F44336|FF9800|FFEB3B|8D6E63|795548).*")) ->
                        ColorGroup.ТЁПЛЫЙ

                    // Холодные
                    normalizedHex.matches(Regex("(?i)^2196F3.*")) -> ColorGroup.СИНИЙ
                    normalizedHex.matches(Regex("(?i)^9C27B0.*")) -> ColorGroup.ФИОЛЕТОВЫЙ
                    normalizedHex.matches(Regex("(?i)^4CAF50.*")) -> ColorGroup.ЗЕЛЁНЫЙ
                    normalizedHex.matches(Regex("(?i)^CE93D8.*")) -> ColorGroup.ЛАВАНДА
                    normalizedHex.matches(Regex("(?i)^AED581.*")) -> ColorGroup.МЯТА
                    normalizedHex.matches(Regex("(?i)^4DD0E1.*")) -> ColorGroup.АКВА
                    normalizedHex.matches(Regex("(?i)^FF00FF.*")) -> ColorGroup.ЯРКИЙ
                    normalizedHex.matches(Regex("(?i)^(64B5F6|4CAF50|2196F3|9C27B0|E040FB|E91E63).*")) ->
                        ColorGroup.ХОЛОДНЫЙ

                    // Дополнительные
                    normalizedHex.matches(Regex("(?i)^FF8A65.*")) -> ColorGroup.ЛОСОСЕВЫЙ
                    normalizedHex.matches(Regex("(?i)^880E4F.*")) -> ColorGroup.БОРДОВЫЙ
                    normalizedHex.matches(Regex("(?i)^FFF176.*")) -> ColorGroup.ПЕСОЧНЫЙ
                    normalizedHex.matches(Regex("(?i)^E91E63.*")) -> ColorGroup.РОЗОВЫЙ
                    normalizedHex.matches(Regex("(?i)^FFD700.*")) -> ColorGroup.ЗОЛОТОЙ
                    normalizedHex.matches(Regex("(?i)^C0C0C0.*")) -> ColorGroup.СЕРЕБРЯНЫЙ

                    else -> ColorGroup.ХОЛОДНЫЙ
                }
                return Color("#$normalizedHex", group)
            }
        }

        val isWarm: Boolean
            get() =
                when (colorGroup) {
                    ColorGroup.ТЁПЛЫЙ,
                    ColorGroup.ОРАНЖЕВЫЙ,
                    ColorGroup.ЖЁЛТЫЙ,
                    ColorGroup.КРАСНЫЙ,
                    ColorGroup.ЗЕМЛЯНОЙ,
                    ColorGroup.КОРИЧНЕВЫЙ,
                    ColorGroup.ЛОСОСЕВЫЙ,
                    ColorGroup.БОРДОВЫЙ,
                    ColorGroup.ПЕСОЧНЫЙ,
                    ColorGroup.ЗОЛОТОЙ -> true
                    else -> false
                }

        val isCool: Boolean
            get() =
                when (colorGroup) {
                    ColorGroup.ХОЛОДНЫЙ,
                    ColorGroup.СИНИЙ,
                    ColorGroup.ФИОЛЕТОВЫЙ,
                    ColorGroup.ЗЕЛЁНЫЙ,
                    ColorGroup.ЛАВАНДА,
                    ColorGroup.МЯТА,
                    ColorGroup.АКВА,
                    ColorGroup.ЯРКИЙ,
                    ColorGroup.РОЗОВЫЙ,
                    ColorGroup.СЕРЕБРЯНЫЙ -> true
                    else -> false
                }
    }
}