@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wardrobecomposer.model.item.Look

@Suppress("ktlint:standard:function-naming")
@Composable
fun LooksListScreen(
    looks: List<Look>,
    onBackClick: () -> Unit,
    onLookClick: (Look) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Button(onClick = onBackClick) {
            Text("Назад")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Сгенерированные образы:", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        if (looks.isEmpty()) {
            Text("Нет сгенерированных образов")
        } else {
            LazyColumn {
                items(looks) { look ->
                    LookCard(
                        look = look,
                        onClick = { onLookClick(look) },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LookCard(
    look: Look,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(look.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Стиль: ${look.style.name}")
            Text("Цветовая схема: ${look.colorScheme.primaryColor.colorGroup.name}")
            if (look.compatibilityReason.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(look.compatibilityReason, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
