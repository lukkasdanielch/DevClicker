package com.example.devclicker.ui.game.upgrades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.remote.RetrofitInstance
import com.example.devclicker.data.remote.dto.UpgradeInfo // <--- IMPORTANTE
import com.example.devclicker.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 1. Definir um estado para a UI
sealed class UpgradesUiState {
    object Loading : UpgradesUiState()
    // ESTA LINHA DEVE ESTAR CORRETA:
    data class Success(val upgrades: List<UpgradeInfo>) : UpgradesUiState()
    data class Error(val message: String) : UpgradesUiState()
}

class UpgradesViewModel : ViewModel() {

    // 2. Instanciar o Repositório
    private val gameRepository = GameRepository(RetrofitInstance.api)

    // 3. Criar o StateFlow para a UI observar
    private val _upgradesState = MutableStateFlow<UpgradesUiState>(UpgradesUiState.Loading)
    val upgradesState: StateFlow<UpgradesUiState> = _upgradesState

    // 4. Criar a função para carregar os dados
    init {
        loadUpgrades()
    }

    fun loadUpgrades() {
        _upgradesState.value = UpgradesUiState.Loading
        viewModelScope.launch {
            try {
                // 5. Chamar a função do repositório
                val upgradesList = gameRepository.getUpgrades()
                // Se a linha abaixo der erro, é porque a definição da
                // classe 'Success' (ali em cima) está diferente.
                _upgradesState.value = UpgradesUiState.Success(upgradesList)
            } catch (e: Exception) {
                // 6. Tratar o erro (IMPORTANTE!)
                _upgradesState.value = UpgradesUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}