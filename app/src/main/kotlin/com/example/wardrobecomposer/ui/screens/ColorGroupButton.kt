package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.utils.ColorUtils

@Composable
fun ColorGroupButton(
    group: Item.Color.ColorGroup,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(60.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(ColorUtils.colorForGroup(group))
                .border(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) ColorUtils.colorForGroup(Item.Color.ColorGroup.НЕЙТРАЛЬНЫЙ)
                    else Color(0xFF808080), // Используем HEX-значение вместо Color.Gray
                    shape = CircleShape
                )
                .clickable(onClick = onClick)
        )
        Text(
            text = group.name,
            modifier = Modifier.padding(top = 4.dp),
            color = ColorUtils.getContrastColor(ColorUtils.colorForGroup(group))
        )
    }
}