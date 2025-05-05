package com.example.wardrobecomposer.api

import retrofit2.http.Body
import retrofit2.http.POST

interface ColorApiService {
    @POST("api/")
    suspend fun generateColorPalette(
        @Body request: ColorRequest
    ): ColorPaletteResponse

    data class ColorRequest(
        val model: String = "default",
        val input: List<Any>? = null, // [R,G,B] или "N" для случайного
        val paletteType: String? = null // "analogous", "complementary", "monochromatic"
    )

    data class ColorPaletteResponse(
        val result: List<List<Int>> // [[R,G,B], [R,G,B], [R,G,B], [R,G,B], [R,G,B]]
    )
}