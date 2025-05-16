package com.example.wardrobecomposer.repository

import com.example.wardrobecomposer.api.TheColorApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteServices @Inject constructor(
    private val theColorApi: TheColorApiService
) {

    // Получение палитры из TheColorApiService
    suspend fun getColorScheme(hex: String, mode: String): List<String> = withContext(Dispatchers.IO) {
        try {
            val response = theColorApi.getColorScheme(hex.removePrefix("#"), mode)
            response.colors.map { it.hex.value }
        } catch (e: Exception) {
            emptyList()
        }
    }
}