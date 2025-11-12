package com.example.devclicker.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.model.Jogador
import com.example.devclicker.data.repository.AuthRepository
import com.example.devclicker.data.repository.JogadorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // <-- Hilt, este ViewModel é seu
class SignUpViewModel @Inject constructor(
    // 1. Eu preciso destas duas peças:
    private val authRepository: AuthRepository,
    private val jogadorRepository: JogadorRepository // <-- O parâmetro que faltava
) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    // O ViewModel agora precisa do 'nome'
    fun signUp(nome: String, email: String, password: String) {
        if (nome.isBlank() || email.isBlank() || password.isBlank()) {
            _signUpState.value = SignUpState.Error("Preencha todos os campos")
            return
        }
        _signUpState.value = SignUpState.Loading

        viewModelScope.launch {
            try {
                // Etapa 1: Firebase (com a Peça 1)
                authRepository.signUp(email, password)

                // Etapa 2: Room (com a Peça 2)
                val novoJogador = Jogador(nome = nome, pontos = 0)
                jogadorRepository.insert(novoJogador)

                _signUpState.value = SignUpState.Success

            } catch (e: Exception) {
                // Se qualquer etapa falhar, o erro cai aqui
                _signUpState.value = SignUpState.Error(e.message ?: "Erro ao cadastrar")
            }
        }
    }
}

// Mova isso para seu próprio arquivo: SignUpState.kt
sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}