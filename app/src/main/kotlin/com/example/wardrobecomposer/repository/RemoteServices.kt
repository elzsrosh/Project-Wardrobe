package com.example.wardrobecomposer.repository

import com.example.wardrobecomposer.api.ColorApiService
import javax.inject.Inject

class RemoteServices
    @Inject
    constructor(
        private val colorApi: ColorApiService,
    ) {
        suspend fun generateColorPalette(
            baseColor: String? = null,
            paletteType: String? = null,
        ): List<String> {
            val request =
                if (baseColor != null) {
                    val rgb = convertHexToRgb(baseColor)
                    ColorApiService.ColorRequest(
                        input = listOf(rgb, "N", "N", "N", "N"),
                        paletteType = paletteType,
                    )
                } else {
                    ColorApiService.ColorRequest(paletteType = paletteType)
                }

            return try {
                val response = colorApi.generateColorPalette(request)
                response.result.take(5).map { rgb ->
                    String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2])
                }
            } catch (e: Exception) {
                emptyList() // Возвращаем пустой список в случае ошибки
            }
        }

        private fun convertHexToRgb(hexColor: String): List<Int> {
            val color = android.graphics.Color.parseColor(hexColor)
            return listOf(
                android.graphics.Color.red(color),
                android.graphics.Color.green(color),
                android.graphics.Color.blue(color),
            )
        }
    }
