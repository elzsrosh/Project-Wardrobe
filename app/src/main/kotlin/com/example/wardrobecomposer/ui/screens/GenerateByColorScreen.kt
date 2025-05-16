@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.utils.*
import com.example.wardrobecomposer.utils.ColorSquare
import com.example.wardrobecomposer.ui.theme.WardrobeComposerTheme

@Composable
fun GenerateByColorScreen(
    viewModel: WardrobeViewModel,
    onBackClick: () -> Unit,
) {
    var selectedGroup by remember { mutableStateOf<Item.Color.ColorGroup?>(null) }
    val colorPalette by viewModel.colorPalette.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var showColorPickerDialog by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.Black) }

    WardrobeComposerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF0F4))
                .padding(16.dp),
        ) {
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            ) {
                Text("Назад", color = Color(0xFFC2185B))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Выберите цветовую группу:",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFC2185B)
            )

            val groupedColors = Item.Color.ColorGroup.values().toList().chunked(4) // По 4 группы в строке

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        selectedGroup?.let {
                            isLoading = true
                            viewModel.generateColorPalette(it.name)
                            isLoading = false
                        }
                    },
                    enabled = selectedGroup != null && !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color(0xFFC2185B)
                        )
                    } else {
                        Text("По группе", color = Color(0xFFC2185B))
                    }
                }

                Button(
                    onClick = { showColorPickerDialog = true },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Вручную", color = Color(0xFFC2185B))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (colorPalette.isNotEmpty()) {
                Text(
                    "Гармоничная палитра:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFC2185B)
                )
                Spacer(modifier = Modifier.height(8.dp))

                val rows = colorPalette.chunked(4)

                Column {
                    rows.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { colorHex ->
                                ColorItemWithLabel(
                                    colorHex = colorHex,
                                    isSelected = false,
                                    onClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showColorPickerDialog) {
            ColorPickerDialog(
                initialColor = selectedColor,
                onColorSelected = { color ->
                    selectedColor = color
                    val hex = ColorUtils.colorToHex(color)
                    isLoading = true
                    viewModel.generateColorPalette(hex)
                    isLoading = false
                },
                onDismiss = { showColorPickerDialog = false }
            )
        }
    }
}

@Composable
private fun ColorItemWithLabel(
    colorHex: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val name = ColorUtils.colorNameMap[colorHex] ?: "Неизвестный"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(85.dp)
    ) {
        ColorSquare(
            color = ColorUtils.hexToColor(colorHex),
            selected = isSelected,
            onClick = onClick,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}