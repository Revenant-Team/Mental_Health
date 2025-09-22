package com.example.metalhealthapp.NavController

import CreatePostScreen
import HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.metalhealthapp.Screens.ControlScreen.ControlScreen
import com.example.metalhealthapp.Screens.HomeScreen.StressCheckScreen
import com.example.metalhealthapp.Screens.HomeScreen.StressCheckScreenFun

@Composable
fun NavController(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val navController = rememberNavController()
    NavHost(navController, Screen.CONTROLSCREEN.name){
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
    }
}