@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Suppress("ktlint:standard:function-naming")
@Composable
fun HomeScreen(
    onWardrobeClick: () -> Unit,
    onGeneratorClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = onWardrobeClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
        ) {
            Text("Мой гардероб")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onGeneratorClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
        ) {
            Text("Генератор образов")
        }
    }
}
