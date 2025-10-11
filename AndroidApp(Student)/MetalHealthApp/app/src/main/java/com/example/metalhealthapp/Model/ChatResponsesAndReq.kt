package com.example.metalhealthapp.Model

data class ChatRequest(
    val sessionId: String,
    val userMessage: String
)

data class ChatResponse(
    val reply: String
)


data class EndSessionRequest(
    val userId: String,
    val sessionId: String
)