package com.example.genai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.genai.data.Message

@Composable
fun MessageBubble(message: Message) {
    val isUserMessage = message.author == "user"
    val bubbleColor = if (isUserMessage) {
        Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary))
    } else {
        Brush.horizontalGradient(listOf(Color.LightGray, Color.Gray))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
    ) {
        Column {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(bubbleColor)
                    .padding(16.dp)
            ) {
                Text(
                    text = message.content,
                    color = Color.White
                )
            }
            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(if (isUserMessage) Alignment.End else Alignment.Start).padding(top = 4.dp, start = 8.dp, end = 8.dp)
            )
        }
    }
}