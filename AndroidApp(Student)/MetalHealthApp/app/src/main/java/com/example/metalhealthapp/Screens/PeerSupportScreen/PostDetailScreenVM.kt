package com.example.metalhealthapp.Screens.PeerSupportScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metalhealthapp.Model.ForumPost
import com.example.metalhealthapp.Model.ForumPostDetail
import com.example.metalhealthapp.Model.ReplyDetail
import com.example.metalhealthapp.Repo.Repo
import com.example.metalhealthapp.Utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailScreenVM @Inject constructor(private val repo : Repo)  : ViewModel() {
    private val _post : MutableStateFlow<ForumPostDetail> = MutableStateFlow(ForumPostDetail())
    val post : StateFlow<ForumPostDetail> = _post

    private val _replies : MutableStateFlow<List<ReplyDetail>> = MutableStateFlow(emptyList())
    val replies : StateFlow<List<ReplyDetail>> = _replies
    val isLoading : MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun fetchPost(context : Context, postId : String,onSuccess : ()->Unit,onFailure : (String) -> Unit){
        isLoading.value = true
        viewModelScope.launch {
            val tokenManager = TokenManager(context)
            val token = tokenManager.getToken()
            if(token!=null){
                repo.getPostById(postId,"Bearer $token")
                    .onSuccess {
                        _post.value = it.data.post
                        fetchReplies(context,postId,onSuccess,onFailure)

                    }
                    .onFailure {
                        onFailure(it.message?:"Unknown Error")
                        isLoading.value = false

                        // Handle error
                    }

            }
        }
    }

    fun fetchReplies(context: Context,postId: String,onSuccess: () -> Unit,onFailure: (String) -> Unit){
        isLoading.value = true
        viewModelScope.launch {
            val tokenManager = TokenManager(context)
            val token = tokenManager.getToken()
            if(token!=null) {
                repo.getReplybyId(postId)
                    .onSuccess {
                        _replies.value = it.data?.replies?:emptyList()
                        isLoading.value = false
                        onSuccess()
                    }
                    .onFailure {
                        onFailure(it.message ?: "Unknown Error")
                        isLoading.value = false
                        // Handle error
                    }
            }
        }
    }

}