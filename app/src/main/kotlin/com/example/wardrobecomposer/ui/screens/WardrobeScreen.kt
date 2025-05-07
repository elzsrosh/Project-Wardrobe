@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wardrobecomposer.model.item.Item

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WardrobeScreen(
    onBackClick: () -> Unit,
    onAddItemClick: () -> Unit,
    items: List<Item>,
    onItemClick: (Item) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мой гардероб") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddItemClick,
                icon = { Icon(Icons.Filled.Add, contentDescription = "Добавить") },
                text = { Text("Добавить вещь") },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Нет добавленных вещей")
                }
            } else {
                LazyColumn {
                    items(items) { item ->
                        ItemCard(
                            item = item,
                            onClick = { onItemClick(item) },
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (item.imageUri.isNotEmpty()) {
                AsyncImage(
                    model = item.imageUri,
                    contentDescription = item.name,
                    modifier = Modifier.size(64.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.headlineSmall)
                Text("Категория: ${item.category.name}")
                Text("Цвет: ${item.color.colorGroup.name}")
                Text("Стиль: ${item.style.name}")
            }
        }
    }
}
