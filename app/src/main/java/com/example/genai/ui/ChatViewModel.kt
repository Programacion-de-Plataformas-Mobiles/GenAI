package com.example.genai.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genai.data.Message
import com.example.genai.data.OpenAIRepository
import com.example.genai.data.OpenAIMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val openAIRepository = OpenAIRepository()

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
             _state.value = _state.value.copy(isLoading = true)
            val baymaxResponse = openAIRepository.getBaymaxStructuredResponse(emptyList())
            if (baymaxResponse != null) {
                val baymaxGreeting = Message("bot", baymaxResponse.spokenResponse, "now")
                _state.value = ChatState(messages = listOf(baymaxGreeting))
            } else {
                val errorMessage = Message("bot", "Parece que hay un problema de conexión.", "now")
                _state.value = ChatState(messages = listOf(errorMessage))
            }
             _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SendMessage -> {
                sendMessage(event.message)
            }
        }
    }

    private fun sendMessage(userInput: String) {
        viewModelScope.launch {
            val currentUserMessage = Message("user", userInput, "now")
            val currentMessages = _state.value.messages
            _state.value = _state.value.copy(messages = currentMessages + currentUserMessage, isLoading = true)

            val history = (currentMessages + currentUserMessage).takeLast(10).map {
                val role = if (it.author == "bot") "assistant" else it.author
                OpenAIMessage(role = role, content = it.content)
            }

            val baymaxResponse = openAIRepository.getBaymaxStructuredResponse(history)

            if (baymaxResponse != null) {
                Log.d("BaymaxSentimentAnalysis", "Detected sentiment: ${baymaxResponse.userSentiment}")

                val newBotMessage = Message(
                    author = "bot",
                    content = baymaxResponse.spokenResponse,
                    timestamp = "now",
                    recommendation = baymaxResponse.recommendation // Adjuntar la recomendación
                )

                _state.value = _state.value.copy(
                    messages = _state.value.messages + newBotMessage,
                    isLoading = false
                )
            } else {
                val errorMessage = Message("bot", "Lo siento, no pude procesar tu solicitud.", "now")
                _state.value = _state.value.copy(
                    messages = _state.value.messages + errorMessage,
                    isLoading = false
                )
            }
        }
    }
}