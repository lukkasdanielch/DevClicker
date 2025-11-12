package com.example.devclicker.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devclicker.MainActivity
import com.example.devclicker.ui.auth.login.LoginScreen
import com.example.devclicker.ui.auth.login.LoginViewModel
import com.example.devclicker.ui.auth.signup.SignUpScreen
import com.example.devclicker.ui.auth.signup.SignUpViewModel
import com.example.devclicker.ui.game.game.GameScreen
@Composable
fun MainNavGraph(
    navController: NavHostController,
    startDestination: String,
    factory: MainActivity.AppViewModelFactory
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Rotas de Autenticação
        composable("login_screen") {
            val viewModel: LoginViewModel = viewModel(factory = factory)
            LoginScreen(navController, viewModel)
        }
        composable("signup_screen") {
            val viewModel: SignUpViewModel = viewModel(factory = factory)
            SignUpScreen(navController, viewModel)
        }

        // A rota "game_screen" agora carrega o contêiner
        composable("game_screen") {
            GameScreen(mainNavController = navController, factory = factory)
        }
    }
}