package com.example.wardrobecomposer.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HuggingFaceApiService @Inject constructor(
    private val client: OkHttpClient
) {
    private val apiUrl = "https://api-inference.huggingface.co/models/ByteDance-Seed/Seed-Coder-8B-Reasoning"

    private val apiKey = "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"

    suspend fun getStyleAdvice(
        itemName: String,
        type: String? = null,
        material: String? = null,
        style: String? = null
    ): String = withContext(Dispatchers.IO) {
        val prompt = buildString {
            append("Название: $itemName.")
            type?.let { append(" Тип: $it.") }
            material?.let { append(" Материал: $it.") }
            style?.let { append(" Стиль: $it.") }
            append(" С чем это можно сочетать?")
        }

        val jsonBody = JSONObject().apply {
            put("inputs", prompt)
            put("parameters", JSONObject().apply {
                put("max_new_tokens", 1024)
                put("temperature", 0.7)
                put("top_p", 0.95)
                put("do_sample", true)
            })
        }.toString()

        val request = Request.Builder()
            .url(apiUrl)
            .post(jsonBody.toRequestBody("application/json".toMediaType()))
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        try {
            val response = client.newCall(request).execute()
            val result = response.body?.string()

            if (!response.isSuccessful || result == null) {
                return@withContext "Ошибка от API HuggingFace"
            }

            val jsonArray = org.json.JSONArray(result)
            val generatedText = jsonArray.getJSONObject(0).getString("generated_text")

            generatedText.replace(Regex(".*С чем.*?:?", RegexOption.IGNORE_CASE), "").trim()
        } catch (e: Exception) {
            "Не удалось получить совет: ${e.localizedMessage}"
        }
    }
}
