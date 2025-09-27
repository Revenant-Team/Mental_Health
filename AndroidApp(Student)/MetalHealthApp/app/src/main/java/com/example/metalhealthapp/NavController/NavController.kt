package com.example.metalhealthapp.NavController

import CreatePostScreen
import HomeScreen
import SignUpScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.metalhealthapp.Screens.AuthPages.SignIn.SignInScreen
import com.example.metalhealthapp.Screens.ControlScreen.ControlScreen
import com.example.metalhealthapp.Screens.HomeScreen.StressCheckScreen
import com.example.metalhealthapp.Screens.HomeScreen.StressCheckScreenFun
import com.example.metalhealthapp.Screens.PeerSupportScreen.PostDetailCard
import com.example.metalhealthapp.Screens.PeerSupportScreen.PostDetailScreen
import com.example.metalhealthapp.Utils.TokenManager
import com.example.metalhealthapp.Utils.extractUserIdFromJWT

@Composable
fun NavController(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val navController = rememberNavController()
    NavHost(navController, Screen.SIGNIN.name){
        composable (route = Screen.SIGNUP.name){
            SignUpScreen(viewModel = hiltViewModel(),
                onNavigateToSignIn = {navController.navigate(Screen.SIGNIN.name)},
                onSignUp = { navController.navigate(Screen.CONTROLSCREEN.name)})
        }
        composable(route = Screen.SIGNIN.name) {
            SignInScreen(viewModel = hiltViewModel(),
                onNavigateToSignUp = {navController.navigate(Screen.SIGNUP.name)},
                onSignIn = {navController.navigate(Screen.CONTROLSCREEN.name)})
        }
        composable(route = Screen.CONTROLSCREEN.name) {
            ControlScreen(navController)
        }
        composable (route = Screen.CREATEPOST.name){
            CreatePostScreen(viewModel = hiltViewModel(),
                onBackClick = {navController.navigate(Screen.CONTROLSCREEN.name)},
                onPostClick = {navController.navigate(Screen.CONTROLSCREEN.name)})
        }
        composable(route = Screen.STRESSCHECK.name) {
            StressCheckScreenFun(onNavigateBack = {navController.navigate(Screen.CONTROLSCREEN.name)})
        }

        composable (route = "${Screen.POSTDETAILSCREEN.name}/{postId}"){
            backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")
            val token = TokenManager(context).getToken()
            val userid = extractUserIdFromJWT(token?:"")
            PostDetailScreen(
                postId = postId ?: "",
                viewModel = hiltViewModel(),
                userid?:""
            )
        }
    }
}