package com.example.genai.ui

import com.example.genai.data.Message

sealed class ChatEvent {
    data class SendMessage(val message: String) : ChatEvent()
}

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false
)

sealed class ChatEffect