package com.example.wardrobecomposer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.wardrobecomposer.data.model.Look
import com.example.wardrobecomposer.data.repository.WardrobeRepository
import com.example.wardrobecomposer.ui.FilterScreen
import com.example.wardrobecomposer.ui.WardrobeApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentScreen by remember { mutableStateOf("main") }
            var generatedLook by remember { mutableStateOf<Look?>(null) }

            if (currentScreen == "main") {
                WardrobeApp(
                    onNavigateToFilters = { currentScreen = "filters" },
                    generatedLook = generatedLook
                )
            } else {
                FilterScreen(
                    onFiltersSelected = { color: String, style: String, material: String ->
                        CoroutineScope(Dispatchers.Main).launch {
                            generatedLook = WardrobeRepository.generateLookByFilters(color, style, material)
                            currentScreen = "main"
                        }
                    }
                )
            }
        }
    }
}
