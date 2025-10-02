package com.example.genai.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// --- Request Models (for Retrofit with Moshi) ---

@JsonClass(generateAdapter = true)
data class OpenAIRequest(
    @Json(name = "model") val model: String = "gpt-3.5-turbo-1106",
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

// --- Response Models (for Retrofit with Moshi) ---

// This is the outer shell of the API's response
@JsonClass(generateAdapter = true)
data class OpenAIResponse(
    @Json(name = "choices") val choices: List<Choice>
)

@JsonClass(generateAdapter = true)
data class Choice(
    @Json(name = "message") val message: OpenAIMessage
)
