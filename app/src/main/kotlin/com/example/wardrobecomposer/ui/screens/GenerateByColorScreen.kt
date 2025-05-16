@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.utils.ColorUtils
import com.example.wardrobecomposer.utils.ColorPickerDialog
import com.example.wardrobecomposer.ui.theme.WardrobeComposerTheme

@Composable
fun GenerateByColorScreen(
    viewModel: WardrobeViewModel,
    onBackClick: () -> Unit,
) {
    var selectedGroup by remember { mutableStateOf<Item.Color.ColorGroup?>(null) }
    var selectedColor by remember { mutableStateOf<Color?>(null) }
    var selectedMode by remember { mutableStateOf("complement") }
    val colorPalette by viewModel.colorPalette.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showColorPickerDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val modes = listOf(
        "complement" to "Классический",
        "analogic" to "Аналогичный",
        "monochrome-light" to "Монохромный светлый",
        "triad" to "Триада"
    )

    WardrobeComposerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color(0xFFFFF0F4))
                .padding(16.dp)
        ) {
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Назад", color = Color(0xFFC2185B))
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Выберите цветовую группу:",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFC2185B)
            )

            val groupedColors = Item.Color.ColorGroup.values().toList().chunked(4)
            groupedColors.forEach { rowGroups ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowGroups.forEach { group ->
                        ColorGroupButton(
                            group = group,
                            selected = selectedGroup == group,
                            onClick = {
                                selectedGroup = group
                                selectedColor = null
                            }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(16.dp))

            Text("Выберите стиль палитры:", color = Color(0xFFC2185B), fontWeight = FontWeight.Bold)
            Column {
                modes.chunked(2).forEach { modeRow ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        modeRow.forEach { (modeValue, modeLabel) ->
                            Button(
                                onClick = { selectedMode = modeValue },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedMode == modeValue) Color(0xFFC2185B) else Color.White
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                            ) {
                                Text(
                                    text = modeLabel,
                                    color = if (selectedMode == modeValue) Color.White else Color(0xFFC2185B)
                                )
                            }
                        }
                        if (modeRow.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { showColorPickerDialog = true },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Выбрать цвет вручную", color = Color(0xFFC2185B))
            }

            selectedColor?.let { color ->
                Spacer(Modifier.height(12.dp))
                Text("Выбранный цвет:", color = Color(0xFFC2185B))
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(color)
                        .border(1.dp, Color.DarkGray)
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val hex = selectedColor?.let { ColorUtils.colorToHex(it) }
                        ?: selectedGroup?.let { ColorUtils.colorToHex(ColorUtils.colorForGroup(it)) }

                    if (hex != null) {
                        viewModel.generateColorPalette(hex, selectedMode)
                    }
                },
                enabled = !isLoading && (selectedColor != null || selectedGroup != null),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color(0xFFC2185B)
                    )
                } else {
                    Text("Сгенерировать палитру 🎨", color = Color(0xFFC2185B))
                }
            }

            Spacer(Modifier.height(24.dp))

            if (colorPalette.isNotEmpty()) {
                Text(
                    "Гармоничная палитра:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFC2185B)
                )
                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colorPalette.take(5).forEach { colorHex ->
                        ColorItemWithLabel(colorHex = colorHex)
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    "💡 Совет: используйте до 3 цветов в одном образе для лучшего сочетания 🎨",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        if (showColorPickerDialog) {
            ColorPickerDialog(
                initialColor = selectedColor ?: Color.Black,
                onColorSelected = { color ->
                    selectedColor = color
                    selectedGroup = null
                    showColorPickerDialog = false
                },
                onDismiss = { showColorPickerDialog = false }
            )
        }
    }
}

@Composable
fun ColorItemWithLabel(
    colorHex: String,
    modifier: Modifier = Modifier
) {
    val color = Color(android.graphics.Color.parseColor(colorHex))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(85.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(color)
                .border(1.dp, Color.DarkGray)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = colorHex.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            maxLines = 1
        )
    }
}
