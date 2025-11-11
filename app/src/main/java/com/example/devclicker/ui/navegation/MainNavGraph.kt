package com.example.devclicker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devclicker.ui.auth.login.LoginScreen
import com.example.devclicker.ui.auth.signup.SignUpScreen

@Composable
fun MainNavGraph(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login_screen") {
            LoginScreen(navController)
        }
        composable("signup_screen") {
            SignUpScreen(navController)
        }
        composable("game_screen") {

        }
    }
}