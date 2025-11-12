package com.example.devclicker.ui.game.clicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.repository.GameRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClickerViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClickerUiState())
    val uiState = _uiState.asStateFlow()

    // Estado do Multiplicador
    private val _multiplier = MutableStateFlow(1) // 1x por padrão
    val multiplier = _multiplier.asStateFlow()

    // Lista de fragmentos de código para o console
    private val codeSnippets = listOf(
        "fun main() { ... }",
        "System.out.println(\"Hello!\");",
        "const [count, setCount] = useState(0);",
        "@Composable",
        "SELECT * FROM users;",
        "import pandas as pd",
        "git commit -m \"fix\"",
        "if (user != null) { ... }",
        "</div>",
        "data class User(val id: Int, val name: String)",
        "viewModelScope.launch { ... }",
        "await fetchUserData();",
        "x = 5; y = 10; z = x + y;"
    )

    init {
        // Game Loop para Pontos por Segundo
        viewModelScope.launch {
            while (true) {
                delay(100) // Roda 10x por segundo
                val pontosGanhos = _uiState.value.pontosPorSegundo / 10.0
                if (pontosGanhos > 0) {
                    _uiState.update { currentState ->
                        currentState.copy(pontos = currentState.pontos + pontosGanhos)
                    }
                }
            }
        }
    }

    // Função para mudar o multiplicador
    fun setMultiplier(value: Int) {
        _multiplier.value = value
    }

    // Função chamada ao clicar
    fun onDevClicked() {
        _uiState.update { currentState ->
            val newPoints = currentState.pontos + currentState.pontosPorClique

            // Pega um fragmento aleatório
            val snippet = codeSnippets.random()

            // Adiciona o fragmento à lista, mantendo apenas os últimos 50
            val newLines = (currentState.consoleLines + snippet).takeLast(50)

            currentState.copy(
                pontos = newPoints,
                consoleLines = newLines // Salva a nova lista no estado
            )
        }
    }

    // Função para comprar upgrades
    fun onComprarUpgrades(tipo: String) {
        val estadoAtual = _uiState.value
        val amountToBuy = _multiplier.value // Quantidade a comprar (1, 10, 100)

        if (tipo == "click") {
            val custoUnitario = 50
            val custoTotal = custoUnitario * amountToBuy // Custo para N upgrades
            val beneficioTotal = 1 * amountToBuy        // Benefício de N upgrades

            if (estadoAtual.pontos >= custoTotal) {
                _uiState.update {
                    it.copy(
                        pontos = it.pontos - custoTotal,
                        pontosPorClique = it.pontosPorClique + beneficioTotal
                    )
                }
            }
        } else if (tipo == "pps") {
            val custoUnitario = 100
            val custoTotal = custoUnitario * amountToBuy
            val beneficioTotal = 0.1 * amountToBuy // Benefício (Double)

            if (estadoAtual.pontos >= custoTotal) {
                _uiState.update {
                    it.copy(
                        pontos = it.pontos - custoTotal,
                        pontosPorSegundo = it.pontosPorSegundo + beneficioTotal
                    )
                }
            }
        }
    }
}