package com.example.devclicker.data.remote.dto

/**
 * Requisição de cadastro (dados enviados ao criar conta)
 */
data class SignUpRequest(
    val email: String,
    val password: String,
    val username: String
)
