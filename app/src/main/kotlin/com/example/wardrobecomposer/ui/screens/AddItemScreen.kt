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

@OptIn(ExperimentalMaterial3Api::class)
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
            Text("НАЗАД")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("ДОБАВИТЬ НОВУЮ ВЕЩЬ", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            item {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("НАЗВАНИЕ ВЕЩИ") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("КАТЕГОРИЯ:", style = MaterialTheme.typography.titleMedium)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Item.Category.values().take(3).forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.name.uppercase()) }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Item.Category.values().drop(3).forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.name.uppercase()) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("МАТЕРИАЛ:", style = MaterialTheme.typography.titleMedium)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Item.Material.values().take(3).forEach { material ->
                        FilterChip(
                            selected = selectedMaterial == material,
                            onClick = { selectedMaterial = material },
                            label = { Text(material.name.uppercase()) }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Item.Material.values().drop(3).forEach { material ->
                        FilterChip(
                            selected = selectedMaterial == material,
                            onClick = { selectedMaterial = material },
                            label = { Text(material.name.uppercase()) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("СТИЛЬ (МОЖНО ВЫБРАТЬ НЕСКОЛЬКО):", style = MaterialTheme.typography.titleMedium)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Item.Style.values().take(3).forEach { style ->
                        FilterChip(
                            selected = style in selectedStyles,
                            onClick = {
                                if (style in selectedStyles) selectedStyles.remove(style)
                                else selectedStyles.add(style)
                            },
                            label = { Text(style.name.uppercase()) }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Item.Style.values().drop(3).forEach { style ->
                        FilterChip(
                            selected = style in selectedStyles,
                            onClick = {
                                if (style in selectedStyles) selectedStyles.remove(style)
                                else selectedStyles.add(style)
                            },
                            label = { Text(style.name.uppercase()) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("ЦВЕТ:", style = MaterialTheme.typography.titleMedium)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Item.Color.ColorGroup.values().take(6).forEach { colorGroup ->
                        ColorSquare(
                            color = colorForGroup(colorGroup),
                            selected = selectedColorGroup == colorGroup,
                            onClick = { selectedColorGroup = colorGroup }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Item.Color.ColorGroup.values().drop(6).take(6).forEach { colorGroup ->
                        ColorSquare(
                            color = colorForGroup(colorGroup),
                            selected = selectedColorGroup == colorGroup,
                            onClick = { selectedColorGroup = colorGroup }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Item.Color.ColorGroup.values().drop(12).forEach { colorGroup ->
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
                        if (itemName.isNotBlank() &&
                            selectedCategory != null &&
                            selectedMaterial != null &&
                            selectedStyles.isNotEmpty() &&
                            selectedColorGroup != null
                        ) {
                            val newItem = Item(
                                id = UUID.randomUUID().toString(),
                                name = itemName,
                                category = selectedCategory!!,
                                material = selectedMaterial!!,
                                style = selectedStyles.first(),
                                color = Item.Color(
                                    hex = "#000000",
                                    colorGroup = selectedColorGroup!!
                                ),
                                imageUri = ""
                            )
                            onItemAdded(newItem)
                            navController.navigateUp()
                        }
                    },
                    enabled = itemName.isNotBlank() &&
                            selectedCategory != null &&
                            selectedMaterial != null &&
                            selectedStyles.isNotEmpty() &&
                            selectedColorGroup != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("СОХРАНИТЬ")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
