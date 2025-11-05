package com.example.devclicker.ui.game.upgrades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UpgradesScreen(viewModel: UpgradesViewModel = viewModel()) {

    // Observa o estado do ViewModel
    val uiState by viewModel.upgradesState.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val state = uiState) {
            is UpgradesUiState.Loading -> {
                CircularProgressIndicator()
            }
            is UpgradesUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(state.upgrades) { upgrade ->
                        // Apenas um texto simples de exemplo
                        Text(text = "${upgrade.name}: ${upgrade.cost} moedas")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            is UpgradesUiState.Error -> {
                // Agora, se houver um erro, ele aparecerá na tela!
                Text(text = "Erro: ${state.message}")
            }
        }
    }
}