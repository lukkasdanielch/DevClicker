package com.example.devclicker.ui.game.upgrades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpgradesScreen(
    navController: NavHostController,
    viewModel: UpgradesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ... (Os LaunchedEffect para as snackbars estão corretos, pode manter) ...
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

            Text(
                text = String.format("Pontos: %d", uiState.jogadorPontos),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // (NOVO) Botões de Multiplicador
            MultiplierToggle(
                selected = uiState.selectedMultiplier,
                onSelected = { viewModel.onMultiplierSelected(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = uiState.termoPesquisa,
                onValueChange = { viewModel.onSearchTermChanged(it) },
                label = { Text("Procurar upgrade...") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // (ATUALIZADO) Usa a nova lista 'displayUpgrades'
                    items(uiState.displayUpgrades, key = { it.definition.id }) { upgrade ->
                        UpgradeItem(
                            upgrade = upgrade,
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
 * (NOVO) Composable para os botões 1x, 10x, 100x, Max
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiplierToggle(
    selected: PurchaseMultiplier,
    onSelected: (PurchaseMultiplier) -> Unit
) {
    val options = listOf(
        PurchaseMultiplier.ONE,
        PurchaseMultiplier.TEN,
        PurchaseMultiplier.HUNDRED,
        PurchaseMultiplier.MAX
    )

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEach { multiplier ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = options.indexOf(multiplier),
                    count = options.size
                ),
                onClick = { onSelected(multiplier) },
                selected = (multiplier == selected)
            ) {
                Text(multiplier.label)
            }
        }
    }
}


/**
 * (ATUALIZADO) Composable para mostrar o item de upgrade
 */
@Composable
fun UpgradeItem(
    upgrade: DisplayUpgrade,
    onBuyClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = upgrade.definition.nome, style = MaterialTheme.typography.titleMedium)
                Text(text = upgrade.definition.descricao, style = MaterialTheme.typography.bodySmall)
                // Mostra o nível atual
                Text(
                    text = "Nível: ${upgrade.currentLevel}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onBuyClick,
                enabled = upgrade.canAfford // Habilita só se puder pagar
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Mostra o custo
                    Text(text = "Custo: ${upgrade.totalCost}")
                    // Mostra quantos vai comprar
                    Text(
                        text = "Comprar ${upgrade.levelsToBuy} Nv.",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}