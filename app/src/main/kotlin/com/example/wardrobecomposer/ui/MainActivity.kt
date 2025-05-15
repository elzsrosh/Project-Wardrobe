package com.example.wardrobecomposer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wardrobecomposer.ui.screens.*
import com.example.wardrobecomposer.ui.theme.WardrobeComposerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    )

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* Можно обработать результаты, если нужно */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissions.any {
                    ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
                }) {
                requestPermissionLauncher.launch(permissions)
            }
        }

        setContent {
            WardrobeComposerTheme {
                WardrobeApp()
            }
        }
    }
}

@Composable
fun WardrobeApp() {
    val navController = rememberNavController()
    val viewModel: WardrobeViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        composable("home") {
            HomeScreen(
                onWardrobeClick = { navController.navigate("wardrobe") },
                onGeneratorClick = { navController.navigate("generator_options") }
            )
        }

        composable("wardrobe") {
            WardrobeScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onAddItemClick = { navController.navigate("add_item") },
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
            GenerateByColorScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("generate_by_item") {
            GenerateOutfitScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onGenerateLooks = { item ->
                    viewModel.generateLooksFromItem(item)
                    navController.navigate("looks_list")
                },
                onAddNewItemClick = { navController.navigate("add_item") }
            )
        }

        composable("looks_list") {
            LooksListScreen(
                viewModel = viewModel,
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
            val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
            ItemDetailsScreen(
                itemId = itemId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onGenerateLooks = {
                    navController.navigate("looks_list")
                }
            )
        }

        composable(
            route = "look_detail/{lookId}",
            arguments = listOf(navArgument("lookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lookId = backStackEntry.arguments?.getString("lookId") ?: return@composable
            LookDetailScreen(
                lookId = lookId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}