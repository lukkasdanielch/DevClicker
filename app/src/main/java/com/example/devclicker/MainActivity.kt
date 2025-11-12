package com.example.devclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.devclicker.data.database.AppDatabase
import com.example.devclicker.data.repository.AuthRepository
import com.example.devclicker.data.repository.GameRepository
import com.example.devclicker.navigation.Appnavigation
import com.example.devclicker.ui.auth.login.LoginViewModel
import com.example.devclicker.ui.auth.signup.SignUpViewModel
import com.example.devclicker.ui.game.settings.SettingsViewModel
import com.example.devclicker.ui.theme.DevClickerTheme
import com.google.firebase.auth.FirebaseAuth
import com.example.devclicker.ui.game.clicker.ClickerViewModel

class MainActivity : ComponentActivity() {

    class AppViewModelFactory(
        private val authRepository: AuthRepository,
        private val gameRepository: GameRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(authRepository) as T
                }
                modelClass.isAssignableFrom(ClickerViewModel::class.java) -> {
                    ClickerViewModel(gameRepository) as T
                }
                modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                    SignUpViewModel(authRepository) as T
                }
                modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                    SettingsViewModel(authRepository) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val auth = FirebaseAuth.getInstance()
        val authRepository = AuthRepository(auth)
        val gameRepository = GameRepository(db)
        val factory = AppViewModelFactory(authRepository, gameRepository)

        setContent {
            DevClickerTheme {
                Appnavigation(factory = factory)
            }
        }
    }
}