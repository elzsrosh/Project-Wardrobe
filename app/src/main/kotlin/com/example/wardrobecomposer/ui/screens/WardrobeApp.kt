package com.example.wardrobecomposer.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun WardrobeApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "wardrobe") {
        composable("wardrobe") { WardrobeScreen(navController) }
        composable("filter") { FilterScreen { /* filter handling */ } }
        composable("look/{lookId}") { backStackEntry ->
            val lookId = backStackEntry.arguments?.getString("lookId")
            LookDetailScreen(lookId = lookId)
        }
    }
}