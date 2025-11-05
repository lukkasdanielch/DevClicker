package com.example.devclicker.data.remote

import com.example.devclicker.data.remote.dto.*
import retrofit2.http.*

/**
 * Interface da API do DevClicker.
 * Define todos os endpoints que serão acessados pelo Retrofit.
 */
interface ApiService {

    // 🔹 LOGIN
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse // Removido Response<>

    // 🔹 CADASTRO
    @POST("auth/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): LoginResponse // Removido Response<>

    // 🔹 PROGRESSO DO USUÁRIO
    @GET("user/progress/{userId}")
    suspend fun getUserProgress(
        @Path("userId") userId: String
    ): UserProgress // Removido Response<>

    // 🔹 LISTA DE UPGRADES DISPONÍVEIS
    // FUNÇÃO CORRIGIDA: Renomeada para "getUpgrades" e removido o Response<>
    @GET("game/upgrades")
    suspend fun getUpgrades(): List<UpgradeInfo>

    // 🔹 COMPRA DE UPGRADE
    @POST("game/buy-upgrade")
    suspend fun buyUpgrade(
        @Body request: BuyUpgradeRequest
    ): BuyUpgradeResponse // Removido Response<>

    // 🔹 RESETAR PROGRESSO DO USUÁRIO (opcional)
    @POST("user/reset")
    suspend fun resetProgress(
        @Body request: ResetProgressRequest
    ): GenericResponse // Removido Response<>
}