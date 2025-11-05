package com.example.devclicker.data.repository

import com.example.devclicker.data.remote.ApiService
import com.example.devclicker.data.remote.dto.BuyUpgradeRequest
import com.example.devclicker.data.remote.dto.ResetProgressRequest

// O repositório recebe a ApiService (idealmente via Injeção de Dependência)
class GameRepository(private val apiService: ApiService) {

    // Funções de Progresso
    suspend fun getUserProgress(userId: String) = apiService.getUserProgress(userId)

    suspend fun resetProgress(userId: String) =
        apiService.resetProgress(ResetProgressRequest(userId))

    // Funções de Upgrades
    suspend fun getUpgrades() = apiService.getUpgrades()

    suspend fun buyUpgrade(userId: String, upgradeId: String) =
        apiService.buyUpgrade(BuyUpgradeRequest(userId, upgradeId))

}