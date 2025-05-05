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
    enum class Category {
        ВЕРХ,
        НИЗ,
        ПЛАТЬЕ,
        ВЕРХНЯЯ_ОДЕЖДА,
        ОБУВЬ,
        АКСЕССУАРЫ
    }

    enum class Style {
        ПОВСЕДНЕВНЫЙ,
        ОФИЦИАЛЬНЫЙ,
        СПОРТИВНЫЙ,
        ДЕЛОВОЙ,
        ВЕЧЕРНИЙ,
        УЛИЧНЫЙ
    }

    enum class Material {
        ХЛОПОК,
        ШЕРСТЬ,
        ШЁЛК,
        ДЖИНСА,
        КОЖА,
        СИНТЕТИКА
    }

    data class Color(
        val hex: String,
        val colorGroup: ColorGroup
    ) {
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
            БЕЛЫЙ
        }

        val isWarm: Boolean
            get() = colorGroup == ColorGroup.ТЁПЛЫЙ
                    || colorGroup == ColorGroup.ЗЕМЛЯНОЙ
                    || colorGroup == ColorGroup.КРАСНЫЙ
                    || colorGroup == ColorGroup.ОРАНЖЕВЫЙ
                    || colorGroup == ColorGroup.ЖЁЛТЫЙ

        val isCool: Boolean
            get() = colorGroup == ColorGroup.ХОЛОДНЫЙ
                    || colorGroup == ColorGroup.ЯРКИЙ
                    || colorGroup == ColorGroup.СИНИЙ
                    || colorGroup == ColorGroup.ФИОЛЕТОВЫЙ
                    || colorGroup == ColorGroup.ЗЕЛЁНЫЙ
    }
}