package com.example.wardrobecomposer.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.wardrobecomposer.model.item.Item

@Suppress("ktlint:standard:function-naming")
@Composable
fun ColorSquare(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(color)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
            )
            .clickable { onClick() },
    )
}

fun colorForGroup(group: Item.Color.ColorGroup): Color =
    when (group) {
        Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ -> Color(0xFFB0BEC5)
        Item.Color.ColorGroup.ТЁПЛЫЙ -> Color(0xFFFFB300)
        Item.Color.ColorGroup.ХОЛОДНЫЙ -> Color(0xFF64B5F6)
        Item.Color.ColorGroup.ЗЕМЛЯНОЙ -> Color(0xFF8D6E63)
        Item.Color.ColorGroup.ПАСТЕЛЬНЫЙ -> Color(0xFFE8F5E9)
        Item.Color.ColorGroup.ЯРКИЙ -> Color(0xFFFF00FF)
        Item.Color.ColorGroup.КРАСНЫЙ -> Color(0xFFF44336)
        Item.Color.ColorGroup.ОРАНЖЕВЫЙ -> Color(0xFFFF9800)
        Item.Color.ColorGroup.ЖЁЛТЫЙ -> Color(0xFFFFEB3B)
        Item.Color.ColorGroup.ЗЕЛЁНЫЙ -> Color(0xFF4CAF50)
        Item.Color.ColorGroup.СИНИЙ -> Color(0xFF2196F3)
        Item.Color.ColorGroup.ФИОЛЕТОВЫЙ -> Color(0xFF9C27B0)
        Item.Color.ColorGroup.РОЗОВЫЙ -> Color(0xFFE91E63)
        Item.Color.ColorGroup.КОРИЧНЕВЫЙ -> Color(0xFF795548)
        Item.Color.ColorGroup.СЕРЫЙ -> Color(0xFF9E9E9E)
        Item.Color.ColorGroup.ЧЁРНЫЙ -> Color(0xFF000000)
        Item.Color.ColorGroup.БЕЛЫЙ -> Color(0xFFFFFFFF)
        Item.Color.ColorGroup.ЛАВАНДА -> Color(0xFFCE93D8)
        Item.Color.ColorGroup.МЯТА -> Color(0xFFAED581)
        Item.Color.ColorGroup.АКВА -> Color(0xFF4DD0E1)
        Item.Color.ColorGroup.ЛОСОСЕВЫЙ -> Color(0xFFFF8A65)
        Item.Color.ColorGroup.БОРДОВЫЙ -> Color(0xFF880E4F)
        Item.Color.ColorGroup.ПЕСОЧНЫЙ -> Color(0xFFFFF176)
        Item.Color.ColorGroup.ЗОЛОТОЙ -> Color(0xFFFFD700)
        Item.Color.ColorGroup.СЕРЕБРЯНЫЙ -> Color(0xFFC0C0C0)
    }