package com.example.devclicker.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.repository.AuthRepository // Importar
// import com.google.firebase.auth.FirebaseAuth // DELETAR ESTE IMPORT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun signUp(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _signUpState.value = SignUpState.Error("Preencha todos os campos")
            return
        }

        _signUpState.value = SignUpState.Loading

        viewModelScope.launch {
            // 3. Usar o authRepository
            authRepository.signUp(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _signUpState.value = SignUpState.Success
                    } else {
                        _signUpState.value = SignUpState.Error(task.exception?.message ?: "Erro ao cadastrar")
                    }
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
