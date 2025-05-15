package com.example.wardrobecomposer.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobecomposer.model.item.Item
import com.example.wardrobecomposer.model.item.Look
import com.example.wardrobecomposer.repository.WardrobeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WardrobeViewModel @Inject constructor(
    private val repository: WardrobeRepository
) : ViewModel() {
    // --- Основные потоки данных ---
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    private val _looks = MutableStateFlow<List<Look>>(emptyList())
    val looks: StateFlow<List<Look>> = _looks

    private val _colorPalette = MutableStateFlow<List<String>>(emptyList())
    val colorPalette: StateFlow<List<String>> = _colorPalette

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _styleAdvice = MutableStateFlow("Загрузка...")
    val styleAdvice: StateFlow<String> = _styleAdvice

    // --- Новые поля для дополнительного функционала ---
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _selectedPaletteType = MutableStateFlow("")
    val selectedPaletteType: StateFlow<String> = _selectedPaletteType

    val paletteTypes = listOf("complementary", "analogous", "monochromatic")

    // --- Методы для работы с цветовой палитрой ---
    fun setPaletteType(type: String) {
        _selectedPaletteType.value = type
    }

    fun generateColorPalette(baseColor: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _colorPalette.value = repository.generateColorPalette(baseColor)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка генерации палитры: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- Методы для работы с советами по стилю ---
    fun getStyleAdvice(itemName: String) {
        viewModelScope.launch {
            try {
                val advice = repository.getStyleAdvice(itemName)
                _styleAdvice.value = advice
                _errorMessage.value = null
            } catch (e: Exception) {
                _styleAdvice.value = "Ошибка загрузки совета"
                _errorMessage.value = "Не удалось получить совет: ${e.message}"
            }
        }
    }

    // --- Методы для работы с предметами гардероба ---
    fun addItem(item: Item) {
        viewModelScope.launch {
            try {
                repository.addItem(item)
                _items.value = repository.getAllItems()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка добавления предмета: ${e.message}"
            }
        }
    }

    fun refreshItems() {
        viewModelScope.launch {
            try {
                _items.value = repository.getAllItems()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка обновления списка: ${e.message}"
            }
        }
    }

    // --- Методы для генерации образов ---
    fun generateLooksFromItem(item: Item) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _looks.value = repository.generateLooksFromItem(item)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка генерации образа: ${e.message}"
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
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка генерации по цвету: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- Инициализация данных ---
    init {
        viewModelScope.launch {
            _items.value = repository.getAllItems()
        }
    }
}