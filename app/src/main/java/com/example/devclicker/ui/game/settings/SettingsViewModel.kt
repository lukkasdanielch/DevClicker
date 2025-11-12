package com.example.devclicker.ui.game.settings

import androidx.lifecycle.ViewModel
import com.example.devclicker.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel // 1. Habilitado para Hilt
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository // 2. Recebe o repositório
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Chamado quando o usuário clica no botão "Logout".
     */
    fun onLogoutClicked() {
        authRepository.logout()
    }
}