package com.example.devclicker.ui.game.upgrades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun UpgradesScreen(viewModel: UpgradesViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.mensagemErro) {
        uiState.mensagemErro?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.mensagemSucesso) {
        uiState.mensagemSucesso?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pontos Atuais: ${uiState.jogadorPontos}",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = uiState.termoPesquisa,
                    onValueChange = viewModel::onSearchTermChange,
                    label = { Text("Pesquisar Upgrades") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Pesquisar") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                if (uiState.isLoading && uiState.upgradesDisponiveis.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                "Upgrades Disponíveis (${uiState.upgradesParaComprar.size})",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                        }
                        if (uiState.upgradesParaComprar.isEmpty() && uiState.termoPesquisa.isNotBlank()) {
                            item {
                                Text("Nenhum upgrade encontrado para '${uiState.termoPesquisa}'.", Modifier.padding(16.dp))
                            }
                        } else if (uiState.upgradesParaComprar.isEmpty() && uiState.termoPesquisa.isBlank()) {
                            item {
                                Text("Todos os upgrades disponíveis foram comprados.", Modifier.padding(16.dp))
                            }
                        }
                        items(uiState.upgradesParaComprar, key = { it.id }) { upgrade ->
                            UpgradeItem(
                                upgrade = upgrade,
                                isAffordable = uiState.jogadorPontos >= upgrade.preco,
                                onBuyClick = { viewModel.buyUpgrade(upgrade) }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UpgradeItem(
    upgrade: UpgradeDisponivel, // Usa o tipo local
    isAffordable: Boolean,
    onBuyClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isAffordable) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFFFEEEE)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = upgrade.nome, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = upgrade.descricao, style = MaterialTheme.typography.bodySmall)
                Text(text = "Preço: ${upgrade.preco} Pontos", style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isAffordable) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error
                ))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onBuyClick,
                enabled = isAffordable,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAffordable) MaterialTheme.colorScheme.primary else Color.Gray
                )
            ) {
                Text("Comprar")
            }
        }
    }
}