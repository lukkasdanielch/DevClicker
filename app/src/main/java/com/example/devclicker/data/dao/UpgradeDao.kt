package com.example.devclicker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.example.devclicker.data.model.UpgradeComprado
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List

@Dao
interface UpgradeDao {

    // 2. TODAS AS FUNÇÕES AGORA USAM "UpgradeComprado"
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(upgrade: UpgradeComprado)

    @Update
    suspend fun update(upgrade: UpgradeComprado)

    @Delete
    suspend fun delete(upgrade: UpgradeComprado)

    /**
     * (NOVA FUNÇÃO) Busca um upgrade específico de um jogador,
     * para ser usada dentro de outras funções suspend.
     */
    @Query("SELECT * FROM upgrades_comprados WHERE jogadorId = :jogadorId AND upgradeId = :upgradeId LIMIT 1")
    suspend fun getUpgrade(jogadorId: Int, upgradeId: String): UpgradeComprado?

    /**
     * Função crucial para obter todos os upgrades comprados por um jogador específico.
     */
    @Query("SELECT * FROM upgrades_comprados WHERE jogadorId = :jogadorId")
    fun getUpgradesDoJogador(jogadorId: Int): Flow<List<UpgradeComprado>>

    /**
     * Busca um upgrade comprado específico pelo seu ID.
     */
    @Query("SELECT * FROM upgrades_comprados WHERE id = :id")
    fun getUpgradeById(id: Int): Flow<UpgradeComprado?>

    /**
     * Retorna todos os upgrades comprados no banco de dados.
     */
    @Query("SELECT * FROM upgrades_comprados")
    fun getAllUpgradesComprados(): Flow<List<UpgradeComprado>>
}