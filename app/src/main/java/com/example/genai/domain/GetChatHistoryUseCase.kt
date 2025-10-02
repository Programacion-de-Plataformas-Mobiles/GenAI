package com.example.genai.domain

import com.example.genai.data.ChatRepository
import com.example.genai.data.Message

class GetChatHistoryUseCase(private val repository: ChatRepository) {
    fun execute(): List<Message> = repository.getChatHistory()
}