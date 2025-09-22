package com.example.metalhealthapp.Repo

import com.example.metalhealthapp.API.Apis
import com.example.metalhealthapp.Model.ForumResponse
import com.example.metalhealthapp.Model.CreatePostReq
import com.example.metalhealthapp.Model.createPostResponse
import javax.inject.Inject

class Repo @Inject constructor(private val api: Apis) {
    suspend fun getPosts() : Result<ForumResponse>{
        return try {
            val response = api.fetchPosts()
            if (response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                }?: Result.failure(Exception("Response body is null"))

            }else{
                Result.failure(Exception("Failed to fetch posts"))
            }
        }catch (e: Exception){
            Result.failure(e)
            }
    }

    suspend fun createPost(content: CreatePostReq) : Result<createPostResponse>{
        return try {
            val response = api.createPost(content)
            if (response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                }?: Result.failure(Exception("Response body is null"))
                }else{
                Result.failure(Exception("Failed to create post "))
            }
        }catch (e: Exception){
            Result.failure(e)
        }

    }
}