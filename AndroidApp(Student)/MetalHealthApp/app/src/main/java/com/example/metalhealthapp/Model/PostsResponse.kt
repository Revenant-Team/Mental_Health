package com.example.metalhealthapp.Model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class ForumResponse(
    val success: Boolean,
    val data: ForumData
)

data class ForumData(
    val posts: List<ForumPost>,
    val pagination: Pagination
)

data class ForumPost(
    val content: Content,        // <-- Wraps title, body, category, tags
    @SerializedName("_id") val id: String,
    val authorId: String?,
    val instituteId: String?,
    val likes: Int,
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("__v") val version: Int
)

@Serializable
data class Content(
    val title: String = "",
    val body: String = "",
    val category: String = "",
    val tags: List<String> = emptyList()
)

@Serializable
data class CreatePostReq(
    val content: Content
)
data class Pagination(
    val currentPage: Int,
    val totalPages: Int,
    val totalPosts: Long,
    val hasNextPage: Boolean,
    val hasPrevPage: Boolean
)

//post create response
data class createPostResponse(
    val success: Boolean,
    val message: String
)