@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.resourcehub

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceHubScreen(onclickYtVideo : ()->Unit = {},
                      modifier: Modifier=Modifier) {
    val lightPurple = Color(0xFFE8E0FF)
    val lightBlue = Color(0xFFE0F2FF)
    val lightGreen = Color(0xFFE0FFF0)
    val darkPurple = Color(0xFF7C3AED)
    val blue = Color(0xFF3B82F6)
    val green = Color(0xFF10B981)
    val orange = Color(0xFFEA580C)
    val pink = Color(0xFFEC4899)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {



        // Category tabs
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                CategoryCard(
                    title = "Meditation",
                    icon = Icons.Default.PlayArrow,
                    backgroundColor = lightPurple,
                    iconColor = darkPurple
                )
            }
            item {
                CategoryCard(
                    title = "Videos",
                    icon = Icons.Default.Videocam,
                    backgroundColor = lightBlue,
                    iconColor = blue,
                    onclick = onclickYtVideo
                )
            }
            item {
                CategoryCard(
                    title = "Articles",
                    icon = Icons.Default.MenuBook,
                    backgroundColor = lightGreen,
                    iconColor = green
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Recommended for You section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recommended for You",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = "Trending",
                tint = darkPurple,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recommended items
        RecommendedItem(
            title = "Morning Stress Relief",
            duration = "10 min",
            icon = Icons.Default.PlayArrow,
            backgroundColor = Color(0xFFDA70D6),
            iconColor = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        RecommendedItem(
            title = "5-Minute Breathing Exercise",
            duration = "5 min",
            icon = Icons.Default.Videocam,
            backgroundColor = Color(0xFF40C4FF),
            iconColor = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        RecommendedItem(
            title = "Managing Academic Pressure",
            duration = "3 min read",
            icon = Icons.Default.MenuBook,
            backgroundColor = Color(0xFF4CAF50),
            iconColor = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Meditation Playlists section
        Text(
            text = "Meditation Playlists",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Playlist grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PlaylistCard(
                title = "Sleep Stories",
                trackCount = "12 tracks",
                backgroundColor = Color(0xFF7E57C2),
                modifier = Modifier.weight(1f)
            )
            PlaylistCard(
                title = "Focus Boost",
                trackCount = "8 tracks",
                backgroundColor = orange,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PlaylistCard(
                title = "Anxiety Relief",
                trackCount = "15 tracks",
                backgroundColor = green,
                modifier = Modifier.weight(1f)
            )
            PlaylistCard(
                title = "Quick Reset",
                trackCount = "6 tracks",
                backgroundColor = pink,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))


    }
}

@Composable
fun CategoryCard(
    title: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    onclick : ()->Unit = {}
) {
    Card(
        modifier = Modifier
            .clickable(enabled = true, onClick = onclick)
            .width(100.dp)
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = iconColor
            )
        }
    }
}

@Composable
fun RecommendedItem(
    title: String,
    duration: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(backgroundColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = duration,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF7C3AED), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun PlaylistCard(
    title: String,
    trackCount: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = trackCount,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResourceHubPreview() {
    MaterialTheme {
        ResourceHubScreen()
    }
}


