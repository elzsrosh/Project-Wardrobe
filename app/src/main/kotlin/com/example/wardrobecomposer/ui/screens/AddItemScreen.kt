@file:Suppress("ktlint:standard:no-wildcard-imports")
package com.example.wardrobecomposer.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wardrobecomposer.model.item.Item
import java.util.*

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    navController: NavController,
    onItemAdded: (Item) -> Unit,
) {
    var itemName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Item.Category?>(null) }
    var selectedMaterial by remember { mutableStateOf<Item.Material?>(null) }
    val selectedStyles = remember { mutableStateListOf<Item.Style>() }
    var selectedColorGroup by remember { mutableStateOf<Item.Color.ColorGroup?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var itemImageUrl by remember { mutableStateOf("") }
    val context = LocalContext.current

    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri -> imageUri = uri },
        )

    // Добавляем прокрутку к Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()) // 🔥 Прокрутка здесь
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Button(
            onClick = { navController.navigateUp() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
        ) {
            Text("НАЗАД")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("ДОБАВИТЬ НОВУЮ ВЕЩЬ", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("НАЗВАНИЕ ВЕЩИ") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("URL изображения:")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = itemImageUrl,
            onValueChange = { itemImageUrl = it },
            label = { Text("Введите URL изображения") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Изображение:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Выбранное изображение",
                modifier =
                    Modifier
                        .size(120.dp)
                        .clickable { galleryLauncher.launch("image/*") },
            )
        } else {
            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Выбрать изображение")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("КАТЕГОРИЯ:", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Item.Category.values().take(3).forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category.name.uppercase()) },
                )
            }
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Item.Category.values().drop(3).forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category.name.uppercase()) },
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("МАТЕРИАЛ:", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Item.Material.values().take(3).forEach { material ->
                FilterChip(
                    selected = selectedMaterial == material,
                    onClick = { selectedMaterial = material },
                    label = { Text(material.name.uppercase()) },
                )
            }
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Item.Material.values().drop(3).forEach { material ->
                FilterChip(
                    selected = selectedMaterial == material,
                    onClick = { selectedMaterial = material },
                    label = { Text(material.name.uppercase()) },
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("СТИЛЬ (МОЖНО ВЫБРАТЬ НЕСКОЛЬКО):", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Item.Style.values().take(3).forEach { style ->
                FilterChip(
                    selected = style in selectedStyles,
                    onClick = {
                        if (style in selectedStyles) {
                            selectedStyles.remove(style)
                        } else {
                            selectedStyles.add(style)
                        }
                    },
                    label = { Text(style.name.uppercase()) },
                )
            }
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Item.Style.values().drop(3).forEach { style ->
                FilterChip(
                    selected = style in selectedStyles,
                    onClick = {
                        if (style in selectedStyles) {
                            selectedStyles.remove(style)
                        } else {
                            selectedStyles.add(style)
                        }
                    },
                    label = { Text(style.name.uppercase()) },
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("ЦВЕТ:", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Item.Color.ColorGroup.values().take(6).forEach { group ->
                ColorSquare(
                    color = getColorForGroup(group),
                    selected = selectedColorGroup == group,
                    onClick = { selectedColorGroup = group },
                )
            }
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Item.Color.ColorGroup.values().drop(6).take(6).forEach { group ->
                ColorSquare(
                    color = getColorForGroup(group),
                    selected = selectedColorGroup == group,
                    onClick = { selectedColorGroup = group },
                )
            }
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Item.Color.ColorGroup.values().drop(12).forEach { group ->
                ColorSquare(
                    color = getColorForGroup(group),
                    selected = selectedColorGroup == group,
                    onClick = { selectedColorGroup = group },
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
                    val newItem =
                        Item(
                            id = UUID.randomUUID().toString(),
                            name = itemName,
                            category = selectedCategory!!,
                            material = selectedMaterial!!,
                            style = selectedStyles.first(),
                            color =
                                Item.Color(
                                    hex = "#000000",
                                    colorGroup = selectedColorGroup!!,
                                ),
                            imageUri = imageUri?.toString() ?: itemImageUrl,
                        )
                    onItemAdded(newItem)
                    navController.navigateUp()
                }
            },
            enabled =
                itemName.isNotBlank() &&
                        selectedCategory != null &&
                        selectedMaterial != null &&
                        selectedStyles.isNotEmpty() &&
                        selectedColorGroup != null,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
        ) {
            Text("СОХРАНИТЬ")
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ColorSquare(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(40.dp)
                .background(color)
                .border(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
                ).clickable(onClick = onClick),
    )
}

fun getColorForGroup(group: Item.Color.ColorGroup): Color =
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
    }