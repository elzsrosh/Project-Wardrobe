package com.example.wardrobecomposer.repository

import android.graphics.Color
import com.example.wardrobecomposer.api.ColorApiService
import com.example.wardrobecomposer.api.TheColorApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteServices @Inject constructor(
    private val colorApi: ColorApiService,
    private val theColorApi: TheColorApiService
) {

    // Получение палитры генератора из TheColorApiService
    suspend fun getColorScheme(hex: String, mode: String): List<String> = withContext(Dispatchers.IO) {
        try {
            val response = theColorApi.getColorScheme(hex.removePrefix("#"), mode)
            response.colors.map { it.hex.value }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Получение палитры деталей из ColorApiService
    suspend fun generateColorPalette(input: List<Int>): List<String> = withContext(Dispatchers.IO) {
        try {
            // В ColorApiService метод принимает List<Int> (RGB) в теле запроса
            val request = ColorApiService.ColorRequest(
                model = "default",
                input = input
            )
            val response = colorApi.generateColorPalette(request)

            // Конвертируем List<List<Int>> в HEX строки
            response.result.map { rgbList ->
                String.format("#%02x%02x%02x", rgbList[0], rgbList[1], rgbList[2])
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
