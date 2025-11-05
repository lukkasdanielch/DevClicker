package com.example.devclicker.data.remote

import com.example.devclicker.data.remote.dto.*
import retrofit2.Response
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
    ): Response<LoginResponse>

    // 🔹 CADASTRO
    @POST("auth/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<LoginResponse>

    // 🔹 PROGRESSO DO USUÁRIO
    @GET("user/progress/{userId}")
    suspend fun getUserProgress(
        @Path("userId") userId: String
    ): Response<UserProgress>

    // 🔹 LISTA DE UPGRADES DISPONÍVEIS
    @GET("game/upgrades")
    suspend fun getAvailableUpgrades(): Response<List<UpgradeInfo>>

    // 🔹 COMPRA DE UPGRADE
    @POST("game/buy-upgrade")
    suspend fun buyUpgrade(
        @Body request: BuyUpgradeRequest
    ): Response<BuyUpgradeResponse>

    // 🔹 RESETAR PROGRESSO DO USUÁRIO (opcional)
    @POST("user/reset")
    suspend fun resetProgress(
        @Body request: ResetProgressRequest
    ): Response<GenericResponse>
}
