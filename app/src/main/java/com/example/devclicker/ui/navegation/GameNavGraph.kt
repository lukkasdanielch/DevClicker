package com.example.devclicker.navigation

import androidx.compose.runtime.Composable
// 1. (A CORREÇÃO) Importe o 'hiltViewModel'
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// 2. (A CORREÇÃO) Remova o import antigo
// import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.devclicker.ui.game.clicker.ClickerScreen
import com.example.devclicker.ui.game.clicker.ClickerViewModel
import com.example.devclicker.ui.game.settings.SettingsScreen
import com.example.devclicker.ui.game.settings.SettingsViewModel
import com.example.devclicker.ui.game.upgrades.UpgradesScreen
import com.example.devclicker.ui.game.upgrades.UpgradesViewModel
import com.example.devclicker.ui.navegation.BottomNavItem

@Composable
fun GameNavGraph(
    gameNavController: NavHostController,
    mainNavController: NavHostController
) {
    NavHost(
        navController = gameNavController,
        startDestination = BottomNavItem.Clicker.route
    ) {
        composable(BottomNavItem.Clicker.route) {
            // 3. (A CORREÇÃO) Use 'hiltViewModel()'
            val viewModel: ClickerViewModel = hiltViewModel()
            ClickerScreen(navController = gameNavController, viewModel = viewModel)
        }
        composable(BottomNavItem.Upgrades.route) {
            // 4. (A CORREÇÃO) Use 'hiltViewModel()'
            val viewModel: UpgradesViewModel = hiltViewModel()
            UpgradesScreen(navController = gameNavController, viewModel = viewModel)
        }
        composable(BottomNavItem.Settings.route) {
            // 5. (A CORREÇÃO) Use 'hiltViewModel()'
            val viewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                mainNavController = mainNavController,
                viewModel = viewModel
            )
        }
    }
}