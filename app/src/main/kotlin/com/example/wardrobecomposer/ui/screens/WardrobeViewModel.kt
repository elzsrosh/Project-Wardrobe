package com.example.wardrobecomposer.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.Look
import com.example.wardrobecomposer.repository.WardrobeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WardrobeViewModel @Inject constructor(
    private val repository: WardrobeRepository
) : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    private val _looks = MutableStateFlow<List<Look>>(emptyList())
    val looks: StateFlow<List<Look>> = _looks

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

    fun generateLooksByColor(colorGroup: Item.Color.ColorGroup) {
        viewModelScope.launch {
            _looks.value = repository.generateLooks(colorScheme = Look.ColorScheme(
                primaryColor = Item.Color("#000000", colorGroup),
                secondaryColors = emptyList(),
                isComplementary = false,
                isAnalogous = true,
                isMonochromatic = false
            ))
        }
    }

    fun generateLooksByItem(item: Item) {
        viewModelScope.launch {
            _looks.value = repository.generateLooks(baseItem = item)
        }
    }
}