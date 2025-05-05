package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.utils.colorForGroup

@Composable
fun GenerateByColorScreen(
    onBackClick: () -> Unit,
    onGenerate: (Item.Color.ColorGroup) -> Unit
) {
    var selectedColorGroup by remember { mutableStateOf<Item.Color.ColorGroup?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBackClick) {
            Text("Назад")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Выберите основной цвет:", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Item.Color.ColorGroup.values().forEach { colorGroup ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                ColorSquare(
                    color = colorForGroup(colorGroup),
                    selected = selectedColorGroup == colorGroup,
                    onClick = { selectedColorGroup = colorGroup }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(colorGroup.name)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { selectedColorGroup?.let { onGenerate(it) } },
            enabled = selectedColorGroup != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сгенерировать образы")
        }
    }
}