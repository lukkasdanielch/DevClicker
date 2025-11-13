package com.example.devclicker.ui.game.upgrades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.example.devclicker.ui.theme.matrixGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpgradesScreen(
    navController: NavHostController,
    viewModel: UpgradesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = String.format("DevPoints: %d", uiState.jogadorPontos),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            MultiplierToggle(
                selected = uiState.selectedMultiplier,
                onSelected = { viewModel.onMultiplierSelected(it) },
                activeColor = matrixGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = uiState.termoPesquisa,
                onValueChange = { viewModel.onSearchTermChanged(it) },
                label = { Text("Procurar upgrade...") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedIndicatorColor = matrixGreen,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = matrixGreen,
                    focusedContainerColor = Color.DarkGray.copy(alpha = 0.5f),
                    unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.5f)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(color = matrixGreen)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.displayUpgrades, key = { it.definition.id }) { upgrade ->
                        UpgradeItem(
                            upgrade = upgrade,
                            onBuyClick = {
                                viewModel.onBuyUpgrade(upgrade)
                            },
                            buttonColor = matrixGreen
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiplierToggle(
    selected: PurchaseMultiplier,
    onSelected: (PurchaseMultiplier) -> Unit,
    activeColor: Color
) {
    val options = listOf(
        PurchaseMultiplier.ONE,
        PurchaseMultiplier.TEN,
        PurchaseMultiplier.HUNDRED,
        PurchaseMultiplier.MAX
    )

    val buttonColors = SegmentedButtonDefaults.colors(
        activeContainerColor = activeColor,
        activeContentColor = Color.Black,
        inactiveContainerColor = Color.DarkGray.copy(alpha = 0.5f),
        inactiveContentColor = Color.White,
        inactiveBorderColor = Color.Gray
    )

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEach { multiplier ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = options.indexOf(multiplier),
                    count = options.size
                ),
                onClick = { onSelected(multiplier) },
                selected = (multiplier == selected),
                colors = buttonColors
            ) {
                Text(multiplier.label)
            }
        }
    }
}


@Composable
fun UpgradeItem(
    upgrade: DisplayUpgrade,
    onBuyClick: () -> Unit,
    buttonColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray.copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = upgrade.definition.nome,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = upgrade.definition.descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
                Text(
                    text = "Nível: ${upgrade.currentLevel}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onBuyClick,
                enabled = upgrade.canAfford,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.DarkGray
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Custo: ${upgrade.totalCost}")
                    Text(
                        text = "Comprar ${upgrade.levelsToBuy} Nv.",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}