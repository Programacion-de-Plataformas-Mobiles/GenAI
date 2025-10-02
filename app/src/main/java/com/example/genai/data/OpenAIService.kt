package com.example.genai.data

import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAIService {
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(@Body request: OpenAIRequest): OpenAIResponse
}
