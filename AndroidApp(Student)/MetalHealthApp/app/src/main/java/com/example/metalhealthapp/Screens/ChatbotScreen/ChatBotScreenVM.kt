package com.example.metalhealthapp.Screens.ChatbotScreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metalhealthapp.Model.ChatRequest
import com.example.metalhealthapp.Repo.Repo
import com.example.metalhealthapp.Utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long
)

@HiltViewModel
class ChatBotScreenVM @Inject constructor(private val repo: Repo) : ViewModel() {
    private val sessionId = UUID.randomUUID().toString()
    private val _messages : MutableStateFlow<List<ChatMessage>>  = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages : StateFlow<List<ChatMessage>> = _messages

    val isLoading = MutableStateFlow<Boolean>(false)

    fun sendMessage(context: Context,message: String,onSuccess : ()->Unit, onFailure : (String)->Unit) {
        val token = TokenManager(context).getToken()
//        val previousContext = messages.value.filter { it.isUser }
//        val prompt ="${messages} this is previous chats its for your context . now my message is $message"
        if(token != null){
            viewModelScope.launch {
                isLoading.value = true
                try {
                    // Add user message locally
                    _messages.value += ChatMessage(text = message, isUser = true, timestamp = System.currentTimeMillis())

                    val response = repo.ChatBot(token,chatReq = ChatRequest(sessionId,message))
                        .onSuccess {
                            _messages.value+= ChatMessage(text = it.reply, isUser = false, timestamp = System.currentTimeMillis())
                            onSuccess()
                        }
                        .onFailure {
                            _messages.value+= ChatMessage(text = "Failed to send message", isUser = false, timestamp = System.currentTimeMillis())
                            onFailure(it.message.toString())
                            Log.d("error in chatting",it.message.toString())
                        }
//                    messages.add(Message("bot", response.))

                } catch (e: Exception) {
                    Log.d("error in chatting",e.message.toString())
                    _messages.value+= ChatMessage(text = "Failed to send message", isUser = false, timestamp = System.currentTimeMillis())
                } finally {
                    isLoading.value = false
                }
        }
        }else{
            onFailure("Token is null")

        }
    }

}