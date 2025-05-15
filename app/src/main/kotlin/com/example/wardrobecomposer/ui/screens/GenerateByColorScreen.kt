package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.utils.ColorUtils
import androidx.compose.foundation.background

@Composable
fun GenerateByColorScreen(
    viewModel: WardrobeViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onGenerateLooks: () -> Unit
) {
    var selectedGroup by remember { mutableStateOf<Item.Color.ColorGroup?>(null) }
    val colorPalette by viewModel.colorPalette.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState(initial = false)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Выберите цветовую группу", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // --- Использование ColorGroupButton ---
        Item.Color.ColorGroup.values().forEach { group ->
            ColorGroupButton(
                group = group,
                selected = selectedGroup == group,
                onClick = { selectedGroup = group }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                selectedGroup?.let {
                    viewModel.generateLooksByColor(it)
                    onGenerateLooks()
                }
            },
            enabled = selectedGroup != null && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Создать образы")
            }
        }

        if (colorPalette.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Гармоничная палитра:", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.padding(top = 8.dp)) {
                colorPalette.forEach { colorHex ->
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(ColorUtils.hexToColor(colorHex))
                    )
                }
            }
        }
    }
}