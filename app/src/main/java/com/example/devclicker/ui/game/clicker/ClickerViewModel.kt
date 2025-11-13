package com.example.devclicker.ui.game.clicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.model.Jogador
import com.example.devclicker.data.model.UpgradeComprado
import com.example.devclicker.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClickerViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    // --- Fontes de Dados Internas ---

    private val _jogadorId = flow { emit(gameRepository.getCurrentJogadorId()) }

    private val _jogadorFlow: Flow<Jogador?> = _jogadorId.flatMapLatest { id ->
        gameRepository.getJogadorById(id)
    }

    private val _upgradesCompradosFlow: Flow<List<UpgradeComprado>> = _jogadorId.flatMapLatest { id ->
        gameRepository.getOwnedUpgrades(id)
    }

    private val _consoleLines = MutableStateFlow<List<String>>(emptyList())

    // (NOVO) Este StateFlow vai guardar os pontos fracionários
    // que ainda não foram salvos no banco.
    private val _pontosFlutuantesAcumulados = MutableStateFlow(0.0)

    private val codeSnippets = listOf(
        "fun main() { ... }", "System.out.println(\"Hello!\");", "const [count, setCount] ...",
        "@Composable", "SELECT * FROM users;", "import pandas as pd", "git commit -m \"fix\"",
        "if (user != null) { ... }", "</div>", "data class User(...)", "viewModelScope.launch { ... }"
    )

    // --- Estado Público (UiState) ---

    // (CORREÇÃO) Combina os QUATRO fluxos
    val uiState: StateFlow<ClickerUiState> = combine(
        _jogadorFlow,
        _upgradesCompradosFlow,
        _consoleLines,
        _pontosFlutuantesAcumulados // Adiciona o novo acumulador
    ) { jogador, upgradesComprados, consoleLines, pontosFlutuantes ->

        val (ppc, pps) = calculateGameMechanics(upgradesComprados)

        // Os pontos no banco (Long) são convertidos para Double
        val pontosBaseDoBanco = jogador?.pontos?.toDouble() ?: 0.0

        ClickerUiState(
            // Os pontos que o jogador vê são a soma do banco + os fracionários
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


    // --- Game Loop (CORRIGIDO para PPS) ---
    init {
        viewModelScope.launch {
            while (true) {
                delay(100) // Roda 10x por segundo

                val pps = uiState.value.pontosPorSegundo
                if (pps <= 0.0) continue // Não faz nada se não houver PPS

                // 1. Calcula os pontos ganhos neste "tick" (ex: 0.1 pps -> 0.01 ganho)
                val pontosGanhosNesteTick = pps / 10.0

                // 2. Adiciona os pontos ganhos ao acumulador fracionário
                _pontosFlutuantesAcumulados.update { it + pontosGanhosNesteTick }

                // 3. Verifica se o acumulador tem pontos inteiros para salvar
                // (ex: se o acumulador está em 1.5, isto será 1L)
                val pontosInteirosParaSalvar = _pontosFlutuantesAcumulados.value.toLong()

                if (pontosInteirosParaSalvar >= 1L) {
                    // 4. Pega o jogador ATUAL do banco
                    val jogador = _jogadorFlow.firstOrNull() ?: continue

                    // 5. Salva os pontos inteiros no banco
                    val novoJogador = jogador.copy(
                        pontos = jogador.pontos + pontosInteirosParaSalvar
                    )
                    gameRepository.updateJogador(novoJogador)

                    // 6. Subtrai os pontos que acabamos de salvar do acumulador
                    // (ex: 1.5 - 1 = 0.5)
                    _pontosFlutuantesAcumulados.update { it - pontosInteirosParaSalvar }
                }
            }
        }
    }

    // --- Funções de Evento (Chamadas pela UI) ---

    /**
     * Chamado toda vez que o usuário clica no botão principal.
     * (Esta função já está correta e não mexe no console)
     */
    fun onDevClicked() {
        viewModelScope.launch {
            // Pega o estado ATUAL do jogador (do flow)
            val jogador = _jogadorFlow.firstOrNull() ?: return@launch
            val pontosGanhos = uiState.value.pontosPorClique

            // Cria uma cópia do jogador com os novos pontos
            val novoJogador = jogador.copy(
                pontos = jogador.pontos + pontosGanhos
            )

            // Manda o repositório SALVAR o jogador no banco (Room)
            gameRepository.updateJogador(novoJogador)

            // Atualiza o console (estado local)
            val snippet = codeSnippets.random()
            _consoleLines.update { currentLines ->
                (currentLines + snippet).takeLast(50) // Mantém só as últimas 50
            }
        }
    }
}