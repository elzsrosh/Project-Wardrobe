package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wardrobecomposer.utils.ColorUtils
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPaletteScreen(
    viewModel: WardrobeViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    // Явное указание типов для collectAsState()
    val colorPalette by viewModel.colorPalette.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState(null)
    val selectedPaletteType by viewModel.selectedPaletteType.collectAsState("")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Цветовая палитра") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Palette type selection
            Text("Тип палитры:", style = MaterialTheme.typography.titleMedium)

            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                viewModel.paletteTypes.forEach { type ->
                    TextButton(
                        onClick = { viewModel.setPaletteType(type) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (selectedPaletteType == type) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    ) {
                        Text(type)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Generate palette button
            Button(
                onClick = { viewModel.generateColorPalette("#B0BEC5") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Сгенерировать палитру")
            }

            if (isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Ошибка",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (colorPalette.isNotEmpty()) {
                Text(
                    text = "Сгенерированная палитра:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Column(modifier = Modifier.padding(top = 8.dp)) {
                    colorPalette.forEach { colorHex ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(android.graphics.Color.parseColor(colorHex)))
                            )
                            Text(
                                text = " $colorHex",
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}