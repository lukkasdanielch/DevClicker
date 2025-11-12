package com.example.devclicker.ui.game.clicker

data class ClickerUiState(
    val pontos: Double = 0.0,
    val pontosPorSegundo: Double = 0.0,
    val pontosPorClique: Long = 1,
    // ADICIONE ESTA LINHA
    val consoleLines: List<String> = emptyList()
)