@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
package com.example.wardrobecomposer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.ui.screens.AddItemScreen
import com.example.wardrobecomposer.ui.theme.WardrobeComposerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WardrobeComposerTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("wardrobe") { WardrobeScreen(navController, items) }
        composable("generator") { GeneratorScreen { navController.navigateUp() } }
        composable("add_item") {
            AddItemScreen(
                navController = navController,
                onItemAdded = { newItem ->
                    items = items + newItem
                }
            )
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Главное меню",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { navController.navigate("wardrobe") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Мой гардероб",
                style = TextStyle(fontSize = 18.sp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("generator") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Генератор образов",
                style = TextStyle(fontSize = 18.sp)
            )
        }
    }
}

@Composable
fun WardrobeScreen(navController: NavHostController, items: List<Item>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Button(
            onClick = { navController.navigateUp() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            )
        ) {
            Text("Назад", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Мой гардероб",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка добавления новой вещи
        Button(
            onClick = { navController.navigate("add_item") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Добавить вещь",
                style = TextStyle(fontSize = 18.sp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Список добавленных вещей
        if (items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет добавленных вещей",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    ItemCard(item)
                }
            }
        }
    }
}

@Composable
fun ItemCard(item: Item) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Категория: ${item.category.name}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Материал: ${item.material.name}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Стиль: ${item.style.name}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Цвет: ${item.color.colorGroup.name}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun GeneratorScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            )
        ) {
            Text("Назад", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Генератор образов",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.Black
            )
        )
    }
}
