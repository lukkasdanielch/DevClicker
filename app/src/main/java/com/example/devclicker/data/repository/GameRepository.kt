package com.example.devclicker.data.repository

import com.example.devclicker.data.dao.JogadorDao
import com.example.devclicker.data.dao.UpgradeDao
import com.example.devclicker.data.model.Jogador
import com.example.devclicker.data.model.UpgradeComprado // A Entidade do banco
import com.example.devclicker.ui.game.upgrades.UpgradeDisponivel // O Modelo da "Loja"
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

/**
 * Repositório principal do jogo (o "Cérebro").
 * Recebe os dois DAOs do Hilt.
 */
class GameRepository @Inject constructor(
    private val jogadorDao: JogadorDao,
    private val upgradeDao: UpgradeDao
) {

    // -----------------------------------------------------------------
    // Lógica de Upgrades (A "Loja")
    // -----------------------------------------------------------------

    private val upgradesDaLoja = listOf(
        UpgradeDisponivel(
            id = "1",
            nome = "Aumentar Taxa de Clique",
            descricao = "Aumenta seus pontos por clique em 1",
            preco = 100
        ),
        UpgradeDisponivel(
            id = "2",
            nome = "Multiplicador Básico",
            descricao = "Dobra seus pontos por segundo",
            preco = 500
        ),
        UpgradeDisponivel(
            id = "3",
            nome = "Ideia Genial",
            descricao = "Aumenta muito seus pontos por clique",
            preco = 2000
        ),
        UpgradeDisponivel(
            id = "4",
            nome = "Café Forte",
            descricao = "Melhora a velocidade de auto-clique",
            preco = 1000
        )
    )

    suspend fun getAvailableUpgrades(): List<UpgradeDisponivel> {
        return upgradesDaLoja
    }

    // -----------------------------------------------------------------
    // Lógica de Negócio (Comprar)
    // -----------------------------------------------------------------

    /**
     * Lógica para comprar um upgrade.
     * Retorna 'true' se a compra foi bem-sucedida.
     */
    suspend fun buyUpgrade(jogadorId: Int, upgrade: UpgradeDisponivel): Boolean {
        // 'upgrade' é o UpgradeDisponivel (da loja)

        val jogador = jogadorDao.getJogadorById(jogadorId).firstOrNull() ?: return false

        if (jogador.pontos >= upgrade.preco) {
            // 1. Debita os pontos
            val novoJogador = jogador.copy(pontos = jogador.pontos - upgrade.preco)
            jogadorDao.update(novoJogador)

            // 2. (A CORREÇÃO ESTÁ AQUI)
            // Salva o "UpgradeComprado" (a Entidade) no banco
            val upgradeComprado = UpgradeComprado(
                upgradeId = upgrade.id,
                nome = upgrade.nome,
                jogadorId = jogadorId,
                preco = upgrade.preco,        // <-- PARÂMETRO 'preco' ADICIONADO
                efeito = upgrade.descricao    // <-- PARÂMETRO 'efeito' ADICIONADO
            )
            upgradeDao.inserir(upgradeComprado) // <-- Esta é a linha 76

            return true // Compra bem-sucedida
        }
        return false // Pontos insuficientes
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