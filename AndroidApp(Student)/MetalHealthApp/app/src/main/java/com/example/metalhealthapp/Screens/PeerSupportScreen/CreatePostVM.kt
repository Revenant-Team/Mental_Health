package com.example.metalhealthapp.Screens.PeerSupportScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metalhealthapp.Model.Content
import com.example.metalhealthapp.Model.CreatePostReq
import com.example.metalhealthapp.Repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostVM @Inject constructor(private val repo: Repo) : ViewModel() {

    fun createPost(post: Content, onResult: (String) -> Unit){
        viewModelScope.launch {
            val req = CreatePostReq(
                content = post
            )
            Log.d("createPost",req.toString())
            repo.createPost(req)
                .onSuccess {
                    onResult(it.message)
                }
                .onFailure {
                    onResult(it.message.toString())
                }
        }
    }
}