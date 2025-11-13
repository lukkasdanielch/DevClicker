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

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val jogadorRepository: JogadorRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    fun signUp(nome: String, email: String, password: String) {
        if (nome.isBlank() || email.isBlank() || password.isBlank()) {
            _signUpState.value = SignUpState.Error("Preencha todos os campos")
            return
        }
        _signUpState.value = SignUpState.Loading

        viewModelScope.launch {
            try {
                authRepository.signUp(email, password)

                val novoJogador = Jogador(nome = nome, pontos = 0)
                jogadorRepository.insert(novoJogador)

                _signUpState.value = SignUpState.Success

            } catch (e: Exception) {
                _signUpState.value = SignUpState.Error(e.message ?: "Erro ao cadastrar")
            }
        }
    }
}

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}