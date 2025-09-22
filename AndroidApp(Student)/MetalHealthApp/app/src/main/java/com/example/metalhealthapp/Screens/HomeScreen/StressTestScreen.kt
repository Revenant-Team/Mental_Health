package com.example.metalhealthapp.Screens.HomeScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data Classes
data class Assessment(
    val id: String,
    val title: String,
    val description: String,
    val questionCount: Int,
    val estimatedTime: String,
    val icon: String,
    val color: Color,
    val type: AssessmentType
)

data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val scores: List<Int>
)

data class AssessmentResult(
    val assessmentId: String,
    val totalScore: Int,
    val severity: String,
    val interpretation: String
)

enum class AssessmentType {
    DEPRESSION, ANXIETY, STRESS, BURNOUT, PTSD, SUBSTANCE_USE
}

@Preview(showSystemUi = true)
// Main Composable
@Composable
fun StressCheckScreenFun(
    onNavigateBack: () -> Unit = {}
) {
    var currentScreen by remember { mutableStateOf(StressCheckScreen.SELECTION) }
    var selectedAssessment by remember { mutableStateOf<Assessment?>(null) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var answers by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }


    when (currentScreen) {
        StressCheckScreen.SELECTION -> {
            AssessmentSelectionScreen(
                onNavigateBack = onNavigateBack,
                onAssessmentSelected = { assessment ->
                    selectedAssessment = assessment
                    currentQuestionIndex = 0
                    answers=emptyMap()
                    currentScreen = StressCheckScreen.QUESTIONS
                }
            )
        }

        StressCheckScreen.QUESTIONS -> {
            selectedAssessment?.let { assessment ->
                QuestionScreen(
                    assessment = assessment,
                    currentQuestionIndex = currentQuestionIndex,
                    totalQuestions = assessment.questionCount,
                    onAnswerSelected = { questionIndex, answerIndex ->
                        answers = answers.toMutableMap().apply {
                            this[questionIndex] = answerIndex
                        }
                    },
                    onNextQuestion = {
                        if (currentQuestionIndex < assessment.questionCount - 1) {
                            currentQuestionIndex++
                        } else {
                            currentScreen = StressCheckScreen.RESULTS
                        }
                    },
                    onNavigateBack = {
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex--
                        } else {
                            currentScreen = StressCheckScreen.SELECTION
                        }
                    },
                    selectedAnswer = answers[currentQuestionIndex]
                )
            }
        }

        StressCheckScreen.RESULTS -> {
            selectedAssessment?.let { assessment ->
                ResultsScreen(
                    assessment = assessment,
                    answers = answers,
                    onRetakeTest = {
                        currentQuestionIndex = 0
                        answers=emptyMap()
                        currentScreen = StressCheckScreen.QUESTIONS
                    },
                    onSelectNewTest = {
                        currentScreen = StressCheckScreen.SELECTION
                    },
                    onNavigateBack = onNavigateBack
                )
            }
        }
    }
}

enum class StressCheckScreen {
    SELECTION, QUESTIONS, RESULTS
}

// Assessment Selection Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentSelectionScreen(
    onNavigateBack: () -> Unit,
    onAssessmentSelected: (Assessment) -> Unit
) {
    val assessments = remember { getAvailableAssessments() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Stress Check",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Choose an assessment to evaluate your current mental health status",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(assessments) { assessment ->
                AssessmentCard(
                    assessment = assessment,
                    onClick = { onAssessmentSelected(assessment) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentCard(
    assessment: Assessment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Top accent line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(assessment.color, assessment.color.copy(alpha = 0.6f))
                        ),
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.Top
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    assessment.color,
                                    assessment.color.copy(alpha = 0.8f)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = assessment.icon,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = assessment.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3748)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = assessment.description,
                        fontSize = 13.sp,
                        color = Color(0xFF718096),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Meta information
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color(0xFFA0AEC0),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = assessment.estimatedTime,
                                fontSize = 12.sp,
                                color = Color(0xFFA0AEC0)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (assessment.questionCount) {
                                in 1..10 -> Color(0xFFC6F6D5)
                                else -> Color(0xFFFED7AA)
                            }
                        ) {
                            Text(
                                text = "${assessment.questionCount} questions",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = when (assessment.questionCount) {
                                    in 1..10 -> Color(0xFF276749)
                                    else -> Color(0xFFC05621)
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFFA0AEC0),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// Question Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    assessment: Assessment,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    onAnswerSelected: (Int, Int) -> Unit,
    onNextQuestion: () -> Unit,
    onNavigateBack: () -> Unit,
    selectedAnswer: Int?
) {
    val questions = remember { getQuestionsForAssessment(assessment.id) }
    val currentQuestion = questions[currentQuestionIndex]
    val progress = (currentQuestionIndex + 1).toFloat() / totalQuestions

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        assessment.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Progress Bar
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = assessment.color,
                trackColor = Color(0xFFE2E8F0),
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Question ${currentQuestionIndex + 1} of $totalQuestions",
                    fontSize = 14.sp,
                    color = Color(0xFF718096)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Question Text
            Text(
                text = currentQuestion.text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D3748),
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Options
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(currentQuestion.options) { index, option ->
                    OptionCard(
                        text = option,
                        isSelected = selectedAnswer == index,
                        onClick = {
                            onAnswerSelected(currentQuestionIndex, index)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Next Button
            Button(
                onClick = onNextQuestion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = selectedAnswer != null,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = assessment.color
                )
            ) {
                Text(
                    text = if (currentQuestionIndex == totalQuestions - 1) "Finish" else "Next Question",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun OptionCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
//            .clickable(enabled = true, onClick = onClick)
            .fillMaxWidth(),

        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) {
            BorderStroke(2.dp, Color(0xFF4299E1))
        } else {
            BorderStroke(2.dp, Color(0xFFE2E8F0))
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFEBF8FF) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                      .clickable(onClick = onClick)
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radio button
            Box(
                Modifier
                    .size(20.dp)
                    .border(
                        2.dp,
                        if (isSelected) Color(0xFF4299E1) else Color(0xFFCBD5E0),
                        CircleShape
                    ), Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                Color(0xFF4299E1),
                                CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                fontSize = 16.sp,
                color = Color(0xFF2D3748)
            )
        }
    }
}

// Results Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    assessment: Assessment,
    answers: Map<Int, Int>,
    onRetakeTest: () -> Unit,
    onSelectNewTest: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val result = remember { calculateResult(assessment, answers) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Results",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ResultCard(
                    assessment = assessment,
                    result = result
                )
            }

            item {
                InterpretationCard(result.interpretation)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetakeTest,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Retake Test")
                    }

                    Button(
                        onClick = onSelectNewTest,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = assessment.color
                        )
                    ) {
                        Text("New Test")
                    }
                }
            }
        }
    }
}

@Composable
fun ResultCard(
    assessment: Assessment,
    result: AssessmentResult
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = assessment.color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = assessment.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = result.totalScore.toString(),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = assessment.color
            )

            Text(
                text = result.severity,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF718096)
            )
        }
    }
}

@Composable
fun InterpretationCard(interpretation: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "What this means",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = interpretation,
                fontSize = 14.sp,
                color = Color(0xFF718096),
                lineHeight = 20.sp
            )
        }
    }
}

// Data Functions
fun getAvailableAssessments(): List<Assessment> {
    return listOf(
        Assessment(
            id = "phq9",
            title = "PHQ-9 Depression",
            description = "Assess symptoms of depression over the past two weeks",
            questionCount = 9,
            estimatedTime = "2-3 min",
            icon = "😔",
            color = Color(0xFF4299E1),
            type = AssessmentType.DEPRESSION
        ),
        Assessment(
            id = "gad7",
            title = "GAD-7 Anxiety",
            description = "Measure generalized anxiety disorder symptoms",
            questionCount = 7,
            estimatedTime = "2 min",
            icon = "😰",
            color = Color(0xFF48BB78),
            type = AssessmentType.ANXIETY
        ),
        Assessment(
            id = "dass21",
            title = "DASS-21 Scale",
            description = "Comprehensive depression, anxiety, and stress assessment",
            questionCount = 21,
            estimatedTime = "5 min",
            icon = "😣",
            color = Color(0xFFED8936),
            type = AssessmentType.STRESS
        ),
        Assessment(
            id = "pss10",
            title = "Perceived Stress",
            description = "Evaluate how stressful situations affect your life",
            questionCount = 10,
            estimatedTime = "3 min",
            icon = "😵",
            color = Color(0xFF9F7AEA),
            type = AssessmentType.STRESS
        ),
        Assessment(
            id = "bdi2",
            title = "Beck Depression Inventory",
            description = "Comprehensive depression severity assessment",
            questionCount = 21,
            estimatedTime = "5 min",
            icon = "💙",
            color = Color(0xFF3182CE),
            type = AssessmentType.DEPRESSION
        ),
        Assessment(
            id = "mbi",
            title = "Burnout Assessment",
            description = "Evaluate workplace burnout and exhaustion levels",
            questionCount = 22,
            estimatedTime = "4 min",
            icon = "🔥",
            color = Color(0xFFE53E3E),
            type = AssessmentType.BURNOUT
        ),
        Assessment(
            id = "phq2",
            title = "Quick Depression Screen",
            description = "Brief 2-question depression screening tool",
            questionCount = 2,
            estimatedTime = "30 sec",
            icon = "⚡",
            color = Color(0xFF38B2AC),
            type = AssessmentType.DEPRESSION
        ),
        Assessment(
            id = "pcptsd5",
            title = "PTSD Screening",
            description = "Screen for post-traumatic stress symptoms",
            questionCount = 5,
            estimatedTime = "1 min",
            icon = "💭",
            color = Color(0xFF805AD5),
            type = AssessmentType.PTSD
        )
    )
}

fun getQuestionsForAssessment(assessmentId: String): List<Question> {
    return when (assessmentId) {

        "phq9" -> listOf(
            Question("phq9_1", "Over the last 2 weeks, how often have you been bothered by little interest or pleasure in doing things?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("phq9_2", "Over the last 2 weeks, how often have you been bothered by feeling down, depressed, or hopeless?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("phq9_3", "Over the last 2 weeks, how often have you had trouble falling or staying asleep, or sleeping too much?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("phq9_4", "Over the last 2 weeks, how often have you felt tired or had little energy?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("phq9_5", "Over the last 2 weeks, how often have you had poor appetite or overeating?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("phq9_6", "Over the last 2 weeks, how often have you felt bad about yourself — or that you are a failure or have let yourself or your family down?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("phq9_7", "Over the last 2 weeks, how often have you had trouble concentrating on things, such as reading the newspaper or watching television?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("phq9_8", "Over the last 2 weeks, how often have you been moving or speaking so slowly that other people could have noticed? Or the opposite — being so fidgety or restless that you have been moving around a lot more than usual?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("phq9_9", "Over the last 2 weeks, how often have you had thoughts that you would be better off dead or of hurting yourself in some way?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3))
        )

        "gad7" -> listOf(
            Question("gad7_1", "Feeling nervous, anxious, or on edge?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("gad7_2", "Not being able to stop or control worrying?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("gad7_3", "Worrying too much about different things?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("gad7_4", "Trouble relaxing?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("gad7_5", "Being so restless that it's hard to sit still?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("gad7_6", "Becoming easily annoyed or irritable?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("gad7_7", "Feeling afraid as if something awful might happen?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3))
        )

        "dass21" -> List(21) { i ->
            Question("dass21_${i+1}", "DASS-21 Question ${i+1}", listOf("Did not apply to me","Applied to me to some degree","Applied to me a considerable degree","Applied to me very much"), listOf(0,1,2,3))
        }

        "pss10" -> List(10) { i ->
            Question("pss10_${i+1}", "In the last month, how often have you felt stressed about situation ${i+1}?", listOf("Never","Almost never","Sometimes","Fairly often","Very often"), listOf(0,1,2,3,4))
        }

        "bdi2" -> List(21) { i ->
            Question("bdi2_${i+1}", "BDI-2 Question ${i+1}", listOf("0","1","2","3"), listOf(0,1,2,3))
        }

        "mbi" -> List(22) { i ->
            Question("mbi_${i+1}", "Burnout Question ${i+1}", listOf("Never","Rarely","Sometimes","Often","Always"), listOf(0,1,2,3,4))
        }

        "phq2" -> listOf(
            Question("phq2_1", "Over the last 2 weeks, have you felt little interest or pleasure in doing things?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3)),
            Question("phq2_2", "Over the last 2 weeks, have you felt down, depressed, or hopeless?", listOf("Not at all","Several days","More than half the days","Nearly every day"), listOf(0,1,2,3))
        )

        "pcptsd5" -> listOf(
            Question("pcptsd5_1", "Had nightmares or unwanted memories about a traumatic event?", listOf("Not at all","Once in a while","Often","Very often"), listOf(0,1,2,3)),
            Question("pcptsd5_2", "Avoided thoughts or feelings about a traumatic event?", listOf("Not at all","Once in a while","Often","Very often"), listOf(0,1,2,3)),
            Question("pcptsd5_3", "Felt constantly on guard, watchful, or easily startled?", listOf("Not at all","Once in a while","Often","Very often"), listOf(0,1,2,3)),
            Question("pcptsd5_4", "Felt numb or detached from people, activities, or surroundings?", listOf("Not at all","Once in a while","Often","Very often"), listOf(0,1,2,3)),
            Question("pcptsd5_5", "Felt guilt or blame about the traumatic event?", listOf("Not at all","Once in a while","Often","Very often"), listOf(0,1,2,3))
        )

        else -> emptyList()
    }
}


fun calculateResult(assessment: Assessment, answers: Map<Int, Int>): AssessmentResult {
    val questions = getQuestionsForAssessment(assessment.id)
    var totalScore = 0

    answers.forEach { (questionIndex, answerIndex) ->
        if (questionIndex < questions.size && answerIndex < questions[questionIndex].scores.size) {
            totalScore += questions[questionIndex].scores[answerIndex]
        }
    }

    val (severity, interpretation) = when (assessment.id) {
        "phq9" -> when (totalScore) {
            in 0..4 -> "Minimal" to "Your responses suggest minimal depression symptoms."
            in 5..9 -> "Mild" to "Your responses suggest mild depression symptoms."
            in 10..14 -> "Moderate" to "Your responses suggest moderate depression symptoms."
            in 15..19 -> "Moderately Severe" to "Your responses suggest moderately severe depression symptoms."
            else -> "Severe" to "Your responses suggest severe depression symptoms."
        }
        else -> "Normal" to "Assessment completed successfully."
    }

    return AssessmentResult(
        assessmentId = assessment.id,
        totalScore = totalScore,
        severity = severity,
        interpretation = interpretation
    )
}