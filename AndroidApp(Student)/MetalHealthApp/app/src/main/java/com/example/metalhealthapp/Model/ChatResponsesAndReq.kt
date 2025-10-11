package com.example.metalhealthapp.Model

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val sessionId: String,
    val userMessage: String
)

@Serializable
data class ChatResponse(
    val reply: String
)

@Serializable
data class EndSessionRequest(
    val userId: String,
    val sessionId: String
)

