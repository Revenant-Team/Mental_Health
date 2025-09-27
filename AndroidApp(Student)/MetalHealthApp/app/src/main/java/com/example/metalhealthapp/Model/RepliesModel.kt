package com.example.metalhealthapp.Model


data class GetPostRepliesResponse(
    val success: Boolean,
    val data: RepliesData? = null,
    val message: String? = null
)

data class RepliesData(
    val replies: List<ReplyDetail> = emptyList()
)

data class ReplyDetail(
    val _id: String = "",
    val postId: String = "",
    val parentReplyId: String? = null,
    val anonymousId: String = "",
    val content: String = "",
    val metadata: ReplyMetadata = ReplyMetadata(),
    val engagement: ReplyEngagement = ReplyEngagement(),
    val upvotedBy: List<String> = emptyList(),
    val isMyReply: Boolean = false,
    val children: List<ReplyDetail> = emptyList() // for nested replies
)

data class ReplyMetadata(
    val createdAt: String = "",
    val isActive: Boolean = true,
    val depth: Int = 0
)

data class ReplyEngagement(
    val upvotes: Int = 0,
    val childReplies: Int = 0
)
