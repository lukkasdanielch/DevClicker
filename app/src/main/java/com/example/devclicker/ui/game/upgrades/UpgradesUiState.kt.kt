package com.example.devclicker.ui.game.upgrades

import com.example.devclicker.data.model.Upgrade
// Note: Não importamos UpgradeApi aqui.

// Modelo de upgrade disponível, já que a entidade UpgradeApi não deve ser referenciada
data class UpgradeDisponivel(
    val id: String, // ID único
    val nome: String,
    val descricao: String,
    val preco: Long,
)

data class UpgradesUiState(
    val jogadorPontos: Long = 0,
    // Lista de upgrades que o repositório nos entrega (agora UpgradeDisponivel)
    val upgradesDisponiveis: List<UpgradeDisponivel> = emptyList(),
    // Lista de upgrades que vieram do Room
    val upgradesComprados: List<Upgrade> = emptyList(),
    val termoPesquisa: String = "",
    val isLoading: Boolean = false,
    val mensagemErro: String? = null,
    val mensagemSucesso: String? = null
) {
    // Lista filtrada que exclui upgrades já comprados e aplica o filtro de pesquisa
    val upgradesParaComprar: List<UpgradeDisponivel>
        get() {
            val boughtApiIds = upgradesComprados.map { it.upgradeApiId }.toSet()
            return upgradesDisponiveis
                .filter { it.id !in boughtApiIds }
                .filter { it.nome.contains(termoPesquisa, ignoreCase = true) }
        }
}