package com.example.devclicker.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // 1. Anotação do Hilt
class LoginViewModel @Inject constructor(
    // 2. Recebe o AuthRepository (o Hilt entrega)
    private val authRepository: AuthRepository
) : ViewModel() {

    // 3. Usa a sua sealed class para emitir o estado
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    /**
     * Função de login chamada pela View
     */
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Preencha todos os campos")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                // --- ETAPA ÚNICA: Autenticação (Falar com o Firebase) ---
                // Tenta fazer o login.
                // Se falhar (ex: senha errada), o 'await()' lança uma exceção
                // e o código pula direto para o 'catch'.
                val authResult = authRepository.login(email, password)

                if (authResult.user != null) {
                    _loginState.value = LoginState.Success
                }

            } catch (e: Exception) {
                // Se o login falhar, o erro será capturado aqui.
                _loginState.value = LoginState.Error(e.message ?: "Erro ao fazer login")
            }
        }
    }
}

/**
 * Representa os EVENTOS de login, não o estado da tela inteira.
 * (É uma boa prática mover esta sealed class para seu próprio arquivo,
 * como 'LoginState.kt')
 */
sealed class LoginState {
    object Idle : LoginState()      // Estado inicial
    object Loading : LoginState()   // Carregando
    object Success : LoginState()   // Sucesso no login
    data class Error(val message: String) : LoginState() // Erro
}