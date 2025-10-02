package com.example.genai.data

data class Message(
    val author: String,
    val content: String,
    val timestamp: String,
    val propertyListings: List<PropertyListing>? = null
)
