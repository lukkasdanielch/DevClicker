package com.example.devclicker.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devclicker.MainActivity
import com.example.devclicker.ui.game.clicker.ClickerScreen
import com.example.devclicker.ui.game.clicker.ClickerViewModel
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
    val sharedViewModel: ClickerViewModel = viewModel(
        factory = factory
    )
    NavHost(
        navController = gameNavController,
        startDestination = BottomNavItem.Clicker.route
    ) {
        composable(BottomNavItem.Clicker.route) {
            ClickerScreen(viewModel = sharedViewModel)
        }
        composable(BottomNavItem.Upgrades.route) {
            UpgradesScreen(viewModel = sharedViewModel)
        }
        composable(BottomNavItem.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel(factory = factory)
            SettingsScreen(
                navController = mainNavController,
                viewModel = settingsViewModel
            )
        }
    }
}