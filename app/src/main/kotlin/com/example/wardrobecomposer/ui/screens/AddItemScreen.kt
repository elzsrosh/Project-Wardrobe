@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wardrobecomposer.model.item.Item
import java.util.*

@Composable
fun AddItemScreen(navController: NavController, onItemAdded: (Item) -> Unit) {
    var itemName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Item.Category?>(null) }
    var selectedMaterial by remember { mutableStateOf<Item.Material?>(null) }

    // Позволяет выбрать несколько стилей
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
        // Кнопка возврата
        Button(
            onClick = { navController.navigateUp() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            )
        ) {
            Text("Назад", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Добавить новую вещь",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                // Поле ввода названия вещи
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Название вещи") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Выбор категории
                Text(
                    text = "Категория:",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .selectableGroup(),
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

                // Выбор материала
                Text(
                    text = "Материал:",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .selectableGroup(),
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

                // Выбор стиля (можно выбрать несколько)
                Text(
                    text = "Стиль (можно выбрать несколько):",
                    style = MaterialTheme.typography.titleMedium
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Item.Style.values().forEach { style ->
                        FilterChip(
                            selected = style in selectedStyles,
                            onClick = {
                                if (style in selectedStyles) {
                                    selectedStyles.remove(style)
                                } else {
                                    selectedStyles.add(style)
                                }
                            },
                            label = { Text(style.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Выбор цвета
                Text(
                    text = "Цвет:",
                    style = MaterialTheme.typography.titleMedium
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Item.Color.ColorGroup.values().forEach { colorGroup ->
                        FilterChip(
                            selected = selectedColorGroup == colorGroup,
                            onClick = { selectedColorGroup = colorGroup },
                            label = { Text(colorGroup.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопка сохранения
                Button(
                    onClick = {
                        if (itemName.isNotBlank() && selectedCategory != null &&
                            selectedMaterial != null && selectedStyles.isNotEmpty() &&
                            selectedColorGroup != null) {

                            // Создаем элемент с выбранным стилем
                            val newItem = Item(
                                id = UUID.randomUUID().toString(),
                                name = itemName,
                                category = selectedCategory!!,
                                material = selectedMaterial!!,
                                style = selectedStyles.first(), // Используем первый выбранный стиль
                                color = Item.Color(
                                    hex = "#000000", // Стандартный hex
                                    colorGroup = selectedColorGroup!!
                                ),
                                imageUri = "" // Пусто пока
                            )

                            onItemAdded(newItem)
                            navController.navigateUp()
                        }
                    },
                    enabled = itemName.isNotBlank() && selectedCategory != null &&
                            selectedMaterial != null && selectedStyles.isNotEmpty() &&
                            selectedColorGroup != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Сохранить",
                        style = TextStyle(fontSize = 18.sp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
