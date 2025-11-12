package com.example.devclicker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.devclicker.data.model.Upgrade // Importa a Entidade
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List

@Dao
interface UpgradeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(upgrade: Upgrade)

    @Update
    suspend fun update(upgrade: Upgrade)

    @Delete
    suspend fun delete(upgrade: Upgrade)

    /**
     * Função crucial para obter todos os upgrades comprados por um jogador específico.
     */
    @Query("SELECT * FROM upgrades_comprados WHERE jogadorId = :jogadorId")
    fun getUpgradesDoJogador(jogadorId: Int): Flow<List<Upgrade>>

    /**
     * Busca um upgrade comprado específico pelo seu ID.
     */
    @Query("SELECT * FROM upgrades_comprados WHERE id = :id")
    fun getUpgradeById(id: Int): Flow<Upgrade?>

    /**
     * Retorna todos os upgrades comprados no banco de dados.
     */
    @Query("SELECT * FROM upgrades_comprados")
    fun getAllUpgradesComprados(): Flow<List<Upgrade>>
}