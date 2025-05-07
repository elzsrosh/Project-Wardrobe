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
import androidx.compose.ui.unit.dp
import com.example.wardrobecomposer.model.item.Look

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LookDetailScreen(
    look: Look,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(look.name) },
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
            Text("Стиль: ${look.style.name}", style = MaterialTheme.typography.titleMedium)

            Text("Цветовая схема:", style = MaterialTheme.typography.titleMedium)
            Text("Основной цвет: ${look.colorScheme.primaryColor.colorGroup.name}")
            Text("Второстепенные цвета: ${look.colorScheme.secondaryColors.map { it.colorGroup.name }}")

            Text("Тип палитры:", style = MaterialTheme.typography.titleMedium)
            Text("Комплементарная: ${look.colorScheme.isComplementary}")
            Text("Аналоговая: ${look.colorScheme.isAnalogous}")
            Text("Монохромная: ${look.colorScheme.isMonochromatic}")

            if (look.compatibilityReason.isNotBlank()) {
                Text("Причина совместимости: ${look.compatibilityReason}", style = MaterialTheme.typography.bodySmall)
            }

            Text("Рейтинг: ${look.rating}/5", style = MaterialTheme.typography.titleMedium)

            Text("Вещи в образе:", style = MaterialTheme.typography.titleMedium)
            look.items.forEach { item ->
                Text("- ${item.name} (${item.category.name}, ${item.color.colorGroup.name})")
            }
        }
    }
}
