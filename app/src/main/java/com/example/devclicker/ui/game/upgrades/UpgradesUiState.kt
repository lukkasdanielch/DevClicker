package com.example.devclicker.ui.game.upgrades

import com.example.devclicker.data.model.Upgrade

data class UpgradeDisponivel(
    val id: String,
    val nome: String,
    val descricao: String,
    val preco: Long,
)

data class UpgradesUiState(
    val jogadorPontos: Long = 0,
    val upgradesDisponiveis: List<UpgradeDisponivel> = emptyList(),
    val upgradesComprados: List<Upgrade> = emptyList(),
    val termoPesquisa: String = "",
    val isLoading: Boolean = false,
    val mensagemErro: String? = null,
    val mensagemSucesso: String? = null
) {
    val upgradesParaComprar: List<UpgradeDisponivel>
        get() {
            val boughtApiIds = upgradesComprados.map { it.upgradeApiId }.toSet()
            return upgradesDisponiveis
                .filter { it.id !in boughtApiIds }
                .filter { it.nome.contains(termoPesquisa, ignoreCase = true) }
        }
}