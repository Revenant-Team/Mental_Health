package com.example.metalhealthapp.Screens.ResourceHub


import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.metalhealthapp.Model.YTVideoResponse
import com.example.metalhealthapp.Model.YtVideo
import com.example.metalhealthapp.Screens.ControlScreen.topBar


// Main Navigation Container
@Composable
fun YouTubeVideoApp(
    navController: NavController,
    viewModel: YTVideoVM
) {
    var selectedVideo by remember { mutableStateOf<YtVideo?>(null) }

    // Sample data - replace with your backend API call
    val videos by viewModel.videos.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.fetchVideos(context, onSuccess = {}, onFailure = {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }
    if (selectedVideo == null) {
        VideoGridScreen(
            navController,
            videos = videos,
            onVideoClick = { video -> selectedVideo = video }
        )
    } else {
        YouTubePlayerScreen(
            navController,
            video = selectedVideo!!,
            onBack = { selectedVideo = null }
        )
    }
}

// Video Grid Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoGridScreen(
    navController: NavController,
    videos: List<YtVideo>,
    onVideoClick: (
            YtVideo
            ) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Suggested YT Videos",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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
    ) { padding ->
        if(videos.isEmpty()){
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(padding)
            ){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }
        LazyColumn (
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(
                videos
            ){
                    video ->
                VideoCard(
                    video = video,
                    onClick = { onVideoClick(video) }
                )

            }
        }
    }
}

// Video Card Composable
@Composable
fun VideoCard(
    video: YtVideo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
//            .aspectRatio(0.75f)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Thumbnail
            AsyncImage(
                model = video.thumbnail,
                contentDescription = video.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )

            // Title (if provided)
            if (video.title.isNotEmpty()) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

// YouTube Player Screen with WebView
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouTubePlayerScreen(
    navController: NavController,
    video: YtVideo,
    onBack: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "YouTube Video Player",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp()}) {
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
    ) { padding ->
        Box (modifier = Modifier.fillMaxSize()
            .padding(padding)){
//            Box(
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .fillMaxWidth()
//                    .aspectRatio(16f / 9f) // Keeps YouTube video ratio
//            ) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                loadWithOverviewMode = false
                                useWideViewPort = true
                                mediaPlaybackRequiresUserGesture = false
                            }

                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    isLoading = true
                                }
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    isLoading = false
                                }
                            }

                            webChromeClient = WebChromeClient() // <-- needed for video

                            loadUrl("https://www.youtube.com/embed/${video.videoId}?rel=0&autoplay=0&modestbranding=1")
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )


                // Loading indicator
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

//        }
        }

}

// Usage in MainActivity:
// setContent {
//     YouTubeVideoAppTheme {
//         YouTubeVideoApp()
//     }
// }