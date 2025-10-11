package com.example.metalhealthapp.Screens.PeerSupportScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metalhealthapp.Model.ForumPost
import com.example.metalhealthapp.Model.ForumResponse
import com.example.metalhealthapp.Repo.Repo
import com.example.metalhealthapp.Utils.TokenManager
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
    val isLoading : MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun fetchPosts(onError : (String)-> Unit){
        isLoading.value =true
        viewModelScope.launch {
            repo.getPosts()
                .onSuccess {
                    isLoading.value = false
                    _posts.value = it.data.posts
                }
                .onFailure {
                    onError(it.message.toString())
                    isLoading.value = false
                }

        }
    }

    fun fetchRecommendedPosts(context : Context,onError : (String)-> Unit,onSuccess : ()-> Unit){
        val token = TokenManager(context).getToken()
        isLoading.value =true
        if(token!=null){
            viewModelScope.launch {
                val response = repo.fetchRecommendedPosts(token)
                    .onSuccess {
                        postdata->
                        onSuccess()
                        val _newPosts : MutableStateFlow<List<ForumPost>> = MutableStateFlow<List<ForumPost>>(emptyList())
                       for(i in postdata.data.recommendedPosts){
                           _newPosts.value+=posts.value.filter { it._id==i.id }
                       }
                        _posts.value= _newPosts.value
                        isLoading.value = false
                    }
                    .onFailure {
                        onError(it.message.toString())
                        isLoading.value = false
                    }
            }
        }else{
            onError("No token found")
            isLoading.value = false
        }
    }



}