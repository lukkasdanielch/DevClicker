package com.example.devclicker.data.repository

import com.example.devclicker.data.dao.JogadorDao
import com.example.devclicker.data.model.Jogador
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repositório do Jogador (Room).
 * O Hilt vai entregar o 'jogadorDao: JogadorDao' automaticamente.
 */
class JogadorRepository @Inject constructor(
    private val jogadorDao: JogadorDao
) {

    suspend fun insert(jogador: Jogador) {
        jogadorDao.insert(jogador)
    }

    suspend fun update(jogador: Jogador) {
        jogadorDao.update(jogador)
    }

    suspend fun getJogadorByNome(nome: String): Jogador? {
        return jogadorDao.getJogadorByNome(nome)
    }

    fun getJogadorById(id: Int): Flow<Jogador?> {
        return jogadorDao.getJogadorById(id)
    }
}