package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wardrobecomposer.utils.ColorUtils
import com.example.wardrobecomposer.ui.theme.WardrobeComposerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    itemId: String,
    viewModel: WardrobeViewModel,
    onBackClick: () -> Unit,
    onGenerateLooks: () -> Unit
) {
    val item by viewModel.selectedItem.collectAsState()
    val colorPalette by viewModel.colorPalette.collectAsState()
    val styleAdvice by viewModel.styleAdvice.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(itemId) {
        viewModel.selectItem(itemId)
    }

    WardrobeComposerTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(item?.name ?: "Детали вещи", color = Color(0xFFC2185B)) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = Color(0xFFC2185B))
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFFFFF0F4))
                    .padding(16.dp)
            ) {
                if (item == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Вещь не найдена", color = Color(0xFFC2185B))
                    }
                    return@Scaffold
                }

                // Отображение изображения и деталей вещи
                if (item?.imageUri?.isNotEmpty() == true) {
                    AsyncImage(
                        model = item?.imageUri,
                        contentDescription = item?.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text("Категория: ${item?.category?.name}", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
                Text("Материал: ${item?.material?.name}", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
                Text("Стиль: ${item?.style?.name}", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
                Text("Цвет: ${item?.color?.hex} (${item?.color?.colorGroup?.name})", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            item?.let {
                                val hex = it.color.hex.removePrefix("#")
                                viewModel.generateColorPalette(hex, "analogic")
                                viewModel.getStyleAdvice(
                                    itemName = it.name,
                                    type = it.category?.name,
                                    material = it.material?.name,
                                    style = it.style?.name,
                                    color = it.color?.hex
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("СОВЕТ И ПАЛИТРА", color = Color(0xFFC2185B))
                    }

                    Button(
                        onClick = {
                            item?.let {
                                viewModel.generateLooksFromItem(it)
                                onGenerateLooks()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("ОБРАЗЫ", color = Color(0xFFC2185B))
                    }
                }

                if (isLoading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color(0xFFC2185B)
                    )
                }

                // Отображение палитры
                if (colorPalette.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Сгенерированная палитра:", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        colorPalette.forEach { colorHex ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(ColorUtils.hexToColor(colorHex))
                            )
                        }
                    }
                }

                // Отображение совета по стилю
                if (styleAdvice.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Совет по стилю:", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = styleAdvice,
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFFC2185B),
                            maxLines = Int.MAX_VALUE,
                            softWrap = true
                        )
                    }
                }
            }
        }
    }
}