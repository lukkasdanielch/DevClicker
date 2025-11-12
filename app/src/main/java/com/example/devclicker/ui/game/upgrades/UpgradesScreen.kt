package com.example.devclicker.ui.game.upgrades

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devclicker.R
import com.example.devclicker.ui.game.clicker.ClickerViewModel

@Composable
fun UpgradesScreen(
    viewModel: ClickerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    // Pega o estado do multiplicador
    val multiplier by viewModel.multiplier.collectAsState()

    // Custos base (para mostrar na UI)
    val custoClique = 50
    val custoPps = 100

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

        Text("Tela de Upgrades", fontSize = 24.sp)
        Text(String.format("Pontos atuais: %.1f", uiState.pontos))
        Text(String.format("Pontos p/ segundo: %.1f", uiState.pontosPorSegundo))

        Spacer(modifier = Modifier.height(20.dp))

        // Botões do Multiplicador
        Text("Multiplicador de Compra:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            val selectedColor = MaterialTheme.colorScheme.primary
            val defaultColor = MaterialTheme.colorScheme.secondaryContainer

            Button(
                onClick = { viewModel.setMultiplier(1) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (multiplier == 1) selectedColor else defaultColor
                )
            ) { Text("1x") }

            Button(
                onClick = { viewModel.setMultiplier(10) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (multiplier == 10) selectedColor else defaultColor
                )
            ) { Text("10x") }

            Button(
                onClick = { viewModel.setMultiplier(100) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (multiplier == 100) selectedColor else defaultColor
                )
            ) { Text("100x") }
        }

        // Botões de Compra Atualizados
        Button(onClick = { viewModel.onComprarUpgrades("click") }) {
            // Texto dinâmico
            Text("Comprar ${multiplier}x Clique (Custo: ${custoClique * multiplier})")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.onComprarUpgrades("pps") }) {
            // Texto dinâmico
            val beneficioFormatado = String.format("%.1f", 0.1 * multiplier)
            Text("Comprar ${multiplier}x PPS (+$beneficioFormatado) (Custo: ${custoPps * multiplier})")
        }
    }
}