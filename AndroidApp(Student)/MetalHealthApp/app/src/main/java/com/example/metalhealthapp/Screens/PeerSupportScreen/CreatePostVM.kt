package com.example.metalhealthapp.Screens.PeerSupportScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metalhealthapp.Model.CreatePostReq
import com.example.metalhealthapp.Repo.Repo
import com.example.metalhealthapp.Utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostVM @Inject constructor(private val repo: Repo) : ViewModel() {

    fun createPost(context: Context,post : CreatePostReq, onResult: (String) -> Unit){
        viewModelScope.launch {
            val token = TokenManager(context).getToken()
            if(token!=null){

                repo.createPost(post,token)
                    .onSuccess {
                        onResult(it.message)
                    }
                    .onFailure {
                        onResult(it.message.toString())
                    }
            }

        }
    }
}