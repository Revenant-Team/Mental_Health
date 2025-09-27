package com.example.metalhealthapp.Model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

// ======================
// Response for GET /api/forum/posts
// ======================
@Serializable
data class ForumResponse(
    val success: Boolean,
    val data: ForumData
)

@Serializable
data class ForumData(
    val posts: List<ForumPost>,
    val pagination: Pagination
)

@Serializable
data class ForumPost(
    val _id: String ="",
    val anonymousId: String="",
    val instituteId: String ="",
    val title: String="",
    val content: String="",
    val category: String="",
    val tags: List<String> = emptyList(),
    val engagement: Engagement = Engagement(),
    val metadata: Metadata = Metadata(),
    val upvotedBy: List<String> = emptyList(),

    )

@Serializable
data class Engagement(
    val upvotes: Int = 0,
    val totalReplies: Int = 0,
    val views: Int = 0
)

@Serializable
data class Metadata(
    val createdAt: String ="",
    val priority: String = "Medium"
)


@Serializable
data class Pagination(
    val current: Int,
    val pages: Int,
    val total: Long
)

// ======================
// Request for POST /api/forum/create
// ======================
@Serializable
data class CreatePostReq(
    val anonymousId: String,
    val instituteId: String,
    val title: String,
    val content: String,
    val category: String,
    val tags: List<String> = emptyList()
)

// ======================
// Response for POST /api/forum/create
// ======================
@Serializable
data class CreatePostResponse(
    val success: Boolean,
    val message: String
)

// ======================
// Response for POST /api/forum/:postId
// ======================

data class GetPostByIdResponse(
    val success: Boolean,
    val data: PostData
)

data class PostData(
    val post: ForumPostDetail
)

data class ForumPostDetail(
    val _id: String = "",
    val anonymousId: String = "",
    val instituteId: InstituteInfo = InstituteInfo(),
    val title: String = "",
    val content: String = "",
    val category: String = "",
    val tags: List<String> = emptyList(),
    val metadata: PostMetadata = PostMetadata(),
    val engagement: Engagement = Engagement(),
    val upvotedBy: List<String> = emptyList(),
    val isMyPost: Boolean = false
)

data class InstituteInfo(
    val _id: String = "",
    val name: String = "",
    val code: String = ""
)


data class PostMetadata(
    val createdAt: String = "",
    val updatedAt: String = "",
    val isActive: Boolean = true,
    val priority: String = "Medium"
)
//data class En(
//    val upvotes: Int,
//    val totalReplies: Int,
//    val views: Int
//)
