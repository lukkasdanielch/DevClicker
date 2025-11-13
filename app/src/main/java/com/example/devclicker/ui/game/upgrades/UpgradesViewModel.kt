package com.example.devclicker.ui.game.upgrades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.model.Jogador
import com.example.devclicker.data.model.UpgradeComprado
import com.example.devclicker.data.repository.GameRepository
import com.example.devclicker.data.repository.UpgradeDefinition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow

@HiltViewModel
class UpgradesViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    private val _jogadorId = flow { emit(gameRepository.getCurrentJogadorId()) }
    private val _jogadorFlow: Flow<Jogador?> = _jogadorId.flatMapLatest { gameRepository.getJogadorById(it) }
    private val _upgradesCompradosFlow: Flow<List<UpgradeComprado>> = _jogadorId.flatMapLatest { gameRepository.getOwnedUpgrades(it) }
    private val _upgradeDefinitionsFlow: Flow<List<UpgradeDefinition>> = flow {
        emit(gameRepository.getUpgradeDefinitions())
    }
    private val _termoPesquisa = MutableStateFlow("")
    private val _mensagemErro = MutableStateFlow<String?>(null)
    private val _mensagemSucesso = MutableStateFlow<String?>(null)
    private val _multiplier = MutableStateFlow<PurchaseMultiplier>(PurchaseMultiplier.ONE)

    private data class InteractionState(
        val pesquisa: String,
        val multiplier: PurchaseMultiplier,
        val erro: String?,
        val sucesso: String?
    )
    private val _dataFlow = combine(
        _jogadorFlow,
        _upgradesCompradosFlow,
        _upgradeDefinitionsFlow
    ) { jogador, comprados, definitions ->
        Triple(jogador, comprados, definitions)
    }

    private val _interactionFlow = combine(
        _termoPesquisa,
        _multiplier,
        _mensagemErro,
        _mensagemSucesso
    ) { pesquisa, multiplier, erro, sucesso ->
        InteractionState(pesquisa, multiplier, erro, sucesso)
    }

    val uiState: StateFlow<UpgradesUiState> = combine(
        _dataFlow,
        _interactionFlow
    ) { data, interaction ->

        val (jogador, comprados, definitions) = data
        val (pesquisa, multiplier, erro, sucesso) = interaction

        val pontos = jogador?.pontos ?: 0L

        val displayUpgrades = definitions
            .filter { it.nome.contains(pesquisa, ignoreCase = true) }
            .map { def ->
                val currentLevel = comprados.find { it.upgradeId == def.id }?.level ?: 0
                val (levelsToBuy, totalCost) = calculatePurchase(
                    pontos,
                    currentLevel,
                    def.baseCost,
                    def.costIncreaseFactor,
                    multiplier
                )

                DisplayUpgrade(
                    definition = def,
                    currentLevel = currentLevel,
                    levelsToBuy = levelsToBuy,
                    totalCost = totalCost,
                    canAfford = pontos >= totalCost && levelsToBuy > 0
                )
            }

        UpgradesUiState(
            jogadorPontos = pontos,
            displayUpgrades = displayUpgrades,
            selectedMultiplier = multiplier,
            termoPesquisa = pesquisa,
            isLoading = false,
            mensagemErro = erro,
            mensagemSucesso = sucesso
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UpgradesUiState(isLoading = true)
    )

    fun onSearchTermChanged(termo: String) {
        _termoPesquisa.value = termo
    }

    fun onMultiplierSelected(multiplier: PurchaseMultiplier) {
        _multiplier.value = multiplier
    }

    fun onBuyUpgrade(upgrade: DisplayUpgrade) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!upgrade.canAfford) {
                withContext(Dispatchers.Main) {
                    _mensagemErro.value = "Pontos insuficientes!"
                }
                return@launch
            }

            try {
                val jogadorId = _jogadorId.first()

                val sucesso = gameRepository.buyUpgradeLevels(
                    jogadorId = jogadorId,
                    upgradeId = upgrade.definition.id,
                    levelsToBuy = upgrade.levelsToBuy,
                    totalCost = upgrade.totalCost
                )

                withContext(Dispatchers.Main) {
                    if (sucesso) {
                        _mensagemSucesso.value = "${upgrade.definition.nome} Nv. ${upgrade.currentLevel + upgrade.levelsToBuy}!"
                    } else {
                        _mensagemErro.value = "Não foi possível comprar."
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _mensagemErro.value = e.message ?: "Erro desconhecido"
                }
            }
        }
    }

    fun onErrorMessageShown() { _mensagemErro.value = null }
    fun onSuccessMessageShown() { _mensagemSucesso.value = null }

    private fun calculateGeometricSeriesSum(baseCost: Long, factor: Double, currentLevel: Int, n: Int): Long {
        if (n <= 0) return 0L
        val firstLevelCost = baseCost * factor.pow(currentLevel)
        if (factor == 1.0) return (firstLevelCost * n).toLong()

        return (firstLevelCost * (factor.pow(n) - 1.0) / (factor - 1.0)).toLong()
    }

    private fun calculateMaxAffordableLevels(pontos: Long, baseCost: Long, factor: Double, currentLevel: Int): Int {
        if (pontos <= 0) return 0
        val firstLevelCost = baseCost * factor.pow(currentLevel)
        if (pontos < firstLevelCost) return 0
        if (factor == 1.0) return (pontos / firstLevelCost).toInt()

        val n = log(
            x = (pontos * (factor - 1.0) / firstLevelCost) + 1.0,
            base = factor
        )

        return floor(n).toInt()
    }

    private fun calculatePurchase(
        pontos: Long,
        currentLevel: Int,
        baseCost: Long,
        factor: Double,
        multiplier: PurchaseMultiplier
    ): Pair<Int, Long> {

        val levelsToBuy: Int = when (multiplier) {
            PurchaseMultiplier.ONE -> 1
            PurchaseMultiplier.TEN -> 10
            PurchaseMultiplier.HUNDRED -> 100
            PurchaseMultiplier.MAX -> calculateMaxAffordableLevels(
                pontos, baseCost, factor, currentLevel
            )
        }

        if (levelsToBuy == 0) return Pair(0, 0L)

        val totalCost = calculateGeometricSeriesSum(
            baseCost, factor, currentLevel, levelsToBuy
        )

        if (multiplier != PurchaseMultiplier.MAX && pontos < totalCost) {
            val affordableLevels = calculateMaxAffordableLevels(pontos, baseCost, factor, currentLevel)
            if (affordableLevels < levelsToBuy) {
                return Pair(levelsToBuy, totalCost)
            }
        }
        if (multiplier == PurchaseMultiplier.ONE && pontos < totalCost) {
            return Pair(levelsToBuy, totalCost)
        }

        return Pair(levelsToBuy, totalCost)
    }
}