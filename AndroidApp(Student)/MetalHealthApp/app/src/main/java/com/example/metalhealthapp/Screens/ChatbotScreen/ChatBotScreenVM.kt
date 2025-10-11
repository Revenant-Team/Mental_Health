package com.example.metalhealthapp.Screens.ChatbotScreen

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metalhealthapp.Model.ChatRequest
import com.example.metalhealthapp.Repo.Repo
import com.example.metalhealthapp.Utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Message(
    val role: String,
    val content: String
)
@HiltViewModel
class ChatBotScreenVM @Inject constructor(private val repo: Repo) : ViewModel() {
    var messages = mutableStateListOf<Message>() // Message(role, content)
        private set

    val isLoading = mutableStateOf(false)

    fun sendMessage(context: Context,sessionId: String, message: String,onSuccess : ()->Unit, onFailure : (String)->Unit) {
        val token = TokenManager(context).getToken()
        if(token != null){
            viewModelScope.launch {
                isLoading.value = true
                try {
                    // Add user message locally
                    messages.add(Message("user", message))

                    val response = repo.ChatBot(token,chatReq = ChatRequest(sessionId,message))
                        .onSuccess {
                            messages.add(Message("bot", it.reply))
                            onSuccess()
                        }
                        .onFailure {
                            messages.add(Message("bot", "Failed to send message"))
                            onFailure(it.message.toString())
                        }
//                    messages.add(Message("bot", response.))

                } catch (e: Exception) {
                    messages.add(Message("bot", "Failed to send message"))
                } finally {
                    isLoading.value = false
                }
        }
        }else{
            onFailure("Token is null")

        }
    }

}