package com.example.wardrobecomposer.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HuggingFaceApiService @Inject constructor(
    private val client: OkHttpClient
) {
    private val apiUrl = "https://api-inference.huggingface.co/models/gpt2"
    private val apiKey = "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"

    suspend fun getStyleAdvice(item: String): String = withContext(Dispatchers.IO) {
        val prompt = "С чем можно сочетать $item? Ответь кратко и по делу."

        val requestBody = JSONObject().apply {
            put("inputs", prompt)
        }.toString().toRequestBody()

        val request = Request.Builder()
            .url(apiUrl)
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            parseResponse(responseBody)
        } catch (e: Exception) {
            "Не удалось получить совет."
        }
    }

    private fun parseResponse(response: String): String {
        return try {
            val jsonArray = org.json.JSONArray(response)
            jsonArray.getJSONObject(0).getString("generated_text")
                .replace(Regex("С чем можно сочетать.*?:?", RegexOption.IGNORE_CASE), "")
                .trim()
        } catch (e: Exception) {
            "Не удалось обработать ответ."
        }
    }
}