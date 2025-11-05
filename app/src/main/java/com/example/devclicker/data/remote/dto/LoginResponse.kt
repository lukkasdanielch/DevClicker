package com.example.devclicker.data.remote.dto

/**
 * Resposta da API após o login (dados recebidos)
 */
data class LoginResponse(
    val token: String,
    val userId: String
)