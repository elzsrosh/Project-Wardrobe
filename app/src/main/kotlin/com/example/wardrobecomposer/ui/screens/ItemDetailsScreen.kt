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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.wardrobecomposer.utils.ColorUtils
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    itemId: String,
    viewModel: WardrobeViewModel,
    onBackClick: () -> Unit,
    onGenerateLooks: () -> Unit
) {
    val item by viewModel.selectedItem.collectAsState()
    val colorPalette by viewModel.colorPalette.collectAsState()
    val styleAdvice by viewModel.styleAdvice.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(itemId) {
        viewModel.selectItem(itemId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item?.name ?: "Детали вещи") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (item == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Вещь не найдена")
                }
                return@Scaffold
            }

            if (item?.imageUri?.isNotEmpty() == true) {
                AsyncImage(
                    model = item?.imageUri,
                    contentDescription = item?.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("Категория: ${item?.category?.name}", style = MaterialTheme.typography.titleMedium)
            Text("Материал: ${item?.material?.name}", style = MaterialTheme.typography.titleMedium)
            Text("Стиль: ${item?.style?.name}", style = MaterialTheme.typography.titleMedium)
            Text(
                "Цвет: ${item?.color?.hex} (${item?.color?.colorGroup?.name})",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        item?.let {
                            viewModel.generateColorPalette(it.color.hex)
                            viewModel.getStyleAdvice(it.toString())
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Совет и палитра")
                }

                Button(
                    onClick = {
                        item?.let {
                            viewModel.generateLooksFromItem(it)
                            onGenerateLooks()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Образы")
                }
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (colorPalette.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Сгенерированная палитра:", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colorPalette.forEach { colorHex ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(ColorUtils.hexToColor(colorHex))
                        )
                    }
                }
            }

            if (styleAdvice.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Совет по стилю:", style = MaterialTheme.typography.titleMedium)
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = styleAdvice,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}