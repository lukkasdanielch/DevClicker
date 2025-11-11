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
     * CORREÇÃO: A query agora usa "FROM jogadores" (o tableName)
     * e não "FROM Jogador" (o nome da classe).
     */
    @Query("SELECT * FROM jogadores WHERE id = :id")
    fun getJogadorById(id: Int): Flow<Jogador?>

    /**
     * CORREÇÃO: Query também atualizada para "FROM jogadores".
     */
    @Query("SELECT * FROM jogadores WHERE nome = :nome LIMIT 1")
    suspend fun getJogadorByNome(nome: String): Jogador?

    /**
     * CORREÇÃO: Query também atualizada para "FROM jogadores".
     */
    @Query("SELECT * FROM jogadores")
    fun getAllJogadores(): Flow<List<Jogador>>
}