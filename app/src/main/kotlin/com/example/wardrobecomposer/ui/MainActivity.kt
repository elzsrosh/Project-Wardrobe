package com.example.wardrobecomposer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.Look
import com.example.wardrobecomposer.ui.screens.*
import com.example.wardrobecomposer.ui.theme.WardrobeComposerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WardrobeComposerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WardrobeApp()
                }
            }
        }
    }
}

@Composable
fun WardrobeApp() {
    val navController = rememberNavController()
    val viewModel: WardrobeViewModel = hiltViewModel()
    val items: List<Item> by viewModel.items.collectAsState()
    val looks: List<Look> by viewModel.looks.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onWardrobeClick = { navController.navigate("wardrobe") },
                onGeneratorClick = { navController.navigate("generator_options") }
            )
        }

        composable("wardrobe") {
            WardrobeScreen(
                onBackClick = { navController.popBackStack() },
                onAddItemClick = { navController.navigate("add_item") },
                items = items,
                onItemClick = { item ->
                    navController.navigate("item_detail/${item.id}")
                }
            )
        }

        composable("add_item") {
            AddItemScreen(
                navController = navController,
                onItemAdded = { newItem ->
                    viewModel.addItem(newItem)
                    navController.popBackStack()
                }
            )
        }

        composable("generator_options") {
            GeneratorOptionsScreen(
                onBackClick = { navController.popBackStack() },
                onGenerateByColorClick = { navController.navigate("color_palette") },
                onGenerateByItemClick = { navController.navigate("generate_by_item") }
            )
        }

        composable("color_palette") {
            ColorPaletteScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("generate_by_item") {
            GenerateByItemScreen(
                items = items,
                onBackClick = { navController.popBackStack() },
                onGenerate = { item ->
                    viewModel.generateLooksByItem(item)
                    navController.navigate("looks_list")
                }
            )
        }

        composable("looks_list") {
            LooksListScreen(
                looks = looks,
                onBackClick = { navController.popBackStack() },
                onLookClick = { look ->
                    navController.navigate("look_detail/${look.id}")
                }
            )
        }

        composable(
            route = "item_detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            ItemDetailsScreen(
                itemId = itemId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = "look_detail/{lookId}",
            arguments = listOf(navArgument("lookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lookId = backStackEntry.arguments?.getString("lookId") ?: ""
            LookDetailsScreen(
                lookId = lookId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}