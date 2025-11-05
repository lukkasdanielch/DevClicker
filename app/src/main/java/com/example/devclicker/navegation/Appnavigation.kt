package com.example.devclicker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Appnavigation() {
    val navController = rememberNavController()

    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) {
        "game_screen"
    } else {
        "login_screen"
    }

    MainNavGraph(navController = navController, startDestination = startDestination)
}