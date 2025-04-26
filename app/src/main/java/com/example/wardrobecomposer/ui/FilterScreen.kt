package com.example.wardrobecomposer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterScreen(onFiltersSelected: (String, String, String) -> Unit) {
    var colorInput by remember { mutableStateOf(TextFieldValue("")) }
    var styleInput by remember { mutableStateOf(TextFieldValue("")) }
    var materialInput by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Выберите параметры", fontSize = 26.sp, color = Color(0xFF8E24AA))

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = colorInput,
            onValueChange = { colorInput = it },
            placeholder = { Text("Цвет") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = styleInput,
            onValueChange = { styleInput = it },
            placeholder = { Text("Стиль (кэжуал, офисный...)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = materialInput,
            onValueChange = { materialInput = it },
            placeholder = { Text("Материал (хлопок, шерсть...)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onFiltersSelected(
                    colorInput.text,
                    styleInput.text,
                    materialInput.text
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
        ) {
            Text("Сгенерировать образ", color = Color.White)
        }
    }
}
