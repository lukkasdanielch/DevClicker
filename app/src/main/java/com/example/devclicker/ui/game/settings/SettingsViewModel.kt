package com.example.devclicker.ui.game.settings

import androidx.lifecycle.ViewModel
import com.example.devclicker.data.repository.AuthRepository

class SettingsViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun logout() {
        authRepository.logout()
    }
}