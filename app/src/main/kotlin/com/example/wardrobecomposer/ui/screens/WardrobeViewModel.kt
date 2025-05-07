package com.example.wardrobecomposer.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.Item.Color.ColorGroup
import com.example.wardrobecomposer.model.item.Look
import com.example.wardrobecomposer.repository.RemoteServices
import com.example.wardrobecomposer.repository.WardrobeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WardrobeViewModel
    @Inject
    constructor(
        private val repository: WardrobeRepository,
        private val remoteServices: RemoteServices,
    ) : ViewModel() {
        private val _items = MutableStateFlow<List<Item>>(emptyList())
        val items: StateFlow<List<Item>> = _items.asStateFlow()

        private val _looks = MutableStateFlow<List<Look>>(emptyList())
        val looks: StateFlow<List<Look>> = _looks.asStateFlow()

        private val _colorPalette = MutableStateFlow<List<String>>(emptyList())
        val colorPalette: StateFlow<List<String>> = _colorPalette.asStateFlow()

        private val _selectedPaletteType = MutableStateFlow("analogous")
        val selectedPaletteType: StateFlow<String> = _selectedPaletteType.asStateFlow()

        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

        private val _errorMessage = MutableStateFlow<String?>(null)
        val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

        val paletteTypes =
            listOf(
                "analogous" to "Аналогичная",
                "complementary" to "Комплементарная",
                "monochromatic" to "Монохроматическая",
            )

        init {
            loadItems()
        }

        private fun loadItems() {
            viewModelScope.launch {
                _items.value = repository.getAllItems()
            }
        }

        fun addItem(item: Item) {
            viewModelScope.launch {
                repository.addItem(item)
                _items.value = _items.value + item
            }
        }

        fun setPaletteType(type: String) {
            _selectedPaletteType.value = type
        }

        fun generateColorPalette(baseColor: String) {
            _isLoading.value = true
            _errorMessage.value = null
            viewModelScope.launch {
                try {
                    _colorPalette.value = remoteServices.generateColorPalette(baseColor, _selectedPaletteType.value)
                } catch (e: Exception) {
                    _errorMessage.value = "Ошибка при генерации палитры: ${e.localizedMessage}"
                    _colorPalette.value = emptyList()
                } finally {
                    _isLoading.value = false
                }
            }
        }

        fun generateLooksByColor(colorGroup: ColorGroup) {
            viewModelScope.launch {
                _looks.value = repository.generateLooks(colorScheme = createColorScheme(colorGroup))
            }
        }

        fun generateLooksByItem(item: Item) {
            viewModelScope.launch {
                _looks.value = repository.generateLooks(baseItem = item)
            }
        }

        private fun createColorScheme(colorGroup: ColorGroup): Look.ColorScheme =
            Look.ColorScheme(
                primaryColor = Item.Color(colorPalette.value[0], colorGroup),
                secondaryColors = colorPalette.value.drop(1).map { Item.Color(it, colorGroup) },
                isComplementary = selectedPaletteType.value == "complementary",
                isAnalogous = selectedPaletteType.value == "analogous",
                isMonochromatic = selectedPaletteType.value == "monochromatic",
            )
    }
