package com.example.genai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier, // Este modifier YA contiene el padding del Scaffold
    viewModel: ChatViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    // Aplicamos el modifier del Scaffold a la Columna principal
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding() // Y añadimos el padding para el teclado
    ) {
        // La lista de mensajes ocupa todo el espacio disponible, encogiéndose cuando aparece el teclado
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f)
        ) {
            items(state.messages) { message ->
                MessageBubble(message = message)
                message.recommendation?.let {
                    MedicalRecommendationCard(recommendation = it)
                }
            }
        }

        if (state.isLoading && state.messages.isNotEmpty()) {
            TypingIndicator()
        }

        ChatInput { message ->
            viewModel.onEvent(ChatEvent.SendMessage(message))
        }
    }
}