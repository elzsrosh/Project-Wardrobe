package com.example.wardrobecomposer.ui.screens

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wardrobecomposer.model.item.Item

@Composable
fun WardrobeApp() {
    val navController = rememberNavController()
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }

    NavHost(navController = navController, startDestination = "wardrobe") {
        composable("wardrobe") {
            WardrobeScreen(navController, items)
        }
        composable("filter") { FilterScreen { /* filter handling */ } }
        composable("look/{lookId}") { backStackEntry ->
            val lookId = backStackEntry.arguments?.getString("lookId")
            LookDetailScreen(lookId = lookId)
        }
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
