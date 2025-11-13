package com.example.devclicker.ui.game.upgrades

// A importação do modelo do banco mudou
import com.example.devclicker.data.repository.UpgradeDefinition

/**
 * 1. (NOVO) Representa o multiplicador de compra selecionado.
 * Usamos 'object' para 1x, 10x, 100x e 'object' para Max.
 */
sealed class PurchaseMultiplier {
    abstract val label: String

    object ONE : PurchaseMultiplier() { override val label = "1x" }
    object TEN : PurchaseMultiplier() { override val label = "10x" }
    object HUNDRED : PurchaseMultiplier() { override val label = "100x" }
    object MAX : PurchaseMultiplier() { override val label = "Max" }
}

/**
 * 2. (NOVO) Modelo de dados SÓ PARA A TELA (UI Model).
 * Contém o upgrade da loja + o progresso do jogador
 */
data class DisplayUpgrade(
    val definition: UpgradeDefinition, // O que é (baseCost, nome, etc)
    val currentLevel: Int,             // O nível que o jogador tem
    val levelsToBuy: Int,              // Quantos níveis ele vai comprar (1, 10, 100, ou Max)
    val totalCost: Long,               // O custo total para comprar 'levelsToBuy'
    val canAfford: Boolean             // Se o jogador pode pagar
)

/**
 * 3. O Estado da Tela (UI State) ATUALIZADO.
 */
data class UpgradesUiState(
    val jogadorPontos: Long = 0,

    // Esta é a lista principal que a UI vai mostrar
    val displayUpgrades: List<DisplayUpgrade> = emptyList(),

    // O multiplicador que o usuário selecionou
    val selectedMultiplier: PurchaseMultiplier = PurchaseMultiplier.ONE,

    val termoPesquisa: String = "",
    val isLoading: Boolean = false,
    val mensagemErro: String? = null,
    val mensagemSucesso: String? = null
)