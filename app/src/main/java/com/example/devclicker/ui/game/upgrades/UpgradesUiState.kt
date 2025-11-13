package com.example.devclicker.ui.game.upgrades

import com.example.devclicker.data.repository.UpgradeDefinition


sealed class PurchaseMultiplier {
    abstract val label: String

    object ONE : PurchaseMultiplier() { override val label = "1x" }
    object TEN : PurchaseMultiplier() { override val label = "10x" }
    object HUNDRED : PurchaseMultiplier() { override val label = "100x" }
    object MAX : PurchaseMultiplier() { override val label = "Max" }
}
data class DisplayUpgrade(
    val definition: UpgradeDefinition,
    val currentLevel: Int,
    val levelsToBuy: Int,
    val totalCost: Long,
    val canAfford: Boolean
)

data class UpgradesUiState(
    val jogadorPontos: Long = 0,

    val displayUpgrades: List<DisplayUpgrade> = emptyList(),

    val selectedMultiplier: PurchaseMultiplier = PurchaseMultiplier.ONE,

    val termoPesquisa: String = "",
    val isLoading: Boolean = false,
    val mensagemErro: String? = null,
    val mensagemSucesso: String? = null
)