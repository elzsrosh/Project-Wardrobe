@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.foundation.layout.ExperimentalLayoutApi::class
)
package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wardrobecomposer.model.item.Item
import java.util.*

@Composable
fun AddItemScreen(
    navController: NavController,
    onItemAdded: (Item) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Item.Category?>(null) }
    var selectedMaterial by remember { mutableStateOf<Item.Material?>(null) }
    val selectedStyles = remember { mutableStateListOf<Item.Style>() }
    var selectedColorGroup by remember { mutableStateOf<Item.Color.ColorGroup?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Button(
            onClick = { navController.navigateUp() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text("Назад")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Добавить новую вещь", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            item {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Название вещи") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Категория:", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Item.Category.values().forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Материал:", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Item.Material.values().forEach { material ->
                        FilterChip(
                            selected = selectedMaterial == material,
                            onClick = { selectedMaterial = material },
                            label = { Text(material.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Стиль (можно выбрать несколько):", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Item.Style.values().forEach { style ->
                        FilterChip(
                            selected = style in selectedStyles,
                            onClick = {
                                if (style in selectedStyles) selectedStyles.remove(style)
                                else selectedStyles.add(style)
                            },
                            label = { Text(style.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Цвет:", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Item.Color.ColorGroup.values().forEach { colorGroup ->
                        ColorSquare(
                            color = colorForGroup(colorGroup),
                            selected = selectedColorGroup == colorGroup,
                            onClick = { selectedColorGroup = colorGroup }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (itemName.isNotBlank() && selectedCategory != null &&
                            selectedMaterial != null && selectedStyles.isNotEmpty() &&
                            selectedColorGroup != null
                        ) {
                            val newItem = Item(
                                id = UUID.randomUUID().toString(),
                                name = itemName,
                                category = selectedCategory!!,
                                material = selectedMaterial!!,
                                style = selectedStyles.first(),
                                color = Item.Color(
                                    hex = "#000000", // Можно добавить hex для каждого цвета
                                    colorGroup = selectedColorGroup!!
                                ),
                                imageUri = ""
                            )
                            onItemAdded(newItem)
                            navController.navigateUp()
                        }
                    },
                    enabled = itemName.isNotBlank() && selectedCategory != null &&
                            selectedMaterial != null && selectedStyles.isNotEmpty() &&
                            selectedColorGroup != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Сохранить")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// Квадратик цвета
@Composable
fun ColorSquare(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(6.dp),
        border = if (selected) BorderStroke(3.dp, Color.Black) else null,
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick() }
    ) {}
}

// Маппинг группы цвета на реальный цвет
fun colorForGroup(group: Item.Color.ColorGroup): Color = when (group) {
    Item.Color.ColorGroup.NEUTRAL -> Color(0xFFB0BEC5)
    Item.Color.ColorGroup.WARM -> Color(0xFFFFB300)
    Item.Color.ColorGroup.COOL -> Color(0xFF64B5F6)
    Item.Color.ColorGroup.EARTH -> Color(0xFF8D6E63)
    Item.Color.ColorGroup.PASTEL -> Color(0xFFFFF59D)
    Item.Color.ColorGroup.BRIGHT -> Color(0xFFE040FB)
}
