package com.example.devclicker.data.repository

import com.example.devclicker.data.dao.JogadorDao
import com.example.devclicker.data.dao.UpgradeDao
import com.example.devclicker.data.model.Jogador
import com.example.devclicker.data.model.UpgradeComprado
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import kotlin.math.pow

data class UpgradeDefinition(
    val id: String,
    val nome: String,
    val descricao: String,
    val baseCost: Long,
    val costIncreaseFactor: Double
)

class GameRepository @Inject constructor(
    private val jogadorDao: JogadorDao,
    private val upgradeDao: UpgradeDao
) {
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

    suspend fun buyUpgradeLevels(
        jogadorId: Int,
        upgradeId: String,
        levelsToBuy: Int,
        totalCost: Long
    ): Boolean {
        val jogador = jogadorDao.getJogador(jogadorId) ?: return false

        if (jogador.pontos < totalCost) {
            return false
        }

        val novoJogador = jogador.copy(pontos = jogador.pontos - totalCost)
        jogadorDao.update(novoJogador)

        val upgradeAtual = upgradeDao.getUpgrade(jogadorId, upgradeId)

        if (upgradeAtual != null) {
            val upgradeAtualizado = upgradeAtual.copy(level = upgradeAtual.level + levelsToBuy)
            upgradeDao.update(upgradeAtualizado)
        } else {
            val novoUpgrade = UpgradeComprado(
                jogadorId = jogadorId,
                upgradeId = upgradeId,
                level = levelsToBuy
            )
            upgradeDao.inserir(novoUpgrade)
        }
        return true // Sucesso
    }
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