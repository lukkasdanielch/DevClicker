package com.example.devclicker.ui.game.upgrades

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.devclicker.R
import com.example.devclicker.ui.game.clicker.ClickerViewModel

@Composable
fun UpgradesScreen(
    viewModel: ClickerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

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

        Text("Tela de Upgrades")
        Text("Pontos atuais: ${uiState.pontos}")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { viewModel.onComprarUpgrades("click") }) {
            Text("Comprar Upgrade de Clique (Custo: 50)")
        }
    }
}