package com.example.devclicker.ui.game.clicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.model.Jogador
import com.example.devclicker.data.model.UpgradeComprado
import com.example.devclicker.data.repository.GameRepository
// 1. (NOVO) Importe o novo ficheiro que criámos
import com.example.devclicker.data.repository.GameData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClickerViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _jogadorId = flow { emit(gameRepository.getCurrentJogadorId()) }

    private val _jogadorFlow: Flow<Jogador?> = _jogadorId.flatMapLatest { id ->
        gameRepository.getJogadorById(id)
    }

    private val _upgradesCompradosFlow: Flow<List<UpgradeComprado>> = _jogadorId.flatMapLatest { id ->
        gameRepository.getOwnedUpgrades(id)
    }

    private val _consoleLines = MutableStateFlow<List<String>>(emptyList())

    private val _pontosFlutuantesAcumulados = MutableStateFlow(0.0)

    val uiState: StateFlow<ClickerUiState> = combine(
        _jogadorFlow,
        _upgradesCompradosFlow,
        _consoleLines,
        _pontosFlutuantesAcumulados
    ) { jogador, upgradesComprados, consoleLines, pontosFlutuantes ->

        val (ppc, pps) = calculateGameMechanics(upgradesComprados)

        val pontosBaseDoBanco = jogador?.pontos?.toDouble() ?: 0.0

        ClickerUiState(
            pontos = pontosBaseDoBanco + pontosFlutuantes,
            pontosPorSegundo = pps,
            pontosPorClique = ppc,
            consoleLines = consoleLines
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ClickerUiState()
    )


    private fun calculateGameMechanics(upgrades: List<UpgradeComprado>): Pair<Long, Double> {
        var totalPPC = 1L
        var totalPPS = 0.0

        for (upgrade in upgrades) {
            when (upgrade.upgradeId) {
                "ppc_v1" -> { totalPPC += (1L * upgrade.level) }
                "ppc_v2" -> { totalPPC += (5L * upgrade.level) }
                "pps_v1" -> { totalPPS += (0.1 * upgrade.level) }
                "pps_v2" -> { totalPPS += (1.0 * upgrade.level) }
            }
        }
        return Pair(totalPPC, totalPPS)
    }

    init {
        viewModelScope.launch {
            while (true) {
                delay(100)

                val pps = uiState.value.pontosPorSegundo
                if (pps <= 0.0) continue

                val pontosGanhosNesteTick = pps / 10.0
                _pontosFlutuantesAcumulados.update { it + pontosGanhosNesteTick }
                val pontosInteirosParaSalvar = _pontosFlutuantesAcumulados.value.toLong()

                if (pontosInteirosParaSalvar >= 1L) {
                    val jogador = _jogadorFlow.firstOrNull() ?: continue

                    val novoJogador = jogador.copy(
                        pontos = jogador.pontos + pontosInteirosParaSalvar
                    )
                    gameRepository.updateJogador(novoJogador)

                    _pontosFlutuantesAcumulados.update { it - pontosInteirosParaSalvar }
                }
            }
        }
    }

    fun onDevClicked() {
        viewModelScope.launch {
            val jogador = _jogadorFlow.firstOrNull() ?: return@launch
            val pontosGanhos = uiState.value.pontosPorClique

            val novoJogador = jogador.copy(
                pontos = jogador.pontos + pontosGanhos
            )

            gameRepository.updateJogador(novoJogador)

            val snippet = GameData.codeSnippets.random()
            _consoleLines.update { currentLines ->
                (currentLines + snippet).takeLast(50)
            }
        }
    }
}