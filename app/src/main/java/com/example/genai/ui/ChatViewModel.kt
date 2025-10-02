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
        // Iniciar la conversación con un saludo en español
        val startingMessage = Message("bot", "¡Hola! Soy tu agente inmobiliario virtual. ¿Qué tipo de propiedad estás buscando hoy?", "now")
        _state.value = ChatState(messages = listOf(startingMessage))
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

            // Preparar el historial de la conversación para la API
            val history = (currentMessages + currentUserMessage)
                .takeLast(10) // Tomar los últimos 10 mensajes
                .map {
                    val role = if (it.author == "bot") "assistant" else it.author
                    OpenAIMessage(role = role, content = it.content)
                }

            // Obtener la respuesta estructurada de OpenAI
            val realEstateResponse = openAIRepository.getStructuredChatCompletion(history)

            if (realEstateResponse != null) {
                Log.d("OpenAIStructuredResponse", realEstateResponse.toString())

                // Crear un nuevo mensaje del bot con el resumen y la lista de propiedades
                val newBotMessage = Message(
                    author = "bot",
                    content = realEstateResponse.agentSummary,
                    timestamp = "now",
                    propertyListings = realEstateResponse.propertyListings
                )

                _state.value = _state.value.copy(
                    messages = _state.value.messages + newBotMessage,
                    isLoading = false
                )
            } else {
                val errorMessage = Message("bot", "Lo siento, no pude procesar tu solicitud. Por favor, inténtalo de nuevo.", "now")
                _state.value = _state.value.copy(
                    messages = _state.value.messages + errorMessage,
                    isLoading = false
                )
            }
        }
    }
}