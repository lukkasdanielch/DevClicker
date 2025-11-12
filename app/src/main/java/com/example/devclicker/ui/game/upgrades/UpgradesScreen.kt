package com.example.devclicker.ui.game.upgrades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController // 1. IMPORTE O NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpgradesScreen(
    // 2. CORREÇÃO: A tela agora aceita os dois parâmetros
    navController: NavHostController,
    viewModel: UpgradesViewModel
) {
    // 3. O 'uiState' agora vem do 'UpgradesViewModel'
    val uiState by viewModel.uiState.collectAsState()

    // 4. Setup da Snackbar para mostrar erros e sucessos
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.mensagemErro) {
        uiState.mensagemErro?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            viewModel.onErrorMessageShown()
        }
    }
    LaunchedEffect(uiState.mensagemSucesso) {
        uiState.mensagemSucesso?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            viewModel.onSuccessMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 5. Mostra os pontos do jogador (do UiState)
            Text(
                text = String.format("Pontos: %d", uiState.jogadorPontos),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 6. Barra de Pesquisa (do UiState)
            TextField(
                value = uiState.termoPesquisa,
                onValueChange = { viewModel.onSearchTermChanged(it) },
                label = { Text("Procurar upgrade...") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 7. Lista de Upgrades
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 8. Usa a lista filtrada 'upgradesParaComprar' (do UiState)
                    items(uiState.upgradesParaComprar) { upgrade ->
                        UpgradeItem(
                            upgrade = upgrade,
                            podeComprar = uiState.jogadorPontos >= upgrade.preco,
                            onBuyClick = {
                                viewModel.onBuyUpgrade(upgrade)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Um Composable auxiliar para mostrar um item da lista de upgrades.
 */
@Composable
fun UpgradeItem(
    upgrade: UpgradeDisponivel,
    podeComprar: Boolean,
    onBuyClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = upgrade.nome, style = MaterialTheme.typography.titleMedium)
                Text(text = upgrade.descricao, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onBuyClick, enabled = podeComprar) {
                Text(text = "Custo: ${upgrade.preco}")
            }
        }
    }
}