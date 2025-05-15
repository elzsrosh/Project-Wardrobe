@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.ui.theme.WardrobeComposerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateOutfitScreen(
    viewModel: WardrobeViewModel,
    onBackClick: () -> Unit,
    onGenerateLooks: (Item) -> Unit,
    onAddNewItemClick: () -> Unit
) {
    val items by viewModel.items.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var selectedOption by remember { mutableStateOf(0) }
    var selectedItem by remember { mutableStateOf<Item?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Генерация образов", color = Color(0xFFC2185B)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color(0xFFC2185B))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFFFF0F4))
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                "Выберите вариант:",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFC2185B)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedOption == 0,
                    onClick = { selectedOption = 0 },
                    label = { Text("Из гардероба", color = Color(0xFFC2185B)) },
                )
                FilterChip(
                    selected = selectedOption == 1,
                    onClick = {
                        selectedOption = 1
                        onAddNewItemClick()
                    },
                    label = { Text("Новая вещь", color = Color(0xFFC2185B)) },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedOption) {
                0 -> {
                    Text(
                        "Выберите базовую вещь:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFC2185B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFFC2185B))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(items) { item ->
                                ItemCard(
                                    item = item,
                                    isSelected = selectedItem?.id == item.id,
                                    onSelect = { selectedItem = item },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    selectedItem?.let { onGenerateLooks(it) }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = (selectedOption == 0 && selectedItem != null),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Сгенерировать образы", color = Color(0xFFC2185B))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(
    item: Item,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onSelect,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUri,
                contentDescription = item.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
                Text("Категория: ${item.category.name}", color = Color(0xFFC2185B))
                Text("Цвет: ${item.color.colorGroup.name}", color = Color(0xFFC2185B))
            }
        }
    }
}