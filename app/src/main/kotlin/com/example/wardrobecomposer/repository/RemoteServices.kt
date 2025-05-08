package com.example.wardrobecomposer.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.wardrobecomposer.api.ColorApiService
import com.example.wardrobecomposer.api.ImageUploadService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class RemoteServices
    @Inject
    constructor(
        private val colorApi: ColorApiService,
        private val imageUploadService: ImageUploadService,
    ) {
        suspend fun generateColorPalette(
            baseColor: String? = null,
            paletteType: String? = null,
        ): List<String> {
            val request =
                if (baseColor != null) {
                    val rgb = convertHexToRgb(baseColor)
                    ColorApiService.ColorRequest(input = listOf(rgb, "N", "N", "N", "N"), paletteType = paletteType)
                } else {
                    ColorApiService.ColorRequest(paletteType = paletteType)
                }

            return try {
                val response = colorApi.generateColorPalette(request)
                response.result.take(5).map { rgb ->
                    String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2])
                }
            } catch (e: Exception) {
                emptyList()
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

        // Загружаем изображение по URL
        suspend fun loadImageFromUrl(url: String): ByteArray? =
            withContext(Dispatchers.IO) {
                try {
                    val inputStream = java.net.URL(url).openStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                    stream.toByteArray()
                } catch (e: Exception) {
                    null
                }
            }

        // Отправляем изображение на внешний API
        suspend fun uploadItemImage(imageData: ByteArray): String? =
            withContext(Dispatchers.IO) {
                try {
                    val requestFile = imageData.toRequestBody("image/*".toMediaType())

                    val body =
                        MultipartBody.Part.createFormData(
                            name = "file",
                            filename = "item_image.jpg",
                            body = requestFile,
                        )

                    val response = imageUploadService.uploadImage(body)
                    response.imageUrl
                } catch (e: Exception) {
                    null
                }
            }
    }
