package com.example.wardrobecomposer.repository

import android.graphics.Color
import com.example.wardrobecomposer.api.ColorApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteServices @Inject constructor(
    private val colorApi: ColorApiService
) {
    suspend fun generateColorPalette(baseColor: String? = null, paletteType: String? = null): List<String> =
        withContext(Dispatchers.IO) {
            try {
                val request = if (baseColor != null) {
                    val rgb = convertHexToRgb(baseColor)
                    ColorApiService.ColorRequest(input = listOf(rgb[0], rgb[1], rgb[2], "N", "N"), paletteType = paletteType)
                } else {
                    ColorApiService.ColorRequest(paletteType = paletteType)
                }

                val response = colorApi.generateColorPalette(request)

                // Преобразуем каждый RGB-массив в HEX строку
                response.result.take(10).map { rgb ->
                    String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2])
                }
            } catch (e: Exception) {
                emptyList()
            }
        }

    private fun convertHexToRgb(hexColor: String): List<Int> {
        val parsedColor = Color.parseColor(hexColor)
        return listOf(
            Color.red(parsedColor),
            Color.green(parsedColor),
            Color.blue(parsedColor)
        )
    }
}