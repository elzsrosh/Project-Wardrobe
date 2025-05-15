@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.utils.ColorUtils

@Composable
fun GenerateByColorScreen(
    viewModel: WardrobeViewModel,
    onBackClick: () -> Unit,
) {
    var selectedGroup by remember { mutableStateOf<Item.Color.ColorGroup?>(null) }
    val colorPalette by viewModel.colorPalette.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Button(onClick = onBackClick) {
            Text("Назад")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Выберите цветовую группу:", style = MaterialTheme.typography.titleLarge)

        val groupedColors = Item.Color.ColorGroup.values().toList().chunked(5)

        groupedColors.forEach { rowGroups ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                rowGroups.forEach { group ->
                    ColorGroupButton(
                        group = group,
                        selected = selectedGroup == group,
                        onClick = { selectedGroup = group },
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                selectedGroup?.let {
                    isLoading = true
                    viewModel.generateColorPalette(it.name)
                    isLoading = false
                }
            },
            enabled = selectedGroup != null && !isLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Сгенерировать палитру")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (colorPalette.isNotEmpty()) {
            Text("Гармоничная палитра:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                colorPalette.forEach { colorHex ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(ColorUtils.hexToColor(colorHex))
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                }
            }
        }
    }
}