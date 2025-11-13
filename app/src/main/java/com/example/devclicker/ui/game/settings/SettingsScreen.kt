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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

@Composable
fun SettingsScreen(
    mainNavController: NavHostController,
    viewModel: SettingsViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Tela de Configurações", color = Color.White)

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.onLogoutClicked()
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
            Text("Sair")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text("Desenvolvido por Allan, Lucas, Malik e Yago",color = Color.White)
    }
}