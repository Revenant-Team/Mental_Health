package com.example.metalhealthapp.API

import com.example.metalhealthapp.Model.ForumResponse
import com.example.metalhealthapp.Model.CreatePostReq
import com.example.metalhealthapp.Model.CreatePostResponse
import com.example.metalhealthapp.Model.GetPostByIdResponse
import com.example.metalhealthapp.Model.GetPostRepliesResponse
import com.example.metalhealthapp.Model.LoginResponse
import com.example.metalhealthapp.Model.UserSignUpReq
import com.example.metalhealthapp.Model.RegisterResponse
import com.example.metalhealthapp.Model.UserSignInReq
import com.example.metalhealthapp.Model.ReplyToPostResp
import com.example.metalhealthapp.Model.ReplyRequest
import com.example.metalhealthapp.Model.CreateUpvoteReponse
import com.example.metalhealthapp.Model.FetchUpvoteData
import com.example.metalhealthapp.Model.FetchUpvotesResponse
import retrofit2.Response
import retrofit2.http.Body
//import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Apis {



    @POST("api/forum/")
    suspend fun createPost(@Body post : CreatePostReq,
                           @Header("token") authHeader: String) : Response<CreatePostResponse>

    @POST("api/users/register")
    suspend fun registerUser(@Body user : UserSignUpReq) : Response<RegisterResponse>

    @POST("api/users/login")
    suspend fun loginUser(@Body user : UserSignInReq) : Response<LoginResponse>

    @GET("api/forum/")
    suspend fun fetchPosts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("category") category: String? = null,
        @Query("sortBy") sortBy: String = "newest",
        @Query("instituteId") instituteId: String? = null
    ) : Response<ForumResponse>

    @GET("api/forum/{postId}")
    suspend fun getPostById(
        @Path("postId") postId: String,
        @Header("Authorization") authHeader: String
    ) : Response<GetPostByIdResponse>

    @GET("api/reply/posts/{postId}/replies")
    suspend fun getPostReplies(
            @Path("postId") postId: String,
//            @Header("Authorization") authHeader: String
    ): Response<GetPostRepliesResponse>

    @POST("api/reply/posts/{postId}/replies")
    suspend fun replyToPost(
        @Path("postId") postId: String,
        @Header("token") authHeader: String,
        @Body content : ReplyRequest
    ) : Response<ReplyToPostResp>


    //upvote
    @POST("api/upvote/posts/{postId}/upvote")
    suspend fun upvoteToPost(
        @Path("postId") postId :String,
        @Header("token") authHeader: String
    ) : Response<CreateUpvoteReponse>

    @GET("api/upvote/posts/{postId}/upvotes")
    suspend fun fetchUpvotes(
        @Path("postId") postId :String,
        @Header("token") authHeader: String
    ) : Response<FetchUpvotesResponse>

    @POST("api/chat/chat_with_bot")
    suspend fun chatWithBot(
        
    )
}