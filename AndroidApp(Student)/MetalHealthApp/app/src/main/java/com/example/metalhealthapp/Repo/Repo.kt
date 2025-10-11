package com.example.metalhealthapp.Repo

import com.example.metalhealthapp.API.Apis
import com.example.metalhealthapp.Model.ChatRequest
import com.example.metalhealthapp.Model.ChatResponse
import com.example.metalhealthapp.Model.ForumResponse
import com.example.metalhealthapp.Model.CreatePostReq
import com.example.metalhealthapp.Model.CreatePostResponse
import com.example.metalhealthapp.Model.GetPostByIdResponse
import com.example.metalhealthapp.Model.GetPostRepliesResponse
import com.example.metalhealthapp.Model.LoginResponse
import com.example.metalhealthapp.Model.UserSignUpReq
import com.example.metalhealthapp.Model.RegisterResponse
import com.example.metalhealthapp.Model.ReplyRequest
import com.example.metalhealthapp.Model.ReplyToPostResp
import com.example.metalhealthapp.Model.CreateUpvoteReponse
import com.example.metalhealthapp.Model.FetchUpvotesResponse
import com.example.metalhealthapp.Model.UserSignInReq
import org.json.JSONObject
import javax.inject.Inject

class Repo @Inject constructor(private val api: Apis) {

    //auth apis
    suspend fun signUp(user: UserSignUpReq): Result<RegisterResponse> {
        return try {
            val response = api.registerUser(user)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val jsonObject = JSONObject(errorBody)
                    jsonObject.getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
                Result.failure(Exception("Sign-up failed: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun signIn(user: UserSignInReq): Result<LoginResponse> {
        return try {
            val response = api.loginUser(user)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val jsonObject = JSONObject(errorBody)
                    jsonObject.getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
                Result.failure(Exception("Login failed: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPosts(): Result<ForumResponse> {
        return try {
            val response = api.fetchPosts()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))

            } else {
                Result.failure(Exception("Failed to fetch posts"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPost(content: CreatePostReq,authHeader: String): Result<CreatePostResponse> {
        return try {
            val response = api.createPost(content,authHeader)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody).getString("message")
                } catch (e: Exception) {
                    errorBody ?: "Unknown error"
                }
                Result.failure(Exception("Failed to create post: $errorMessage"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun getPostById(postId: String, authHeader: String): Result<GetPostByIdResponse> {
        return try {
            val response = api.getPostById(postId, authHeader)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val jsonObject = JSONObject(errorBody)
                    jsonObject.getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
                Result.failure(Exception("Failed to fetch post by ID: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReplybyId(postId: String) : Result<GetPostRepliesResponse>{
        return try {
            val response = api.getPostReplies(postId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val jsonObject = JSONObject(errorBody)
                    jsonObject.getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
                Result.failure(Exception("Failed to fetch post by ID: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun replyToPost(postId : String,authHeader : String,content : String) : Result<ReplyToPostResp>{
        return try {
            val response = api.replyToPost(postId = postId, authHeader = authHeader ,content = ReplyRequest(
                content = content
            )
            )
            if(response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            }else{
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val jsonObject = JSONObject(errorBody)
                    jsonObject.getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }

                Result.failure(Exception("Failed to reply to post: $errorMessage"))
            }

        }catch (e : Exception){
            Result.failure(e)
        }
    }

    //upvotes

    suspend fun upvotePost(postId: String,authHeader: String) : Result<CreateUpvoteReponse>{
        return try{
            val response = api.upvoteToPost(postId = postId,authHeader = authHeader)
            if(response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            }else{
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val jsonObject = JSONObject(errorBody)
                    jsonObject.getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }

                Result.failure(Exception("Failed to upvote the post: $errorMessage"))
            }
        }catch (e : Exception){
            Result.failure(e)
        }
    }

    suspend fun isUpvotedByUser(postId: String, authHeader: String) : Result<FetchUpvotesResponse>{
        return try{
            val response = api.fetchUpvotes(postId = postId, authHeader = authHeader)
            if (response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            }else{
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val jsonObject = JSONObject(errorBody)
                    jsonObject.getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
                Result.failure(Exception("Failed fetch upvotes: $errorMessage"))
            }
        }catch (e : Exception){
            Result.failure(e)
        }
    }

    //chatbot
    suspend fun ChatBot(authHeader: String,chatReq : ChatRequest) : Result<ChatResponse>{
        return try {
            val response = api.chatWithBot(authHeader = authHeader, chatReq = chatReq)

            if (response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            }else{
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val jsonObject = JSONObject(errorBody)
                    jsonObject.getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }

                Result.failure(Exception("Failed to chat with bot: $errorMessage"))
            }
        }catch (e : Exception){
            Result.failure(e)
        }
    }
}
