package com.example.wardrobecomposer.api

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageUploadService {
    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part("file") file: MultipartBody.Part,
    ): ImageUploadResponse
}

data class ImageUploadResponse(
    val imageUrl: String,
)
