package com.example.devclicker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.devclicker.data.model.Jogador
import kotlinx.coroutines.flow.Flow

@Dao
interface JogadorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(jogador: Jogador)

    @Update
    suspend fun update(jogador: Jogador)

    @Delete
    suspend fun delete(jogador: Jogador)

    /**
     * (NOVA FUNÇÃO) Busca um jogador pelo ID (não-Flow)
     * para ser usada dentro de outras funções suspend.
     */
    @Query("SELECT * FROM jogadores WHERE id = :id")
    suspend fun getJogador(id: Int): Jogador?

    /**
     * (Esta é observada pela UI)
     */
    @Query("SELECT * FROM jogadores WHERE id = :id")
    fun getJogadorById(id: Int): Flow<Jogador?>

    @Query("SELECT * FROM jogadores WHERE nome = :nome LIMIT 1")
    suspend fun getJogadorByNome(nome: String): Jogador?

    @Query("SELECT * FROM jogadores")
    fun getAllJogadores(): Flow<List<Jogador>>
}