
@file:OptIn(ExperimentalMaterial3Api::class)


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotScreen(modifier: Modifier = Modifier) {
    var messageText by remember { mutableStateOf("") }

    val purpleBlueGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6))
    )
    val purple = Color(0xFF8B5CF6)
    val lightPurple = Color(0xFFEDE9FE)
    val green = Color(0xFF10B981)
    val lightGray = Color(0xFFF9FAFB)
    val darkGray = Color(0xFF6B7280)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(lightGray)
    ) {


        // Chat Content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // AI Message 1
                AIMessage(
                    message = "Namaste! I'm here to help you with personalized wellness guidance. How are you feeling today?"
                )
            }

            item {
                // User Message
                UserMessage(
                    message = "I've been feeling quite stressed with my studies lately."
                )
            }

            item {
                // AI Message 2
                AIMessage(
                    message = "I understand. Based on your stress assessment, I'm preparing a personalized report. Would you like some immediate breathing exercises or shall we explore Ayurvedic stress relief techniques?"
                )
            }

            item {
                // Report Generation Card
                ReportGenerationCard()
            }
        }

        // Message Input
        MessageInputSection(
            messageText = messageText,
            onMessageChange = { messageText = it },
            onSendMessage = {
                // Handle send message
                messageText = ""
            },
            onVoiceMessage = {
                // Handle voice message
            }
        )

    }
}

@Composable
fun AIMessage(message: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        // AI Avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFF8B5CF6), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = "AI",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Message Bubble
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 4.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun UserMessage(message: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        // Message Bubble
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 4.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF8B5CF6)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // User Avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFF6B7280), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ReportGenerationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    imageVector = Icons.Default.Assignment,
                    contentDescription = "Report",
                    tint = Color(0xFF8B5CF6),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Generating Your Wellness Report",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Items
            ProgressItem(
                text = "Stress level analysis complete",
                isCompleted = true
            )

            ProgressItem(
                text = "Ayurvedic constitution assessment",
                isCompleted = true
            )

            ProgressItem(
                text = "Personalized diet recommendations",
                isCompleted = false,
                isInProgress = true
            )

            ProgressItem(
                text = "Exercise routine customization",
                isCompleted = false,
                isInProgress = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Send to WhatsApp when ready",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ProgressItem(
    text: String,
    isCompleted: Boolean,
    isInProgress: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            isCompleted -> {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(16.dp)
                )
            }
            isInProgress -> {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "In Progress",
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(16.dp)
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Gray.copy(alpha = 0.3f), CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isCompleted) Color.Black else Color.Gray,
            fontWeight = if (isCompleted) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
fun MessageInputSection(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onVoiceMessage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onVoiceMessage,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Message",
                    tint = Color(0xFF8B5CF6),
                    modifier = Modifier.size(20.dp)
                )
            }

            BasicTextField(
                value = messageText,
                onValueChange = onMessageChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                decorationBox = { innerTextField ->
                    if (messageText.isEmpty()) {
                        Text(
                            text = "Type your message or use voice...",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )

            IconButton(
                onClick = onSendMessage,
                modifier = Modifier.size(36.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFF8B5CF6), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AIWellnessCounselorPreview() {
    MaterialTheme {
        ChatBotScreen()
    }
}