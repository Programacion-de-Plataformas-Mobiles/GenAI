package com.example.genai.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize().imePadding()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
            items(state.messages.reversed()) { message ->
                MessageBubble(message = message)

                // Display property cards if they exist
                message.propertyListings?.let {
                    it.forEach { property ->
                        PropertyCard(property = property)
                    }
                }
            }
        }

        // Show a loading indicator at the bottom while waiting for a response
        if (state.isLoading) {
            TypingIndicator()
        }

        ChatInput { message ->
            viewModel.onEvent(ChatEvent.SendMessage(message))
            coroutineScope.launch {
                if (state.messages.isNotEmpty()) {
                    listState.animateScrollToItem(0)
                }
            }
        }
    }
}