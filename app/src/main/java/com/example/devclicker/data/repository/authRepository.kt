package com.example.devclicker.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await // Importante para o 'suspend'
import javax.inject.Inject

/**
 * Repositório de Autenticação.
 * Única responsabilidade: Falar com o Firebase Authentication.
 */
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    /**
     * Tenta fazer o login.
     * Se falhar (ex: senha errada), 'await()' vai lançar uma exceção.
     */
    suspend fun login(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    /**
     * Tenta criar um novo usuário.
     * Se falhar (ex: email já existe), 'await()' vai lançar uma exceção.
     */
    suspend fun signUp(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    /**
     * Faz o logout.
     */
    fun logout() = auth.signOut()
}