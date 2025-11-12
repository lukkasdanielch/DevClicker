package com.example.devclicker.ui.game.upgrades // <-- Pacote da UI

import com.example.devclicker.data.model.UpgradeComprado // <-- Importa o @Entity do banco

/**
 * 1. Modelo de dados SÓ PARA A TELA (UI Model).
 * Representa um upgrade na "loja".
 * Ele vive aqui, junto com o UiState.
 */
data class UpgradeDisponivel(
    val id: String,
    val nome: String,
    val descricao: String,
    val preco: Long,
)

/**
 * 2. O Estado da Tela (UI State).
 * Ele "possui" e usa o UpgradeDisponivel.
 */
data class UpgradesUiState(
    val jogadorPontos: Long = 0,
    val upgradesDisponiveis: List<UpgradeDisponivel> = emptyList(), // <-- Usa o modelo da UI
    val upgradesComprados: List<UpgradeComprado> = emptyList(), // <-- Usa o modelo do Banco
    val termoPesquisa: String = "",
    val isLoading: Boolean = false,
    val mensagemErro: String? = null,
    val mensagemSucesso: String? = null
) {
    val upgradesParaComprar: List<UpgradeDisponivel>
        get() {
            val boughtApiIds = upgradesComprados.map { it.upgradeId }.toSet()
            return upgradesDisponiveis
                .filter { it.id !in boughtApiIds }
                .filter { it.nome.contains(termoPesquisa, ignoreCase = true) }
        }
}