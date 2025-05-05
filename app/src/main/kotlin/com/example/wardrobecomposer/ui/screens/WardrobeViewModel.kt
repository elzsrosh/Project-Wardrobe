package com.example.wardrobecomposer.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.Look
import com.example.wardrobecomposer.repository.RemoteServices
import com.example.wardrobecomposer.repository.WardrobeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WardrobeViewModel @Inject constructor(
    private val repository: WardrobeRepository,
    private val remoteServices: RemoteServices
) : ViewModel() {

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    private val _looks = MutableStateFlow<List<Look>>(emptyList())
    val looks: StateFlow<List<Look>> = _looks

    private val _colorPalette = MutableStateFlow<List<String>>(emptyList())
    val colorPalette: StateFlow<List<String>> = _colorPalette

    // Добавляем состояние для выбора схемы
    private val _selectedScheme = MutableStateFlow("analogous")
    val selectedScheme: StateFlow<String> = _selectedScheme

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

    fun generateColorPalette(baseColor: String) {
        viewModelScope.launch {
            _colorPalette.value = remoteServices.generateColorPalette(baseColor)
        }
    }

    fun setColorScheme(scheme: String) {
        _selectedScheme.value = scheme
    }

    fun generateLooksByColor(colorGroup: Item.Color.ColorGroup) {
        viewModelScope.launch {
            if (colorPalette.value.size >= 3) {
                val schemeType = selectedScheme.value

                _looks.value = repository.generateLooks(
                    colorScheme = Look.ColorScheme(
                        primaryColor = Item.Color(colorPalette.value[0], colorGroup),
                        isAnalogous = schemeType == "analogous",
                        isComplementary = schemeType == "complementary",
                        isMonochromatic = schemeType == "monochromatic",
                        secondaryColors = colorPalette.value.drop(1).map {
                            Item.Color(it, colorGroup)
                        }
                    )
                )
            }
        }
    }

    fun generateLooksByItem(item: Item) {
        viewModelScope.launch {
            _looks.value = repository.generateLooks(baseItem = item)
        }
    }
}