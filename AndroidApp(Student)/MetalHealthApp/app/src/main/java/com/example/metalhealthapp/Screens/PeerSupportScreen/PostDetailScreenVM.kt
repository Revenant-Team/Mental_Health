package com.example.metalhealthapp.Screens.PeerSupportScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    //upvoteCount is not useful though
//    private val _upvoteCount : MutableStateFlow<Int> = MutableStateFlow(0)
//    val upvoteCount : StateFlow<Int> = _upvoteCount

    private val _isUpvoted : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUpvoted : StateFlow<Boolean> = _isUpvoted

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
                        isUpvotedByUser(postId,context,onSuccess,onFailure)

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

    fun replyToPost(replyContent : String,postId : String,context: Context,onSuccess: () -> Unit,onFailure: (String) -> Unit){
        viewModelScope.launch {
            val token = TokenManager(context).getToken()
            Log.d("token",token.toString())
            if(token!=null){
                repo.replyToPost(postId = postId, authHeader = token,content = replyContent)
                    .onSuccess {
                        fetchReplies(context,postId,onSuccess,onFailure)
                    }
                    .onFailure {
                        onFailure(it.message?:"Unknown Error")
                    }
            }else{
                onFailure("Token is not Stored")
            }
        }
    }

    fun upvoteToPost(postId : String,context: Context,onFailure: (String) -> Unit,onSuccess: () -> Unit){
        val token = TokenManager(context).getToken()
        val wasUpvoted = isUpvoted.value
        changeUpvotedState()
        viewModelScope.launch {
            if(token!=null){
                repo.upvotePost(postId = postId, authHeader = token)
                .onSuccess {
                    onSuccess()
                }
                .onFailure {
//                    _isUpvoted.value = wasUpvoted
                    changeUpvotedState()
                    onFailure(it.message?:"Unknown message")
                }
            }else{
                onFailure("Token is not Stored")
            }
        }

    }

    //this function is used to check wheather user has clicked or not
    fun isUpvotedByUser(postId : String, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit){
        val token = TokenManager(context).getToken()
        viewModelScope.launch {
            if(token!=null){
                repo.isUpvotedByUser(postId = postId, authHeader = token)
                    .onSuccess {
//                        _upvoteCount.value = it.data.upvotes.toInt()
                        _isUpvoted.value=it.data.upvoted
                        onSuccess()
                    }
                    .onFailure {
                        onFailure(it.message?:"Unknown Error")
                    }
        }
        }
    }

    //function to change the upvoted by user locally only for backend other function is there
    fun changeUpvotedState(){
        _isUpvoted.value = !_isUpvoted.value
        _post.value = _post.value.copy(
            engagement = _post.value.engagement.copy(
                upvotes = if (_isUpvoted.value)
                    _post.value.engagement.upvotes + 1
                else
                    _post.value.engagement.upvotes - 1
            )
        )
    }

    private var lastUpvoteTime = 0L

    fun canUpvoteNow(): Boolean {
        val now = System.currentTimeMillis()
        if (now - lastUpvoteTime > 800) { // 800ms cooldown
            lastUpvoteTime = now
            return true
        }
        return false
    }


}