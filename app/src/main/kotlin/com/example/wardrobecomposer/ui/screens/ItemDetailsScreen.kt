@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wardrobecomposer.model.item.Item
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun itemDetailsScreen(
    item: Item,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
        ) {
            if (item.imageUri.isNotEmpty()) {
                AsyncImage(
                    model = item.imageUri,
                    contentDescription = item.name,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(MaterialTheme.shapes.medium),
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("Категория: ${item.category.name}", style = MaterialTheme.typography.titleMedium)
            Text("Материал: ${item.material.name}", style = MaterialTheme.typography.titleMedium)
            Text("Стиль: ${item.style.name}", style = MaterialTheme.typography.titleMedium)
            Text("Цвет: ${item.color.hex} (${item.color.colorGroup.name})", style = MaterialTheme.typography.titleMedium)

            if (item.isFavorite) {
                Text("Избранное: Да", style = MaterialTheme.typography.titleMedium)
            }

            if (item.lastWornDate != null) {
                Text("Последнее использование: ${Date(item.lastWornDate)}", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
