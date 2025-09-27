package com.example.metalhealthapp.Screens.PeerSupportScreen


import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metalhealthapp.Model.ForumPost
import com.example.metalhealthapp.Model.ForumPostDetail
import com.example.metalhealthapp.Model.ReplyDetail
import java.text.SimpleDateFormat
import java.util.*

// Data classes based on your MongoDB models
data class Post(
    val _id: String,
    val anonymousId: String,
    val instituteId: String,
    val title: String,
    val content: String,
    val category: String,
    val tags: List<String>,
    val metadata: PostMetadata,
    val engagement: PostEngagement,
    val upvotedBy: List<String>,
    val createdAt: String,
    val updatedAt: String
)

data class PostMetadata(
    val createdAt: String,
    val updatedAt: String,
    val isActive: Boolean,
    val priority: String
)

data class PostEngagement(
    val upvotes: Int,
    val totalReplies: Int,
    val views: Int
)

data class Reply(
    val _id: String,
    val postId: String,
    val parentReplyId: String?,
    val anonymousId: String,
    val content: String,
    val metadata: ReplyMetadata,
    val engagement: ReplyEngagement,
    val upvotedBy: List<String>,
    val createdAt: String,
    val updatedAt: String
)

data class ReplyMetadata(
    val createdAt: String,
    val isActive: Boolean,
    val depth: Int
)

data class ReplyEngagement(
    val upvotes: Int,
    val childReplies: Int
)

// Post Detail Screen with Replies
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId : String,
    viewModel : PostDetailScreenVM,
//    replies: List<Reply>,
    currentUserId: String,
    onBackClick: () -> Unit = {},
    onUpvotePost: (String) -> Unit = {},
    onUpvoteReply: (String) -> Unit = {},
    onReply: (String, String?) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val post by viewModel.post.collectAsState()
    val replies by viewModel.replies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchPost(context,postId,{}){
            Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
        }
    }

    var replyText by remember { mutableStateOf("") }
    var showReplyInput by remember { mutableStateOf(false) }
    var replyingTo by remember { mutableStateOf<String?>(null) }

    val purple = Color(0xFF7C3AED)
    val lightPurple = Color(0xFFF3F4F6)
    val blue = Color(0xFF3B82F6)
    val green = Color(0xFF10B981)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Discussion",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showReplyInput = !showReplyInput
                    replyingTo = null
                },
                containerColor = purple,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = if (showReplyInput) Icons.Default.Close else Icons.Default.Reply,
                    contentDescription = if (showReplyInput) "Close Reply" else "Reply",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->

        if(isLoading){
            Box(modifier = Modifier.padding(paddingValues)
                .fillMaxSize()){

                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }else{
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Original Post
                item {
                    PostDetailCard(
                        post = post,
                        isUpvoted = post.upvotedBy.contains(currentUserId),
                        onUpvote = { onUpvotePost(post._id) },
                        onReply = {
                            showReplyInput = true
                            replyingTo = null
                        }
                    )
                }

                // Reply Input
                item {
                    AnimatedVisibility(
                        visible = showReplyInput,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        ReplyInputCard(
                            replyText = replyText,
                            onReplyTextChange = { replyText = it },
                            replyingToId = replyingTo,
                            onSendReply = {
                                if (replyText.isNotBlank()) {
                                    onReply(replyText, replyingTo)
                                    replyText = ""
                                    showReplyInput = false
                                    replyingTo = null
                                }
                            },
                            onCancel = {
                                showReplyInput = false
                                replyingTo = null
                                replyText = ""
                            }
                        )
                    }
                }

//            // Replies Section Header
            item {
                if (replies.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forum,
                            contentDescription = "Replies",
                            tint = purple,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${replies.size} Replies",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                }
            }

            // Replies List
            items(replies.filter { it.parentReplyId == null }) { reply ->
                ReplyCard(
                    reply = reply,
                    childReplies = replies.filter { it.parentReplyId == reply._id },
                    currentUserId = currentUserId,
                    onUpvote = { onUpvoteReply(reply._id) },
                    onReply = { replyId ->
                        showReplyInput = true
                        replyingTo = replyId
                    },
                    maxDepth = 3
                )
            }

                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

        }

    }
}

@Composable
fun PostDetailCard(
    post: ForumPostDetail,
    isUpvoted: Boolean,
    onUpvote: () -> Unit,
    onReply: () -> Unit,
    modifier: Modifier = Modifier
) {
    val purple = Color(0xFF7C3AED)
    val lightPurple = Color(0xFFF3F4F6)
    val blue = Color(0xFF3B82F6)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Category Badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = getCategoryColor(post.category).copy(alpha = 0.1f)
            ) {
                Text(
                    text = post.category,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    color = getCategoryColor(post.category),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = post.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tags
            if (post.tags.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(post.tags) { tag ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = lightPurple
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                fontSize = 11.sp,
                                color = purple,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Post Info & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Anonymous Avatar
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(blue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "A",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Anonymous • ${formatTimeAgo(post.metadata.createdAt)}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.weight(1f))

                // Views
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Views",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.engagement.views}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.Gray.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Upvote Button
                Surface(
                    onClick = onUpvote,
                    shape = RoundedCornerShape(8.dp),
                    color = if (isUpvoted) purple.copy(alpha = 0.1f) else Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isUpvoted) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                            contentDescription = "Upvote",
                            tint = if (isUpvoted) purple else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${post.engagement.upvotes}",
                            fontSize = 14.sp,
                            color = if (isUpvoted) purple else Color.Gray,
                            fontWeight = if (isUpvoted) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }

                // Reply Button
                Surface(
                    onClick = onReply,
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Reply,
                            contentDescription = "Reply",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${post.engagement.totalReplies}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Share Button
                Surface(
                    onClick = { },
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Share",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReplyCard(
    reply: ReplyDetail,
    childReplies: List<ReplyDetail>,
    currentUserId: String,
    onUpvote: () -> Unit,
    onReply: (String) -> Unit,
    maxDepth: Int = 3,
    modifier: Modifier = Modifier
) {
    val purple = Color(0xFF7C3AED)
    val isUpvoted = reply.upvotedBy.contains(currentUserId)
    val indentLevel = minOf(reply.metadata.depth, maxDepth)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = (indentLevel * 16).dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(purple, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Anonymous",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "• ${formatTimeAgo(reply.metadata.createdAt)}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Content
                Text(
                    text = reply.content,
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Upvote
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onUpvote() }
                    ) {
                        Icon(
                            imageVector = if (isUpvoted) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                            contentDescription = "Upvote",
                            tint = if (isUpvoted) purple else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${reply.engagement.upvotes}",
                            fontSize = 12.sp,
                            color = if (isUpvoted) purple else Color.Gray
                        )
                    }

                    // Reply
                    if (reply.metadata.depth < maxDepth) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onReply(reply._id) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Reply,
                                contentDescription = "Reply",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Reply",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        // Child Replies
        childReplies.forEach { childReply ->
            ReplyCard(
                reply = childReply,
                childReplies = emptyList(), // Only show direct children to avoid deep nesting
                currentUserId = currentUserId,
                onUpvote = { },
                onReply = onReply,
                maxDepth = maxDepth
            )
        }
    }
}

@Composable
fun ReplyInputCard(
    replyText: String,
    onReplyTextChange: (String) -> Unit,
    replyingToId: String?,
    onSendReply: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val purple = Color(0xFF7C3AED)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (replyingToId != null) "Replying to comment" else "Write a reply",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                IconButton(
                    onClick = onCancel,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            BasicTextField(
                value = replyText,
                onValueChange = onReplyTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 18.sp
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.Gray.copy(alpha = 0.05f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        if (replyText.isEmpty()) {
                            Text(
                                text = "Share your thoughts...",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                lineHeight = 18.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) {
                    Text(
                        text = "Cancel",
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onSendReply,
                    enabled = replyText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purple,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Reply")
                }
            }
        }
    }
}

// Helper Functions
fun getCategoryColor(category: String): Color {
    return when (category) {
        "Academic" -> Color(0xFF3B82F6)
        "Personal" -> Color(0xFF10B981)
        "Social" -> Color(0xFFF59E0B)
        "Health" -> Color(0xFFEF4444)
        "Career" -> Color(0xFF8B5CF6)
        else -> Color(0xFF6B7280)
    }
}

fun formatTimeAgo(dateString: String): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = format.parse(dateString)
        val now = Date()
        val diff = now.time - (date?.time ?: 0)

        when {
            diff < 60000 -> "Just now"
            diff < 3600000 -> "${diff / 60000}m ago"
            diff < 86400000 -> "${diff / 3600000}h ago"
            diff < 604800000 -> "${diff / 86400000}d ago"
            else -> "${diff / 604800000}w ago"
        }
    } catch (e: Exception) {
        "Recently"
    }
}