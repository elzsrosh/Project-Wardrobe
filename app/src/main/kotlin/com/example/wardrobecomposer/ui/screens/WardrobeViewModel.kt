package com.example.wardrobecomposer.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.Look
import com.example.wardrobecomposer.repository.WardrobeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WardrobeViewModel @Inject constructor(
    private val repository: WardrobeRepository
) : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()

    private val _looks = MutableStateFlow<List<Look>>(emptyList())
    val looks: StateFlow<List<Look>> = _looks.asStateFlow()

    private val _colorPalette = MutableStateFlow<List<String>>(emptyList())
    val colorPalette: StateFlow<List<String>> = _colorPalette.asStateFlow()

    private val _styleAdvice = MutableStateFlow("")
    val styleAdvice: StateFlow<String> = _styleAdvice.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedItem = MutableStateFlow<Item?>(null)
    val selectedItem: StateFlow<Item?> = _selectedItem.asStateFlow()

    private val _selectedLook = MutableStateFlow<Look?>(null)
    val selectedLook: StateFlow<Look?> = _selectedLook.asStateFlow()

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _items.value = repository.getAllItems()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.addItem(item)
                _items.update { repository.getAllItems() }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectItem(itemId: String) {
        viewModelScope.launch {
            _selectedItem.value = _items.value.find { it.id == itemId }
        }
    }

    fun selectLook(lookId: String) {
        viewModelScope.launch {
            _selectedLook.value = _looks.value.find { it.id == lookId }
        }
    }

    fun generateLooksFromItem(item: Item) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _looks.value = repository.generateLooksFromItem(item)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generateLooksByColor(colorGroup: Item.Color.ColorGroup) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _looks.value = repository.generateLooksByColor(colorGroup)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generateColorPalette(baseColor: String, paletteType: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _colorPalette.value = repository.generateColorPalette(baseColor, paletteType)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getStyleAdvice(itemName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _styleAdvice.value = repository.getStyleAdvice(itemName)
            } finally {
                _isLoading.value = false
            }
        }
    }
}