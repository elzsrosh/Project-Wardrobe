package com.example.wardrobecomposer.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TogetherAiApiService @Inject constructor(
    private val client: OkHttpClient
) {
    private val apiUrl = "https://api.together.xyz/v1/chat/completions "
    private val apiKey = "d7661359ac219ffc1cddad935d812498e277e4e56d8b5f6ac17237a9ee5d4ce0"

    suspend fun getStyleAdvice(
        itemName: String,
        type: String? = null,
        material: String? = null,
        style: String? = null,
        color: String? = null
    ): String = withContext(Dispatchers.IO) {
        val prompt = buildString {
            append("Опиши, с чем можно сочетать \"$itemName\" ($type). ")
            append("Учти следующие детали: \n")
            append("- Материал: $material\n")
            append("- Цвет: $color\n")
            append("- Стиль: $style\n")
            append("Приведи краткие примеры образов и ситуаций использования. ")
            append("Все в одну строку. Ответ полностью на русском языке.")
        }

        val message = JSONObject().apply {
            put("role", "user")
            put("content", prompt)
        }

        val jsonBody = JSONObject().apply {
            put("model", "mistralai/Mixtral-8x7B-Instruct-v0.1")
            put("messages", org.json.JSONArray().put(message))
            put("max_tokens", 1024)
            put("temperature", 0.7)
            put("top_p", 0.95)
            put("do_sample", true)
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
                return@withContext "Ошибка от API Together AI"
            }

            val responseObject = JSONObject(result)
            val choices = responseObject.getJSONArray("choices")
            val messageObject = choices.getJSONObject(0).getJSONObject("message")
            val generatedText = messageObject.getString("content")

            // Убираем возможное повторение начала запроса или лишние слова
            generatedText.replaceAfter(":", "").trim()
        } catch (e: Exception) {
            "Не удалось получить совет: ${e.localizedMessage}"
        }
    }
}