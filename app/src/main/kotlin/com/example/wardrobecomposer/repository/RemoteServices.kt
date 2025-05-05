package com.example.wardrobecomposer.repository

import com.example.wardrobecomposer.api.ColorApiService
import com.example.wardrobecomposer.model.item.Item
import javax.inject.Inject

class RemoteServices @Inject constructor(
    private val colorApi: ColorApiService
) {
    suspend fun generateColorPalette(baseColor: String? = null): List<String> {
        return try {
            val request = if (baseColor != null) {
                val rgb = convertHexToRgb(baseColor)
                ColorApiService.ColorRequest(input = listOf(rgb, "N", "N", "N", "N"))
            } else {
                ColorApiService.ColorRequest()
            }

            val response = colorApi.generateColorPalette(request)
            response.result.take(3).map { rgb -> // Берем первые 3 цвета
                String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2])
            }
        } catch (e: Exception) {
            // Fallback colors
            listOf("#FF5733", "#33FF57", "#3357FF")
        }
    }

    private fun convertHexToRgb(hexColor: String): List<Int> {
        val color = android.graphics.Color.parseColor(hexColor)
        return listOf(
            android.graphics.Color.red(color),
            android.graphics.Color.green(color),
            android.graphics.Color.blue(color)
        )
    }
}