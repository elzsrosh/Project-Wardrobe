package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wardrobecomposer.model.item.Look
import com.example.wardrobecomposer.ui.theme.WardrobeComposerTheme
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LooksListScreen(
    viewModel: WardrobeViewModel,
    onBackClick: () -> Unit,
    onLookClick: (Look) -> Unit
) {
    val looks by viewModel.looks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    WardrobeComposerTheme {
        Scaffold(
            modifier = Modifier.background(Color(0xFFFFF0F4)),
            topBar = {
                TopAppBar(
                    title = { Text("Сгенерированные образы", color = Color(0xFFC2185B)) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = Color(0xFFC2185B))
                        }
                    }
                )
            }
        ) { padding ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color(0xFFFFF0F4)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFC2185B))
                }
            } else if (looks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color(0xFFFFF0F4)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет сгенерированных образов", color = Color(0xFFC2185B))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color(0xFFFFF0F4)),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(looks) { look ->
                        LookCard(
                            look = look,
                            onClick = { onLookClick(look) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LookCard(
    look: Look,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(look.name, style = MaterialTheme.typography.titleLarge, color = Color(0xFFC2185B))
            Spacer(modifier = Modifier.height(8.dp))

            Text("Стиль: ${look.style.name}", color = Color(0xFFC2185B))
            Text("Цветовая схема: ${look.colorScheme.primaryColor.colorGroup.name}", color = Color(0xFFC2185B))

            if (look.compatibilityReason.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(look.compatibilityReason, color = Color(0xFFC2185B))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Вещи в образе:", style = MaterialTheme.typography.titleMedium, color = Color(0xFFC2185B))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                look.items.forEach { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(100.dp)
                    ) {
                        AsyncImage(
                            model = item.imageUri,
                            contentDescription = item.name,
                            modifier = Modifier.size(80.dp)
                        )
                        Text(item.name, maxLines = 1, color = Color(0xFFC2185B))
                    }
                }
            }
        }
    }
}