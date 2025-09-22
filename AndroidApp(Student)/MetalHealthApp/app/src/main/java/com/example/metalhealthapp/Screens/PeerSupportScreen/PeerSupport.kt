@file:OptIn(ExperimentalMaterial3Api::class)

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.metalhealthapp.Model.Content
import com.example.metalhealthapp.Screens.PeerSupportScreen.CreatePostVM
import com.example.metalhealthapp.Screens.PeerSupportScreen.PeerSupportVM

// Updated PeerSupportScreen with FAB
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeerSupportScreen(
    viewModel : PeerSupportVM,
    modifier: Modifier = Modifier,
    onCreatePostClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val posts by viewModel.posts.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchPosts{
            Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
        }

    }

    val purple = Color(0xFF7C3AED)
    val lightPurple = Color(0xFFF3F4F6)
    val blue = Color(0xFF3B82F6)

    Box(modifier = modifier.fillMaxSize()) {
        if(posts.isEmpty()){
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }else{
            Log.d("posts",posts.toString())
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentPadding = PaddingValues(bottom = 80.dp) // Extra padding for FAB
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(posts){it->
                    PostCard(
                        timeAgo = "2h ago",
                        content = it.content.body?:"",
                        tags = it.content.tags?:emptyList(),
                        repliesCount = 8,
                        helpfulCount = 12,
                        avatarColor = blue,
                        modifier= Modifier
                    )
                }

//            item {
//                // First Post
//                PostCard(
//                    timeAgo = "2h ago",
//                    content = "Feeling overwhelmed with final exams coming up. Any tips for managing study stress?",
//                    tags = listOf("#stress", "#exams"),
//                    repliesCount = 8,
//                    helpfulCount = 12,
//                    avatarColor = blue
//                )
//            }

//            item {
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            item {
//                // Second Post
//                PostCard(
//                    timeAgo = "4h ago",
//                    content = "Started meditation practice this week and it's really helping with my anxiety!",
//                    tags = listOf("#meditation", "#progress"),
//                    repliesCount = 5,
//                    helpfulCount = 18,
//                    avatarColor = purple
//                )
//            }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }


        // FAB positioned in bottom right
        FloatingActionButton(
            onClick = onCreatePostClick,
            containerColor = purple,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Post",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// New Create Post Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    viewModel: CreatePostVM,
    onBackClick: () -> Unit = {},
    onPostClick: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var postText by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(setOf<String>()) }
    var isAnonymous by remember { mutableStateOf(true) }

    val purple = Color(0xFF7C3AED)
    val lightPurple = Color(0xFFF3F4F6)

    val availableTags = listOf(
        "#stress", "#anxiety", "#depression", "#meditation", "#progress",
        "#exams", "#relationships", "#work", "#family", "#health",
        "#motivation", "#selfcare", "#therapy", "#support"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Post",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {

                            if (postText.isNotBlank()) {
                                val createPost = Content(
                                    title = "test",
                                    category = "academic_stress" ,
                                    body = postText,
                                    tags = listOf("anxiety")
                                )
                                viewModel.createPost(createPost){
                                    onPostClick()
                                    Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
                                }

                            }
                        },
                        enabled = postText.isNotBlank()
                    ) {
                        Text(
                            text = "Post",
                            color = if (postText.isNotBlank()) purple else Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Anonymous Toggle
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = lightPurple),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isAnonymous) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Anonymous",
                                tint = purple,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isAnonymous) "Post Anonymously" else "Post with Profile",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                Text(
                                    text = if (isAnonymous) "Your identity will be hidden" else "Your profile will be visible",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Switch(
                            checked = isAnonymous,
                            onCheckedChange = { isAnonymous = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = purple,
                                checkedTrackColor = purple.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
            }

            // Post Content Input
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "What's on your mind?",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        BasicTextField(
                            value = postText,
                            onValueChange = { postText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp),
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Black,
                                lineHeight = 20.sp
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
                                    if (postText.isEmpty()) {
                                        Text(
                                            text = "Share your thoughts, experiences, or ask for support...",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            lineHeight = 20.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${postText.length}/500",
                            fontSize = 12.sp,
                            color = if (postText.length > 500) Color.Red else Color.Gray,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Tags Selection
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription = "Tags",
                                tint = purple,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add Tags",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Selected Tags
                        if (selectedTags.isNotEmpty()) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(selectedTags.toList()) { tag ->
                                    SelectedTagChip(
                                        text = tag,
                                        onRemove = { selectedTags = selectedTags - tag }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Available Tags
                        availableTags.chunked(3).forEach { tagRow ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                tagRow.forEach { tag ->
                                    TagChip(
                                        text = tag,
                                        isSelected = tag in selectedTags,
                                        onClick = {
                                            selectedTags = if (tag in selectedTags) {
                                                selectedTags - tag
                                            } else {
                                                selectedTags + tag
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                // Fill remaining space if row has less than 3 items
                                repeat(3 - tagRow.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            // Guidelines
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = lightPurple),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Guidelines",
                                tint = purple,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Community Guidelines",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "• Be respectful and supportive\n• No personal attacks or harassment\n• Keep content relevant to mental health\n• Avoid sharing personal information",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}


@Composable
fun SelectedTagChip(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF7C3AED),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 6.dp, bottom = 6.dp)
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove tag",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun TagChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFF7C3AED) else Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Color(0xFF7C3AED) else Color.Gray.copy(alpha = 0.5f)
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PostCard(
    timeAgo: String,
    content: String,
    tags: List<String>,
    repliesCount: Int,
    helpfulCount: Int,
    avatarColor: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
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
                        .size(32.dp)
                        .background(avatarColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "A",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Anonymous",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(Color(0xFF10B981), CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = timeAgo,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            Text(
                text = content,
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tags
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tags.size) { index ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF3F4F6)
                    ) {
                        Text(
                            text = tags[index],
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            color = Color(0xFF7C3AED),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Replies",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$repliesCount replies",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Helpful",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$helpfulCount helpful",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun CreatePostPreview() {
//    MaterialTheme {
//        CreatePostScreen()
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PeerSupportWithFabPreview() {
//    MaterialTheme {
//        PeerSupportScreen()
//    }
//}