package com.example.wardrobecomposer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wardrobecomposer.data.model.Look

@Composable
fun WardrobeApp(
    onNavigateToFilters: () -> Unit,
    generatedLook: Look?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Wardrobe Composer", fontSize = 30.sp, color = Color(0xFF8E24AA))

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onNavigateToFilters() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
        ) {
            Text("Выбрать фильтры", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        generatedLook?.let { look ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                look.items.forEach { item ->
                    Text("${item.name} (${item.color}, ${item.style}, ${item.material})")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("Цветовая схема: ${look.colorScheme.joinToString()}")
                Spacer(modifier = Modifier.height(10.dp))
                Text("Совет: ${look.styleAdvice}")
            }
        }
    }
}
