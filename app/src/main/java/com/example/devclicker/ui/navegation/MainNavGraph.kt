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
import com.example.devclicker.ui.game.clicker.ClickerScreen
import com.example.devclicker.ui.game.game.GameScreen
import com.example.devclicker.ui.game.settings.SettingsScreen
import com.example.devclicker.ui.game.settings.SettingsViewModel
import com.example.devclicker.ui.game.upgrades.UpgradesScreen

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

        // Rotas do Jogo

        composable("game_screen") {
            GameScreen(navController = navController)
        }

        composable("clicker_screen") {
            ClickerScreen(navController = navController)
        }

        composable("upgrades_screen") {
            UpgradesScreen(navController = navController)
        }

        composable("settings_screen") {
            val viewModel: SettingsViewModel = viewModel(factory = factory)
            SettingsScreen(navController = navController, viewModel = viewModel)
        }
    }
}