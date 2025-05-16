package com.example.wardrobecomposer.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.Look
import com.example.wardrobecomposer.repository.WardrobeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                val loadedItems = repository.getAllItems()
                _items.emit(loadedItems)
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                repository.addItem(item)
                _items.emit(repository.getAllItems())
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    fun selectItem(itemId: String) {
        viewModelScope.launch {
            _selectedItem.emit(_items.value.find { it.id == itemId })
        }
    }

    fun selectLook(lookId: String) {
        viewModelScope.launch {
            _selectedLook.emit(_looks.value.find { it.id == lookId })
        }
    }

    fun generateLooksFromItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                val generatedLooks = repository.generateLooksFromItem(item)
                _looks.emit(generatedLooks)
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    fun generateLooksByColor(colorGroup: Item.Color.ColorGroup) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                val looks = repository.generateLooksByColor(colorGroup)
                _looks.emit(looks)
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    // Используем режим mode (например, "complement", "analogic" и т.д.)
    fun generateColorPalette(baseColorHex: String, mode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                val palette = repository.generateColorPalette(baseColorHex, mode)
                _colorPalette.emit(palette)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    fun getStyleAdvice(itemName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                val advice = repository.getStyleAdvice(itemName)
                _styleAdvice.emit(advice)
            } finally {
                _isLoading.emit(false)
            }
        }
    }
}
