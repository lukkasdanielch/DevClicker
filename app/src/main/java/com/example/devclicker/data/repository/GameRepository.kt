package com.example.devclicker.data.repository

import com.example.devclicker.data.dao.JogadorDao
import com.example.devclicker.data.dao.UpgradeDao
import com.example.devclicker.data.model.Jogador
import com.example.devclicker.data.model.OwnedUpgrade
import com.example.devclicker.data.model.UpgradeDisponivel // Modelo de dados local
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

// Repositório do Jogo (Fonte de dados LOCAL/Room)
class GameRepository(
    private val jogadorDao: JogadorDao,
    private val ownedUpgradeDao: UpgradeDao, // NOVO: DAO para upgrades comprados
) {

    // Simulação de dados de upgrades disponíveis LOCALMENTE (Substituindo a API)
    private val availableUpgrades = listOf(
        UpgradeDisponivel("1", "Aumentar Taxa de Clique", "Aumenta seus pontos por clique em 1", 100, 1, 1.0),
        UpgradeDisponivel("2", "Multiplicador Básico", "Dobra seus pontos por segundo", 500, 0, 2.0),
        UpgradeDisponivel("3", "Ideia Genial", "Aumenta muito seus pontos por clique", 2000, 5, 1.0),
        UpgradeDisponivel("4", "Café Forte", "Melhora a velocidade de auto-clique", 1000, 0, 1.5)
    )

    companion object {
        // Factory simplificado para inicializar o Repositório
        fun create(jogadorDao: JogadorDao, ownedUpgradeDao: UpgradeDao): GameRepository {
            return GameRepository(jogadorDao, ownedUpgradeDao)
        }
    }

    /** NOVO: Retorna a lista de upgrades disponíveis (dados locais) */
    suspend fun getAvailableUpgrades(): List<UpgradeDisponivel> {
        return availableUpgrades
    }

    /** NOVO: Expõe o Flow dos upgrades já comprados pelo jogador */
    fun getOwnedUpgrades(jogadorId: Int): Flow<List<OwnedUpgrade>> {
        return ownedUpgradeDao.getUpgradesDoJogador(jogadorId)
    }

    /** NOVO: Expõe o Flow do jogador (substitui getJogadorFlow) */
    fun getJogadorById(id: Int): Flow<Jogador?> {
        return jogadorDao.getJogadorById(id)
    }

    /** NOVO: Lógica para comprar upgrade */
    suspend fun buyUpgrade(jogadorId: Int, upgrade: UpgradeDisponivel): Boolean {
        val jogador = jogadorDao.getJogadorById(jogadorId).firstOrNull() ?: return false
        if (jogador.pontos >= upgrade.preco) {
            // 1. Debitar pontos
            val novoJogador = jogador.copy(pontos = jogador.pontos - upgrade.preco)
            jogadorDao.update(novoJogador)

            // 2. Salvar no OwnedUpgradeDao
            val ownedUpgrade = OwnedUpgrade(
                upgradeApiId = upgrade.id,
                nome = upgrade.nome,
                preco = upgrade.preco,
                efeito = upgrade.descricao,
                jogadorId = jogadorId
            )
            ownedUpgradeDao.insert(ownedUpgrade)
            return true
        }
        return false
    }

    /** NOVO: Usado para obter o ID do jogador logado (ou criar um se não existir) */
    suspend fun getCurrentJogadorId(): Int {
        val jogador = jogadorDao.getJogadorByNome("default_user")
        if (jogador == null) {
            // Cria um jogador padrão com pontos para poder comprar
            jogadorDao.insert(Jogador(nome = "default_user", pontos = 10000L))
            return jogadorDao.getJogadorByNome("default_user")?.id ?: 1
        }
        return jogador.id
    }

    /** Mantido e renomeado: Atualiza o jogador no banco de dados. */
    suspend fun updateJogador(jogador: Jogador) {
        jogadorDao.update(jogador)
    }
}