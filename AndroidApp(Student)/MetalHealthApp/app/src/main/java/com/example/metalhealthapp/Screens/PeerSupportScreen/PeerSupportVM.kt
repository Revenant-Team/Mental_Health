package com.example.metalhealthapp.Screens.PeerSupportScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metalhealthapp.Model.Content
import com.example.metalhealthapp.Model.ForumPost
import com.example.metalhealthapp.Model.ForumResponse
import com.example.metalhealthapp.Repo.Repo
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PeerSupportVM @Inject constructor(private val repo: Repo): ViewModel() {
    private val _posts : MutableStateFlow<List<ForumPost>> = MutableStateFlow(emptyList())
    val posts : StateFlow<List<ForumPost>> = _posts

    fun fetchPosts(onError : (String)-> Unit){
        viewModelScope.launch {
            repo.getPosts()
                .onSuccess {
                    _posts.value = it.data.posts
                }
                .onFailure {
                    onError(it.message.toString())
                }

        }
    }



}