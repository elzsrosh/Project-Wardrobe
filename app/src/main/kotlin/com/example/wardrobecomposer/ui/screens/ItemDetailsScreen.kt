package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wardrobecomposer.model.item.Item
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    item: Item,
    onBackClick: () -> Unit,
    viewModel: WardrobeViewModel
) {
    val advice by viewModel.styleAdvice.collectAsState()

    LaunchedEffect(item.name) {
        viewModel.getStyleAdvice(item.name)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {},
                icon = { Icon(Icons.Default.Lightbulb, "Совет") },
                text = { Text("Совет") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (item.imageUri.isNotEmpty()) {
                AsyncImage(
                    model = item.imageUri,
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("Категория: ${item.category.name}", style = MaterialTheme.typography.titleMedium)
            Text("Материал: ${item.material.name}", style = MaterialTheme.typography.titleMedium)
            Text("Стиль: ${item.style.name}", style = MaterialTheme.typography.titleMedium)
            Text("Цвет: ${item.color.hex} (${item.color.colorGroup.name})",
                style = MaterialTheme.typography.titleMedium)

            if (item.isFavorite) {
                Text("Избранное: Да", style = MaterialTheme.typography.titleMedium)
            }

            if (item.lastWornDate != null) {
                Text("Последнее использование: ${Date(item.lastWornDate)}",
                    style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Совет",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = advice,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}