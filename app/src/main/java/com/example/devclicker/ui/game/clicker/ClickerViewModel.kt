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

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                val pontosGanhos = _uiState.value.pontosPorSegundo
                if (pontosGanhos > 0) {
                    _uiState.update { currentState ->
                        currentState.copy(pontos = currentState.pontos + pontosGanhos)
                    }
                }
            }
        }
    }

    fun onDevClicked() {
        _uiState.update { currentState ->
            currentState.copy(
                pontos = currentState.pontos + currentState.pontosPorClique
            )
        }
    }

    fun onComprarUpgrades(tipo: String) {
        val custoUpgrade = 50
        val estadoAtual = _uiState.value

        if (estadoAtual.pontos >= custoUpgrade) {
            _uiState.update {
                it.copy(
                    pontos = it.pontos - custoUpgrade,

                    pontosPorClique = it.pontosPorClique + 1,

                    pontosPorSegundo = it.pontosPorSegundo + 5
                )
            }
        }
    }
}