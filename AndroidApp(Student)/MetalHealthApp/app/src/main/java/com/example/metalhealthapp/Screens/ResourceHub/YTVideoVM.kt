package com.example.metalhealthapp.Screens.ResourceHub

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metalhealthapp.Model.YtVideo
import com.example.metalhealthapp.Repo.Repo
import com.example.metalhealthapp.Utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class YTVideoVM @Inject constructor(private val repo: Repo) : ViewModel() {
    private val _videos : MutableStateFlow<List<YtVideo>> = MutableStateFlow(emptyList())
    val videos : StateFlow<List<YtVideo>> = _videos

    suspend fun fetchVideos(context : Context,onSuccess : ()->Unit,onFailure : (String)->Unit){
        val token = TokenManager(context).getToken()
        viewModelScope.launch {
            if(token!=null){
                val result = repo.fetchYTVideos(token)
                    .onSuccess {
                        _videos.value = it.data.videos
                        onSuccess()
                    }
                    .onFailure {
                        onFailure(it.message.toString())
                    }
            }else{
                onFailure("Token is null")

            }

        }
    }

}