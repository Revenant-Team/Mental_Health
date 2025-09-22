package com.example.metalhealthapp.API

import com.example.metalhealthapp.Model.ForumResponse
import com.example.metalhealthapp.Model.CreatePostReq
import com.example.metalhealthapp.Model.createPostResponse
import retrofit2.Response
import retrofit2.http.Body
//import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface Apis {

    @GET("api/forum/posts")
    suspend fun fetchPosts() : Response<ForumResponse>

    @POST("api/forum/create")
    suspend fun createPost(@Body post : CreatePostReq) : Response<createPostResponse>
}