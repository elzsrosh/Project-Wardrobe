@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wardrobecomposer.model.item.*
import com.example.wardrobecomposer.repository.WardrobeRepository
import kotlinx.coroutines.*

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GenerateOutfitScreen(
    navController: NavController,
    repository: WardrobeRepository,
    existingItems: List<Item> = emptyList(),
) {
    var selectedOption by remember { mutableStateOf(0) }
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    var tempItem by remember { mutableStateOf<Item?>(null) }
    var generatedLooks by remember { mutableStateOf<List<Look>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
    ) {
        Text("СОЗДАТЬ ОБРАЗ", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(
                selected = selectedOption == 0,
                onClick = { selectedOption = 0 },
                label = { Text("ИЗ ГАРДЕРОБА") },
            )
            FilterChip(
                selected = selectedOption == 1,
                onClick = { selectedOption = 1 },
                label = { Text("НОВАЯ ВЕЩЬ") },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedOption) {
            0 -> {
                Text("Выберите базовую вещь:", style = MaterialTheme.typography.titleMedium)
                LazyColumn {
                    items(existingItems) { item ->
                        ItemSelectionCard(
                            item = item,
                            isSelected = selectedItem?.id == item.id,
                            onSelect = { selectedItem = item },
                        )
                    }
                }
            }
            1 -> {
                var selectedCategory by remember { mutableStateOf<Item.Category?>(null) }
                var selectedColor by remember { mutableStateOf<Item.Color?>(null) }

                Text("Категория:", style = MaterialTheme.typography.titleMedium)
                LazyRow {
                    items(Item.Category.values()) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.name.uppercase()) },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Цвет:", style = MaterialTheme.typography.titleMedium)
                LazyRow {
                    items(Item.Color.ColorGroup.values()) { group ->
                        ColorSquare(
                            color = colorForGroup(group),
                            selected = selectedColor?.colorGroup == group,
                            onClick = {
                                selectedColor =
                                    Item.Color(
                                        hex = colorForGroup(group).toString(),
                                        colorGroup = group,
                                    )
                            },
                        )
                    }
                }

                if (selectedCategory != null && selectedColor != null) {
                    tempItem =
                        Item(
                            id = "temp_${System.currentTimeMillis()}",
                            name = "Новая вещь",
                            category = selectedCategory!!,
                            color = selectedColor!!,
                            style = Item.Style.ПОВСЕДНЕВНЫЙ,
                            material = Item.Material.ХЛОПОК,
                            imageUri = "",
                        )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                CoroutineScope(Dispatchers.IO).launch {
                    val looks =
                        when (selectedOption) {
                            0 ->
                                selectedItem?.let {
                                    repository.generateOutfitFromExisting(it)
                                } ?: emptyList()
                            else ->
                                tempItem?.let {
                                    repository.generateOutfitFromNewItem(it)
                                } ?: emptyList()
                        }
                    withContext(Dispatchers.Main) {
                        generatedLooks = looks
                        isLoading = false
                    }
                }
            },
            enabled =
                (selectedOption == 0 && selectedItem != null) ||
                    (selectedOption == 1 && tempItem != null),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("СГЕНЕРИРОВАТЬ ОБРАЗ")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (generatedLooks.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Результаты:", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(generatedLooks) { look ->
                    LookCard(look = look)
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun LookCard(look: Look) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Совместимый образ", style = MaterialTheme.typography.titleMedium)
            LazyRow {
                items(look.items) { item ->
                    Box(modifier = Modifier.padding(4.dp)) {
                        AsyncImage(
                            model = item.imageUri,
                            contentDescription = item.name,
                            modifier = Modifier.size(96.dp),
                        )
                    }
                }
            }
            if (look.compatibilityReason.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Совместимость: ${look.compatibilityReason}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ColorSquare(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color)
                .border(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
                ).clickable { onClick() },
    )
}

fun colorForGroup(group: Item.Color.ColorGroup): Color =
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
