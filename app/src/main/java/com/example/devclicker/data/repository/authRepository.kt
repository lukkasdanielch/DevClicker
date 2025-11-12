package com.example.devclicker.data.repository

import com.google.firebase.auth.FirebaseAuth

// Tarefa 3: Implementar o AuthRepository
class AuthRepository(private val auth: FirebaseAuth) {

    // Função para login (retorna a Task para o ViewModel)
    fun login(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password)

    // Função para cadastro (retorna a Task para o ViewModel)
    fun signUp(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password)

    // Função para logout (não precisa de retorno)
    fun logout() = auth.signOut()
}