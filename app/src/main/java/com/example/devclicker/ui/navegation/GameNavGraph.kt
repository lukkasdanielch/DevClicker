package com.example.devclicker.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devclicker.MainActivity
import com.example.devclicker.ui.game.clicker.ClickerScreen
import com.example.devclicker.ui.game.settings.SettingsScreen
import com.example.devclicker.ui.game.settings.SettingsViewModel
import com.example.devclicker.ui.game.upgrades.UpgradesScreen
import com.example.devclicker.ui.navegation.BottomNavItem

@Composable
fun GameNavGraph(
    gameNavController: NavHostController,
    mainNavController: NavHostController,
    factory: MainActivity.AppViewModelFactory
) {
    NavHost(
        navController = gameNavController,
        startDestination = BottomNavItem.Clicker.route // Tela inicial é o Clicker
    ) {
        // Rota para a tela de clique
        composable(BottomNavItem.Clicker.route) {
            ClickerScreen(navController = mainNavController)
        }
        // Rota para a tela de upgrades
        composable(BottomNavItem.Upgrades.route) {
            UpgradesScreen(navController = mainNavController)
        }
        // Rota para a tela de configurações
        composable(BottomNavItem.Settings.route) {
            val viewModel: SettingsViewModel = viewModel(factory = factory)
            SettingsScreen(navController = mainNavController, viewModel = viewModel)
        }
    }
}