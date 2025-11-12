package com.example.devclicker.ui.game.clicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.model.Jogador
import com.example.devclicker.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // 1. Habilitado para Hilt
class ClickerViewModel @Inject constructor(
    private val gameRepository: GameRepository // 2. Recebe o repositório
) : ViewModel() {

    // --- Fontes de Dados Internas ---

    // 3. Busca o ID do jogador (do repositório)
    private val _jogadorId = flow { emit(gameRepository.getCurrentJogadorId()) }

    // 4. Observa o Jogador (Flow) do banco de dados Room
    private val _jogadorFlow: Flow<Jogador?> = _jogadorId.flatMapLatest { id ->
        gameRepository.getJogadorById(id)
    }

    // 5. Estado local apenas para as linhas do console
    private val _consoleLines = MutableStateFlow<List<String>>(emptyList())

    // Lista de fragmentos de código para o console
    private val codeSnippets = listOf(
        "fun main() { ... }", "System.out.println(\"Hello!\");", "const [count, setCount] ...",
        "@Composable", "SELECT * FROM users;", "import pandas as pd", "git commit -m \"fix\"",
        "if (user != null) { ... }", "</div>", "data class User(...)", "viewModelScope.launch { ... }"
    )

    // --- Estado Público (UiState) ---

    // 6. Combina os dados do banco (Jogador) com os dados locais (Console)
    val uiState: StateFlow<ClickerUiState> = combine(
        _jogadorFlow,
        _consoleLines
    ) { jogador, consoleLines ->
        ClickerUiState(
            pontos = jogador?.pontos?.toDouble() ?: 0.0,
            // TODO: A lógica de PPS e PPC deve vir dos upgrades comprados
            pontosPorSegundo = 0.0,
            pontosPorClique = 1L,
            consoleLines = consoleLines
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ClickerUiState()
    )

    // --- Game Loop (Para Pontos Por Segundo) ---
    init {
        // Game Loop para Pontos por Segundo
        viewModelScope.launch {
            while (true) {
                delay(100) // Roda 10x por segundo
                val pontosGanhos = uiState.value.pontosPorSegundo / 10.0

                if (pontosGanhos > 0) {
                    // Pega o jogador atual
                    val jogador = _jogadorFlow.firstOrNull() ?: continue

                    // Cria a cópia com novos pontos (convertendo Double para Long)
                    val novoJogador = jogador.copy(
                        pontos = (jogador.pontos + pontosGanhos).toLong()
                    )

                    // Salva no banco
                    gameRepository.updateJogador(novoJogador)
                }
            }
        }
    }

    // --- Funções de Evento (Chamadas pela UI) ---

    /**
     * Chamado toda vez que o usuário clica no botão principal.
     */
    fun onDevClicked() {
        viewModelScope.launch {
            // 7. Pega o estado ATUAL do jogador (do flow)
            val jogador = _jogadorFlow.firstOrNull() ?: return@launch
            val pontosGanhos = uiState.value.pontosPorClique

            // 8. Cria uma cópia do jogador com os novos pontos
            val novoJogador = jogador.copy(
                pontos = jogador.pontos + pontosGanhos
            )

            // 9. Manda o repositório SALVAR o jogador no banco (Room)
            gameRepository.updateJogador(novoJogador)

            // 10. Atualiza o console (estado local)
            val snippet = codeSnippets.random()
            _consoleLines.update { currentLines ->
                (currentLines + snippet).takeLast(50) // Mantém só as últimas 50
            }
        }
    }

    // 11. A lógica de 'comprar' foi movida para o UpgradesViewModel
}