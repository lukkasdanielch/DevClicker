package com.example.devclicker.navigation

import androidx.compose.runtime.Composable
// 1. (A CORREÇÃO) Importe o 'hiltViewModel'
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// 2. (A CORREÇÃO) Remova o import antigo
// import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.devclicker.ui.auth.login.LoginScreen
import com.example.devclicker.ui.auth.login.LoginViewModel
import com.example.devclicker.ui.auth.signup.SignUpScreen
import com.example.devclicker.ui.auth.signup.SignUpViewModel
import com.example.devclicker.ui.game.game.GameScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Rotas de Autenticação
        composable("login_screen") {
            // 3. (A CORREÇÃO) Use 'hiltViewModel()'
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, viewModel)
        }
        composable("signup_screen") {
            // 4. (A CORREÇÃO) Use 'hiltViewModel()'
            val viewModel: SignUpViewModel = hiltViewModel()
            SignUpScreen(navController, viewModel)
        }

        composable("game_screen") {
            GameScreen(mainNavController = navController)
        }
    }
}