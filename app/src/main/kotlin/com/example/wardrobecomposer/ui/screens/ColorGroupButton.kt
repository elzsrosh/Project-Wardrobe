@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.utils.ColorUtils
import androidx.compose.material3.MaterialTheme

@Composable
fun ColorGroupButton(
    group: Item.Color.ColorGroup,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(ColorUtils.colorForGroup(group))
                .border(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) MaterialTheme.colorScheme.primary
                    else Color.Gray
                )
                .clickable(onClick = onClick)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = group.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground, // Розовый цвет из темы
            maxLines = 1,
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 4.dp, vertical = 1.dp)
        )
    }
}