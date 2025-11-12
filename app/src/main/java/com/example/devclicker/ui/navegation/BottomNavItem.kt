package com.example.devclicker.ui.navegation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Clicker : BottomNavItem(
        route = "clicker_screen",
        title = "Clicker",
        icon = Icons.Default.Home
    )
    object Upgrades : BottomNavItem(
        route = "upgrades_screen",
        title = "Upgrades",
        icon = Icons.Default.List
    )
    object Settings : BottomNavItem(
        route = "settings_screen",
        title = "Config",
        icon = Icons.Default.Settings
    )
}