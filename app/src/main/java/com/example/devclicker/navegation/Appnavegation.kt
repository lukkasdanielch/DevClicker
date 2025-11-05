package com.example.devclicker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Appnavigation() {
    val navController = rememberNavController()
    MainNavGraph(navController)
}
