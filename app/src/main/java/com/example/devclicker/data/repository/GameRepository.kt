package com.example.devclicker.data.repository

import com.example.devclicker.data.dao.JogadorDao
import com.example.devclicker.data.dao.UpgradeDao
import com.example.devclicker.data.model.Jogador
import com.example.devclicker.data.model.UpgradeComprado
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import kotlin.math.pow

/**
 * Nova classe para definir as propriedades de um upgrade na "loja".
 */
data class UpgradeDefinition(
    val id: String,
    val nome: String,
    val descricao: String,
    val baseCost: Long,
    val costIncreaseFactor: Double // Ex: 1.15 (15% mais caro por nível)
)

class GameRepository @Inject constructor(
    private val jogadorDao: JogadorDao,
    private val upgradeDao: UpgradeDao
) {

    // -----------------------------------------------------------------
    // Lógica de Upgrades (A "Loja")
    // -----------------------------------------------------------------

    private val upgradeDefinitions = listOf(
        UpgradeDefinition(
            id = "ppc_v1",
            nome = "Mouse Novo",
            descricao = "Aumenta pontos por clique",
            baseCost = 50,
            costIncreaseFactor = 1.15
        ),
        UpgradeDefinition(
            id = "pps_v1",
            nome = "Script Básico",
            descricao = "Gera 0.1 pontos por segundo por nível",
            baseCost = 200,
            costIncreaseFactor = 1.20
        ),
        UpgradeDefinition(
            id = "ppc_v2",
            nome = "Teclado Mecânico",
            descricao = "Aumenta muito pontos por clique",
            baseCost = 1000,
            costIncreaseFactor = 1.18
        ),
        UpgradeDefinition(
            id = "pps_v2",
            nome = "Stack Overflow",
            descricao = "Gera 1 ponto por segundo por nível",
            baseCost = 5000,
            costIncreaseFactor = 1.22
        )
    )

    suspend fun getUpgradeDefinitions(): List<UpgradeDefinition> {
        return upgradeDefinitions
    }

    // -----------------------------------------------------------------
    // Lógica de Negócio (Comprar)
    // -----------------------------------------------------------------

    /**
     * Lógica transacional para comprar N níveis de um upgrade.
     * (ESTA FUNÇÃO ESTÁ CORRIGIDA)
     */
    suspend fun buyUpgradeLevels(
        jogadorId: Int,
        upgradeId: String,
        levelsToBuy: Int,
        totalCost: Long
    ): Boolean {
        // 1. (CORREÇÃO) Pega o jogador usando a nova função suspend
        val jogador = jogadorDao.getJogador(jogadorId) ?: return false

        // 2. Verifica se tem dinheiro
        if (jogador.pontos < totalCost) {
            return false // Pontos insuficientes
        }

        // 3. Debita os pontos
        val novoJogador = jogador.copy(pontos = jogador.pontos - totalCost)
        jogadorDao.update(novoJogador)

        // 4. (CORREÇÃO) Pega o upgrade atual no banco
        val upgradeAtual = upgradeDao.getUpgrade(jogadorId, upgradeId) // <-- USA A NOVA FUNÇÃO

        if (upgradeAtual != null) {
            // Se já existe, ATUALIZA o nível (Soma o nível atual + os comprados)
            val upgradeAtualizado = upgradeAtual.copy(level = upgradeAtual.level + levelsToBuy)
            upgradeDao.update(upgradeAtualizado)
        } else {
            // Se é o primeiro, INSERE com o nível comprado
            val novoUpgrade = UpgradeComprado(
                jogadorId = jogadorId,
                upgradeId = upgradeId,
                level = levelsToBuy
            )
            upgradeDao.inserir(novoUpgrade)
        }
        return true // Sucesso
    }

    // -----------------------------------------------------------------
    // Lógica do Jogador e Upgrades (Banco de Dados Room)
    // -----------------------------------------------------------------

    fun getJogadorById(id: Int): Flow<Jogador?> {
        return jogadorDao.getJogadorById(id)
    }

    suspend fun updateJogador(jogador: Jogador) {
        jogadorDao.update(jogador)
    }

    fun getOwnedUpgrades(jogadorId: Int): Flow<List<UpgradeComprado>> {
        return upgradeDao.getUpgradesDoJogador(jogadorId)
    }

    suspend fun getCurrentJogadorId(): Int {
        var jogador = jogadorDao.getJogadorByNome("default_user")
        if (jogador == null) {
            jogador = Jogador(nome = "default_user", pontos = 10000L)
            jogadorDao.insert(jogador)
            jogador = jogadorDao.getJogadorByNome("default_user")
        }
        return jogador?.id ?: 1
    }
}