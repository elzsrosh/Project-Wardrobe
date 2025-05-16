@file:Suppress("ktlint:standard:no-wildcard-imports")
package com.example.wardrobecomposer.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.ui.theme.WardrobeComposerTheme
import com.example.wardrobecomposer.utils.ColorSquare
import com.example.wardrobecomposer.utils.ColorUtils
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
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var itemImageUrl by remember { mutableStateOf("") }
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> imageUri = uri },
    )

    WardrobeComposerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF0F4))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Button(
                onClick = { navController.navigateUp() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("НАЗАД", color = Color(0xFFC2185B))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "ДОБАВИТЬ НОВУЮ ВЕЩЬ",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFFC2185B))
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("НАЗВАНИЕ ВЕЩИ") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color(0xFFC2185B)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFFB6C1),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFFFFB6C1),
                    containerColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("URL изображения:", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = itemImageUrl,
                onValueChange = { itemImageUrl = it },
                label = { Text("Введите URL изображения", color = Color(0xFFC2185B)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFFB6C1),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFFFFB6C1),
                    containerColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Изображение:", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
            Spacer(modifier = Modifier.height(8.dp))
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Выбранное изображение",
                    modifier = Modifier
                        .size(120.dp)
                        .clickable { galleryLauncher.launch("image/*") }
                )
            } else {
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Выбрать изображение", color = Color(0xFFC2185B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("КАТЕГОРИЯ:", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Item.Category.values().take(3).forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category.name.uppercase(), color = Color(0xFFC2185B)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            selectedContainerColor = Color(0xFFFFB6C1)
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Item.Category.values().drop(3).forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category.name.uppercase(), color = Color(0xFFC2185B)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            selectedContainerColor = Color(0xFFFFB6C1)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("МАТЕРИАЛ:", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Item.Material.values().take(3).forEach { material ->
                    FilterChip(
                        selected = selectedMaterial == material,
                        onClick = { selectedMaterial = material },
                        label = { Text(material.name.uppercase(), color = Color(0xFFC2185B)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            selectedContainerColor = Color(0xFFFFB6C1)
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Item.Material.values().drop(3).forEach { material ->
                    FilterChip(
                        selected = selectedMaterial == material,
                        onClick = { selectedMaterial = material },
                        label = { Text(material.name.uppercase(), color = Color(0xFFC2185B)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            selectedContainerColor = Color(0xFFFFB6C1)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("СТИЛЬ (МОЖНО ВЫБРАТЬ НЕСКОЛЬКО):", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
            Spacer(modifier = Modifier.height(8.dp))

            // Строка со стилями СПОРТИВНЫЙ и ДЕЛОВОЙ
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val firstLineStyles = listOf(Item.Style.СПОРТИВНЫЙ, Item.Style.ДЕЛОВОЙ)
                firstLineStyles.forEach { style ->
                    FilterChip(
                        selected = style in selectedStyles,
                        onClick = {
                            if (style in selectedStyles) selectedStyles.remove(style)
                            else selectedStyles.add(style)
                        },
                        label = { Text(style.name.uppercase(), color = Color(0xFFC2185B)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            selectedContainerColor = Color(0xFFFFB6C1)
                        )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val otherStyles = listOf(Item.Style.ПОВСЕДНЕВНЫЙ, Item.Style.ОФИЦИАЛЬНЫЙ)
                otherStyles.forEach { style ->
                    FilterChip(
                        selected = style in selectedStyles,
                        onClick = {
                            if (style in selectedStyles) selectedStyles.remove(style)
                            else selectedStyles.add(style)
                        },
                        label = { Text(style.name.uppercase(), color = Color(0xFFC2185B)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            selectedContainerColor = Color(0xFFFFB6C1)
                        )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val otherStyles = listOf(Item.Style.ВЕЧЕРНИЙ, Item.Style.УЛИЧНЫЙ)
                otherStyles.forEach { style ->
                    FilterChip(
                        selected = style in selectedStyles,
                        onClick = {
                            if (style in selectedStyles) selectedStyles.remove(style)
                            else selectedStyles.add(style)
                        },
                        label = { Text(style.name.uppercase(), color = Color(0xFFC2185B)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            selectedContainerColor = Color(0xFFFFB6C1)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("ЦВЕТ:", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Item.Color.ColorGroup.values().take(7).forEach { group ->
                    ColorSquare(
                        color = ColorUtils.colorForGroup(group),
                        selected = selectedColorGroup == group,
                        onClick = { selectedColorGroup = group }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Item.Color.ColorGroup.values().drop(7).take(7).forEach { group ->
                    ColorSquare(
                        color = ColorUtils.colorForGroup(group),
                        selected = selectedColorGroup == group,
                        onClick = { selectedColorGroup = group }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Item.Color.ColorGroup.values().drop(14).take(3).forEach { group ->
                    ColorSquare(
                        color = ColorUtils.colorForGroup(group),
                        selected = selectedColorGroup == group,
                        onClick = { selectedColorGroup = group }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (itemName.isNotBlank() && selectedCategory != null && selectedMaterial != null && selectedStyles.isNotEmpty() && selectedColorGroup != null) {
                        val newItem = Item(
                            id = UUID.randomUUID().toString(),
                            name = itemName,
                            category = selectedCategory!!,
                            material = selectedMaterial!!,
                            style = selectedStyles.first(),
                            color = Item.Color(hex = "#FFB6C1", colorGroup = selectedColorGroup!!),
                            imageUri = imageUri?.toString() ?: itemImageUrl
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
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("СОХРАНИТЬ", color = Color(0xFFC2185B))
            }
        }
    }
}