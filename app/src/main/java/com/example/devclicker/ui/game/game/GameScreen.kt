package com.example.devclicker.ui.game.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun GameScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Menu Principal (GameScreen)")

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.navigate("clicker_screen") }) {
            Text("Jogar (Clicker Screen)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("upgrades_screen") }) {
            Text("Upgrades (Upgrades Screen)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("settings_screen") }) {
            Text("Configurações (Settings Screen)")
        }
    }
}