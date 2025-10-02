package com.example.genai.data

class ChatRepository {
    fun getChatHistory(): List<Message> {
        return listOf(
            Message("bot", "Hello! How can I help you?", "12:00 PM"),
            Message("user", "I have a question about my account.", "12:01 PM"),
            Message("bot", "I can help with that. What is your question?", "12:02 PM")
        )
    }
}