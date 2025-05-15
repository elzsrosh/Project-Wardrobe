package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LookDetailScreen(
    lookId: String,
    viewModel: WardrobeViewModel,
    onBackClick: () -> Unit
) {
    val look by viewModel.selectedLook.collectAsState()

    LaunchedEffect(lookId) {
        viewModel.selectLook(lookId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(look?.name ?: "Детали образа") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (look == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Образ не найден")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(look!!.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Стиль: ${look!!.style.name}", style = MaterialTheme.typography.titleMedium)
            Text("Цветовая схема:", style = MaterialTheme.typography.titleMedium)
            Text("- Основной цвет: ${look!!.colorScheme.primaryColor.colorGroup.name}")
            Text("- Второстепенные цвета: ${
                look!!.colorScheme.secondaryColors.joinToString { it.colorGroup.name }
            }")

            Spacer(modifier = Modifier.height(16.dp))

            Text("Тип палитры:", style = MaterialTheme.typography.titleMedium)
            Text("- Комплементарная: ${look!!.colorScheme.isComplementary}")
            Text("- Аналоговая: ${look!!.colorScheme.isAnalogous}")
            Text("- Монохромная: ${look!!.colorScheme.isMonochromatic}")

            if (look!!.compatibilityReason.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Причина совместимости:", style = MaterialTheme.typography.titleMedium)
                Text(look!!.compatibilityReason)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Вещи в образе:", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                look!!.items.forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = item.imageUri,
                                contentDescription = item.name,
                                modifier = Modifier.size(80.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(item.name, style = MaterialTheme.typography.titleMedium)
                                Text("Категория: ${item.category.name}")
                                Text("Цвет: ${item.color.colorGroup.name}")
                                Text("Стиль: ${item.style.name}")
                            }
                        }
                    }
                }
            }
        }
    }
}