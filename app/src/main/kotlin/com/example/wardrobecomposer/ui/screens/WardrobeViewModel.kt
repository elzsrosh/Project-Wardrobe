package com.example.wardrobecomposer.ui.screens

import android.util.Log
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

private const val TAG = "WardrobeViewModel"

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
        Log.d(TAG, "Initializing ViewModel")
        loadItems()
    }

    fun loadItems() {
        Log.d(TAG, "Starting to load items")
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                val loadedItems = repository.getAllItems()
                Log.d(TAG, "Loaded ${loadedItems.size} items")
                _items.emit(loadedItems)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading items", e)
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    fun addItem(item: Item) {
        Log.d(TAG, "Adding item: ${item.name}")
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                repository.addItem(item)
                val updatedItems = repository.getAllItems()
                Log.d(TAG, "Added item successfully. Total items: ${updatedItems.size}")
                _items.emit(updatedItems)
            } catch (e: Exception) {
                Log.e(TAG, "Error adding item", e)
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    fun selectItem(itemId: String) {
        Log.d(TAG, "Selecting item with ID: $itemId")
        viewModelScope.launch {
            val item = _items.value.find { it.id == itemId }
            if (item != null) {
                Log.d(TAG, "Selected item: ${item.name}")
            } else {
                Log.w(TAG, "Item not found for ID: $itemId")
            }
            _selectedItem.emit(item)
        }
    }

    fun selectLook(lookId: String) {
        Log.d(TAG, "Selecting look with ID: $lookId")
        viewModelScope.launch {
            val look = _looks.value.find { it.id == lookId }
            if (look != null) {
                Log.d(TAG, "Selected look: ${look.name}")
            } else {
                Log.w(TAG, "Look not found for ID: $lookId")
            }
            _selectedLook.emit(look)
        }
    }

    fun generateLooksFromItem(item: Item) {
        Log.d(TAG, "Generating looks for item: ${item.name}")
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                val generatedLooks = repository.generateLooksFromItem(item)
                Log.d(TAG, "Generated ${generatedLooks.size} looks")
                _looks.emit(generatedLooks)
            } catch (e: Exception) {
                Log.e(TAG, "Error generating looks", e)
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    fun generateLooksByColor(colorGroup: Item.Color.ColorGroup) {
        Log.d(TAG, "Generating looks by color group: $colorGroup")
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                val looks = repository.generateLooksByColor(colorGroup)
                Log.d(TAG, "Generated ${looks.size} looks by color group")
                _looks.emit(looks)
            } catch (e: Exception) {
                Log.e(TAG, "Error generating looks by color", e)
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    fun generateColorPalette(baseColorHex: String, mode: String) {
        Log.d(TAG, "Generating color palette with hex: $baseColorHex, mode: $mode")
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                val palette = repository.generateColorPalette(baseColorHex, mode)
                Log.d(TAG, "Generated color palette: $palette")
                _colorPalette.emit(palette)
            } catch (e: Exception) {
                Log.e(TAG, "Error generating color palette", e)
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    fun getStyleAdvice(
        itemName: String,
        type: String? = null,
        material: String? = null,
        style: String? = null,
        color: String? = null
    ) {
        Log.d(TAG, "Fetching style advice for item: $itemName")
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            try {
                val advice = repository.getStyleAdvice(itemName, type, material, style, color)
                Log.d(TAG, "Received style advice: $advice")
                _styleAdvice.emit(advice)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching style advice", e)
                _styleAdvice.emit("Ошибка получения совета: ${e.message}")
            } finally {
                _isLoading.emit(false)
            }
        }
    }
}