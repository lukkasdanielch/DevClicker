package com.example.devclicker.ui.game.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.devclicker.R
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
// import com.google.firebase.auth.FirebaseAuth // <-- REMOVA ESTE IMPORT (ViewModel cuida disso)

@Composable
fun SettingsScreen(
    // 1. (A CORREÇÃO) Mude o nome do parâmetro para 'mainNavController'
    mainNavController: NavHostController,
    viewModel: SettingsViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Tela de Configurações / Admin")

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // 2. (CORREÇÃO) Chame a função correta no ViewModel
                viewModel.onLogoutClicked()

                // 3. (CORREÇÃO) Use o 'mainNavController' para navegar
                mainNavController.navigate("login_screen") {
                    popUpTo("game_screen") {
                        inclusive = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text("Sair (Logout)")
        }
    }
}