package com.example.metalhealthapp.Screens.ControlScreen

import ChatBotScreen
import CounselorsScreen
import CreatePostScreen
import HomeScreen
import PeerSupportScreen
import WebViewScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.metalhealthapp.NavController.Screen
import com.example.resourcehub.ResourceHubScreen


@Composable
fun ControlScreen(navController: NavController,
                  modifier: Modifier = Modifier) {
    val selectedTab = remember { mutableStateOf("Home") }

    Scaffold(
        topBar ={
//            when(selectedTab.value){
//                "chatbot"->ChatBotTopBar()
//                "community"->CommunityTopBar()
//                "counsellor"->CounselorTopBar()
//                "resource_hub"->ResourceHubTopBar()
//            }

            when(selectedTab.value){
//                "chatbot"-> topBar(selectedTab.value)
                "Home"->topBar(selectedTab.value)
                "Community"-> topBar(selectedTab.value)
                "Counsellors"-> topBar(selectedTab.value)
                "Resource Hub"->topBar(selectedTab.value)
            }

//            ChatBotTopBar(selectedTab.value)
//            FlexibleTopAppBar(selectedTab.value)
        },
        bottomBar = {
            BottomNavigationBar(selectedTab.value,
                {selectedTab.value=it})
        }

    ) { paddingValues ->
        when (selectedTab.value) {
            "Home" -> HomeScreen(onStressCheckclick = {navController.navigate(Screen.STRESSCHECK.name)},modifier.padding(paddingValues))
            "AI Assist" -> WebViewScreen(modifier.padding(paddingValues))
            "Community"-> {
                    PeerSupportScreen(
                        viewModel = hiltViewModel(),
                        onCreatePostClick = { navController.navigate(route = Screen.CREATEPOST.name) },
                        navController = navController,
                        modifier = modifier.padding(paddingValues)
                    )
//                PeerSupportScreen(modifier.padding(paddingValues))
            }
            "Counsellors" -> CounselorsScreen(modifier.padding(paddingValues))
            "Resource Hub" -> ResourceHubScreen(modifier.padding(paddingValues))
        }
    }
}

@Composable
fun BottomNavigationBar(
    selected : String,
    onselected : (String)-> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        NavigationBarItem(
            selected = selected == "Home",
            onClick = { onselected("Home")},
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selected == "AI Assist",
            onClick = { onselected("AI Assist")},
            icon = {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "AI Helper"
                )
            },
            label = { Text("AI Helper") }
        )
        NavigationBarItem(
            selected = selected == "Community",
            onClick = {onselected("Community") },
            icon = {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Community"
                )
            },
            label = { Text("Community",
                overflow = TextOverflow.Ellipsis) }
        )
        NavigationBarItem(
            selected = selected == "Counsellors",
            onClick = {onselected("Counsellors") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Counselor"
                )
            },
            label = { Text("Counselor") }
        )
        NavigationBarItem(
            selected = selected == "Resource Hub",
            onClick = { onselected("Resource Hub")},
            icon = {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = "Resources"
                )
            },
            label = { Text("Resources") }
        )
    }
}

@Composable
fun ChatBotTopBar(title : String,modifier: Modifier = Modifier) {
    val purpleBlueGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6))
    )
    val purple = Color(0xFF8B5CF6)
    val lightPurple = Color(0xFFEDE9FE)
    val green = Color(0xFF10B981)
    val lightGray = Color(0xFFF9FAFB)
    val darkGray = Color(0xFF6B7280)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(purpleBlueGradient)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
//                        text = "AI Wellness Counselor",
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
//                    Text(
//                        text = "Ayurvedic & Modern Approach",
//                        fontSize = 12.sp,
//                        color = Color.White.copy(alpha = 0.8f)
//                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "EN",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CommunityTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Peer Support",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Row {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun CounselorTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Professional Counselors",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun ResourceHubTopBar(modifier: Modifier = Modifier) {
    // Header
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Resource Hub",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar(title : String,modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },

    )
}

@Composable
fun FlexibleTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    showLanguageSelector: Boolean = true,
    currentLanguage: String = "EN",
    onLanguageClick: () -> Unit = {},
    backgroundColor: Brush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6))
    ),
    contentColor: Color = Color.White,
    elevation: Dp = 4.dp,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = elevation,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    onBackClick?.let { backAction ->
                        IconButton(
                            onClick = backAction,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = contentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        subtitle?.let { sub ->
                            Text(
                                text = sub,
                                fontSize = 12.sp,
                                color = contentColor.copy(alpha = 0.8f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions()

                    if (showLanguageSelector) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onLanguageClick() }
                                .padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = contentColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentLanguage,
                                fontSize = 12.sp,
                                color = contentColor,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Dropdown",
                                tint = contentColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}