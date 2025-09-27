import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.metalhealthapp.Model.UserProfile
import com.example.metalhealthapp.Model.UserSignInReq
import com.example.metalhealthapp.Model.UserSignUpReq
import com.example.metalhealthapp.Screens.AuthPages.SignUp.SignUpVM

// Theme colors for mental health app
private val SoftPurple = Color(0xFF8B5FBF)
private val LightPurple = Color(0xFFE8DDFF)
private val SoftBlue = Color(0xFF6B8DD6)
private val LightBlue = Color(0xFFE3F2FD)
private val SoftGreen = Color(0xFF7CB342)
private val LightGreen = Color(0xFFE8F5E8)
private val WarmPink = Color(0xFFFF8A95)
private val SoftOrange = Color(0xFFFFB74D)



@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpVM ,
    onNavigateToSignIn: () -> Unit = {},
    onSignUp : ()-> Unit
) {
    val user by viewModel.user.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    // Required fields
//    var email by remember { mutableStateOf("") }
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
//
//    // Optional fields
//    var instituteCode by remember { mutableStateOf("") }
//    var firstName by remember { mutableStateOf("") }
//    var lastName by remember { mutableStateOf("") }
//    var rollNumber by remember { mutableStateOf("") }
//    var course by remember { mutableStateOf("") }
//    var year by remember { mutableStateOf("") }
//    var department by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Error states
    var emailError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        LightBlue.copy(alpha = 0.3f),
                        LightPurple.copy(alpha = 0.2f),
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with animation
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(SoftBlue.copy(alpha = 0.8f), SoftPurple.copy(alpha = 0.6f))
                        )
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SelfImprovement,
                    contentDescription = "Join Community",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Join Our Community",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = SoftPurple,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Start your journey to better mental wellness",
                style = MaterialTheme.typography.bodyLarge,
                color = SoftPurple.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // Essential Information Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = SoftPurple.copy(alpha = 0.1f)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Icon(
                            Icons.Default.Security,
                            contentDescription = null,
                            tint = SoftPurple,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Essential Information",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SoftPurple
                        )
                    }

                    OutlinedTextField(
                        value = user.email,
                        onValueChange = {
                            viewModel.updateEmail(it)
                            emailError = null
                        },
                        label = { Text("Email Address", color = SoftPurple.copy(alpha = 0.8f)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = SoftPurple.copy(alpha = 0.6f)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = emailError != null,
                        supportingText = emailError?.let { { Text(it, color = WarmPink) } },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftPurple,
                            unfocusedBorderColor = SoftPurple.copy(alpha = 0.3f),
                            cursorColor = SoftPurple
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = user.username,
                        onValueChange = {
                            viewModel.updateUsername(it)
                            usernameError = null
                        },
                        label = { Text("Choose a Username", color = SoftPurple.copy(alpha = 0.8f)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = SoftPurple.copy(alpha = 0.6f)
                            )
                        },
                        isError = usernameError != null,
                        supportingText = usernameError?.let { { Text(it, color = WarmPink) } },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftPurple,
                            unfocusedBorderColor = SoftPurple.copy(alpha = 0.3f),
                            cursorColor = SoftPurple
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = user.password,
                        onValueChange = {
                            viewModel.updatePassword(it)
                            passwordError = null
                        },
                        label = { Text("Create Password", color = SoftPurple.copy(alpha = 0.8f)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = SoftPurple.copy(alpha = 0.6f)
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = passwordError != null,
                        supportingText = passwordError?.let { { Text(it, color = WarmPink) } },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = SoftPurple.copy(alpha = 0.6f)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftPurple,
                            unfocusedBorderColor = SoftPurple.copy(alpha = 0.3f),
                            cursorColor = SoftPurple
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = null
                        },
                        label = { Text("Confirm Password", color = SoftPurple.copy(alpha = 0.8f)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.LockOpen,
                                contentDescription = null,
                                tint = SoftPurple.copy(alpha = 0.6f)
                            )
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = confirmPasswordError != null,
                        supportingText = confirmPasswordError?.let { { Text(it, color = WarmPink) } },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = "Toggle confirm password visibility",
                                    tint = SoftPurple.copy(alpha = 0.6f)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftPurple,
                            unfocusedBorderColor = SoftPurple.copy(alpha = 0.3f),
                            cursorColor = SoftPurple
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Information Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = SoftBlue.copy(alpha = 0.1f)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Icon(
                            Icons.Default.School,
                            contentDescription = null,
                            tint = SoftBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Academic Profile (Optional)",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SoftBlue
                        )
                    }

                    OutlinedTextField(
                        value = user.instituteCode?:"",
                        onValueChange = {  viewModel.updateInstituteCode(it) },
                        label = { Text("Institute Code", color = SoftBlue.copy(alpha = 0.8f)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Business,
                                contentDescription = null,
                                tint = SoftBlue.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftBlue,
                            unfocusedBorderColor = SoftBlue.copy(alpha = 0.3f),
                            cursorColor = SoftBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = profile.firstName?:"",
                            onValueChange = { viewModel.updateFirstName(it) },
                            label = { Text("First Name", color = SoftBlue.copy(alpha = 0.8f)) },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoftBlue,
                                unfocusedBorderColor = SoftBlue.copy(alpha = 0.3f),
                                cursorColor = SoftBlue
                            )
                        )

                        OutlinedTextField(
                            value = profile.lastName?:"",
                            onValueChange = { viewModel.updateLastName(it) },
                            label = { Text("Last Name", color = SoftBlue.copy(alpha = 0.8f)) },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoftBlue,
                                unfocusedBorderColor = SoftBlue.copy(alpha = 0.3f),
                                cursorColor = SoftBlue
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = profile.rollNumber?:"",
                        onValueChange = { viewModel.updateRollNumber(it)  },
                        label = { Text("Roll Number", color = SoftBlue.copy(alpha = 0.8f)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Badge,
                                contentDescription = null,
                                tint = SoftBlue.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftBlue,
                            unfocusedBorderColor = SoftBlue.copy(alpha = 0.3f),
                            cursorColor = SoftBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = profile.course?:"",
                        onValueChange = { viewModel.updateCourse(it) },
                        label = { Text("Course/Program", color = SoftBlue.copy(alpha = 0.8f)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = SoftBlue.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftBlue,
                            unfocusedBorderColor = SoftBlue.copy(alpha = 0.3f),
                            cursorColor = SoftBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = profile.year?:"",
                            onValueChange = { viewModel.updateYear(it) },
                            label = { Text("Academic Year", color = SoftBlue.copy(alpha = 0.8f)) },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoftBlue,
                                unfocusedBorderColor = SoftBlue.copy(alpha = 0.3f),
                                cursorColor = SoftBlue
                            )
                        )

                        OutlinedTextField(
                            value = profile.department?:"",
                            onValueChange = { viewModel.updateDepartment(it) },
                            label = { Text("Department", color = SoftBlue.copy(alpha = 0.8f)) },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoftBlue,
                                unfocusedBorderColor = SoftBlue.copy(alpha = 0.3f),
                                cursorColor = SoftBlue
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val validationResult = validateSignUp(user.email, user.username, user.password, confirmPassword)
                    emailError = validationResult.emailError
                    usernameError = validationResult.usernameError
                    passwordError = validationResult.passwordError
                    confirmPasswordError = validationResult.confirmPasswordError

                    if (validationResult.isValid) {
                        viewModel.signUp(context, onSuccess = {
                             onSignUp()
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }) {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Brush.horizontalGradient(
                        colors = listOf(SoftPurple, SoftBlue)
                    ).let { SoftPurple }, // Fallback to single color since Brush isn't supported
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 12.dp,
                    pressedElevation = 16.dp
                )
            ) {
                AnimatedContent(
                    targetState = isLoading,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
                    }
                ) { loading ->
                    if (loading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                "Creating your account...",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Start Your Wellness Journey",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = onNavigateToSignIn,
                enabled = !isLoading
            ) {
                Text(
                    "Already part of our community? Welcome back",
                    color = SoftPurple,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Privacy and support message
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LightBlue.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Shield,
                            contentDescription = null,
                            tint = SoftBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Your Privacy & Safety",
                            style = MaterialTheme.typography.titleSmall,
                            color = SoftBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        "We prioritize your privacy and provide a safe, anonymous space for mental health support. Your data is encrypted and secure.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftBlue.copy(alpha = 0.8f),
                        lineHeight = MaterialTheme.typography.bodySmall.lineHeight * 1.2
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Validation data classes (unchanged)
data class SignInValidation(
    val isValid: Boolean,
    val emailError: String?,
    val passwordError: String?
)

data class SignUpValidation(
    val isValid: Boolean,
    val emailError: String?,
    val usernameError: String?,
    val passwordError: String?,
    val confirmPasswordError: String?
)

// Enhanced validation functions
fun validateSignIn(email: String, password: String): SignInValidation {
    val emailError = when {
        email.isBlank() -> "Email is required for your secure login"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email address"
        else -> null
    }

    val passwordError = when {
        password.isBlank() -> "Password is required to access your account"
        else -> null
    }

    return SignInValidation(
        isValid = emailError == null && passwordError == null,
        emailError = emailError,
        passwordError = passwordError
    )
}

fun validateSignUp(email: String, username: String, password: String, confirmPassword: String): SignUpValidation {
    val emailError = when {
        email.isBlank() -> "Email is required to create your account"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email address"
        else -> null
    }

    val usernameError = when {
        username.isBlank() -> "Username is required for your profile"
        username.length < 3 -> "Username should be at least 3 characters long"
        username.length > 20 -> "Username should be less than 20 characters"
        !username.matches(Regex("^[a-zA-Z0-9._-]+$")) -> "Username can only contain letters, numbers, dots, hyphens, and underscores"
        else -> null
    }

    val passwordError = when {
        password.isBlank() -> "Password is required to secure your account"
        password.length < 8 -> "Password should be at least 8 characters for better security"
        !password.any { it.isDigit() } -> "Password should contain at least one number"
        !password.any { it.isLetter() } -> "Password should contain at least one letter"
        else -> null
    }

    val confirmPasswordError = when {
        confirmPassword.isBlank() -> "Please confirm your password"
        password != confirmPassword -> "Passwords don't match. Please check again"
        else -> null
    }

    return SignUpValidation(
        isValid = emailError == null && usernameError == null && passwordError == null && confirmPasswordError == null,
        emailError = emailError,
        usernameError = usernameError,
        passwordError = passwordError,
        confirmPasswordError = confirmPasswordError
    )
}

// Composable for consistent theming
@Composable
fun MentalHealthAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = SoftPurple,
            secondary = SoftBlue,
            tertiary = SoftGreen,
            background = Color.White,
            surface = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onTertiary = Color.White,
            onBackground = SoftPurple,
            onSurface = SoftPurple
        )
    ) {
        content()
    }
}