@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.wardrobecomposer.ui.theme.WardrobeComposerTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun HomeScreen(
    onWardrobeClick: () -> Unit,
    onGeneratorClick: () -> Unit,
) {
    WardrobeComposerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF0F4))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = onWardrobeClick,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("МОЙ ГАРДЕРОБ", color = Color(0xFFC2185B))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGeneratorClick,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("ГЕНЕРАТОР ОБРАЗОВ", color = Color(0xFFC2185B))
            }
        }
    }
}