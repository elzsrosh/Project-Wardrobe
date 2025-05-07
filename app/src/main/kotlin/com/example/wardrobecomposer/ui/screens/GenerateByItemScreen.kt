@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wardrobecomposer.model.item.Item

@Suppress("ktlint:standard:function-naming")
@Composable
fun GenerateByItemScreen(
    items: List<Item>,
    onBackClick: () -> Unit,
    onGenerate: (Item) -> Unit,
) {
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = onBackClick) { Text("Назад") }
        Text("Выберите базовую вещь:", style = MaterialTheme.typography.titleLarge)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items) { item ->
                ItemSelectionCard(
                    item = item,
                    isSelected = selectedItem?.id == item.id,
                    onSelect = { selectedItem = item },
                )
            }
        }

        Button(
            onClick = { selectedItem?.let { onGenerate(it) } },
            enabled = selectedItem != null,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Сгенерировать образы")
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSelectionCard(
    item: Item,
    isSelected: Boolean,
    onSelect: (Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onSelect(item) },
        modifier =
            modifier
                .fillMaxWidth()
                .padding(8.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
            ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = item.imageUri,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                Text("Категория: ${item.category.name}")
                Text("Цвет: ${item.color.colorGroup.name}")
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Выбрано",
                )
            }
        }
    }
}
