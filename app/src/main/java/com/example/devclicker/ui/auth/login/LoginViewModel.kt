package com.example.devclicker.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 1. Receber o AuthRepository no construtor
class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Preencha todos os campos")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            authRepository.login(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginState.value = LoginState.Success
                    } else {
                        _loginState.value = LoginState.Error(task.exception?.message ?: "Erro ao fazer login")
                    }
                }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
