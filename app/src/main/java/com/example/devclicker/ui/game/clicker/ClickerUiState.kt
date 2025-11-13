package com.example.devclicker.ui.game.clicker

/**
 * Representa TODO o estado da tela de clique.
 * Esta versão está COMPLETA.
 */
data class ClickerUiState(
    val pontos: Double = 0.0,
    val pontosPorSegundo: Double = 0.0,
    val pontosPorClique: Long = 1,
    val consoleLines: List<String> = emptyList()
)