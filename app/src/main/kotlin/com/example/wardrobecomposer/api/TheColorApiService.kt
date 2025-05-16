package com.example.wardrobecomposer.api

import retrofit2.http.GET
import retrofit2.http.Query

interface TheColorApiService {

    @GET("scheme")
    suspend fun getColorScheme(
        @Query("hex") hex: String,
        @Query("mode") mode: String = "analogic",
        @Query("count") count: Int = 5
    ): ColorSchemeResponse

    data class ColorSchemeResponse(
        val colors: List<ColorInfo>
    )

    data class ColorInfo(
        val hex: Hex
    )

    data class Hex(
        val value: String
    )
}
