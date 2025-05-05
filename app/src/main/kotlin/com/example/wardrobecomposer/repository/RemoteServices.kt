package com.example.wardrobecomposer.repository

import com.example.wardrobecomposer.api.ColorApiService
import javax.inject.Inject

class RemoteServices @Inject constructor(
    private val colorApi: ColorApiService
) {
    suspend fun generateColorPalette(
        baseColor: String? = null,
        paletteType: String? = null
    ): List<String> {
        val request = if (baseColor != null) {
            val rgb = convertHexToRgb(baseColor)
            ColorApiService.ColorRequest(
                input = when (paletteType) {
                    "complementary" -> listOf(rgb, "N", "N", "N", "N")
                    "monochromatic" -> listOf(rgb, rgb, rgb, rgb, rgb)
                    else -> listOf(rgb, "N", "N", "N", "N") // analogous by default
                },
                paletteType = paletteType
            )
        } else {
            ColorApiService.ColorRequest(paletteType = paletteType)
        }

        val response = colorApi.generateColorPalette(request)
        return response.result.take(3).map { rgb ->
            String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2])
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