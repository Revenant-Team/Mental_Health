package com.example.metalhealthapp.NavController

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.metalhealthapp.Screens.ControlScreen.ControlScreen

@Composable
fun NavController(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, Screen.CONTROLSCREEN.name){
        composable(route = Screen.CONTROLSCREEN.name) {
            ControlScreen()
        }
    }
}