package com.example.devclicker.ui.game.game

import android.annotation.SuppressLint
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.devclicker.MainActivity
import com.example.devclicker.navigation.GameNavGraph
import com.example.devclicker.ui.navegation.BottomNavItem

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameScreen(
    mainNavController: NavHostController,
    factory: MainActivity.AppViewModelFactory
) {
    val gameNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = gameNavController) }
    ) {
        GameNavGraph(
            gameNavController = gameNavController,
            mainNavController = mainNavController,
            factory = factory
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Clicker,
        BottomNavItem.Upgrades,
        BottomNavItem.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}