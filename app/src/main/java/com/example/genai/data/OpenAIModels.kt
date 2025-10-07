package com.example.genai.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenAIRequest(
    @Json(name = "model") val model: String = "gpt-4o",
    @Json(name = "messages") val messages: List<OpenAIMessage>,
    @Json(name = "response_format") val responseFormat: ResponseFormat = ResponseFormat()
)

@JsonClass(generateAdapter = true)
data class OpenAIMessage(
    @Json(name = "role") val role: String,
    @Json(name = "content") val content: String
)

@JsonClass(generateAdapter = true)
data class ResponseFormat(
    @Json(name = "type") val type: String = "json_object"
)

@JsonClass(generateAdapter = true)
data class OpenAIResponse(
    @Json(name = "choices") val choices: List<Choice>
)

@JsonClass(generateAdapter = true)
data class Choice(
    @Json(name = "message") val message: OpenAIMessage
)

@JsonClass(generateAdapter = true)
data class BaymaxResponse(
    @Json(name = "spoken_response") val spokenResponse: String,
    @Json(name = "user_sentiment") val userSentiment: String,
    @Json(name = "recommendation") val recommendation: MedicalRecommendation? = null
)

@JsonClass(generateAdapter = true)
data class MedicalRecommendation(
    @Json(name = "item_name") val itemName: String,
    @Json(name = "estimated_price_gtq") val priceGTQ: Double
    // Se ha eliminado imageUrl
)
