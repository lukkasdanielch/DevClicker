package com.example.devclicker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devclicker.ui.auth.login.LoginScreen
import com.example.devclicker.ui.auth.signup.SignUpScreen
import com.example.devclicker.ui.game.clicker.ClickerScreen // Importar
import com.example.devclicker.ui.game.game.GameScreen
import com.example.devclicker.ui.game.settings.SettingsScreen // Importar
import com.example.devclicker.ui.game.upgrades.UpgradesScreen // Importar

@Composable
fun MainNavGraph(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Rotas de Autenticação
        composable("login_screen") {
            LoginScreen(navController)
        }
        composable("signup_screen") {
            SignUpScreen(navController)
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
            SettingsScreen(navController = navController)
        }
    }
}