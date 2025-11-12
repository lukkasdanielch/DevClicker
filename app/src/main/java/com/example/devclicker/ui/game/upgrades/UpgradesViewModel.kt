package com.example.devclicker.ui.game.upgrades

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.database.AppDatabase
import com.example.devclicker.data.repository.GameRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UpgradesViewModel(application: Application) : AndroidViewModel(application) {


    private val db = AppDatabase.getDatabase(application)
    // CORREÇÃO: Usando o método db.upgradeDao() (se o seu DAO for nomeado UpgradeDao)
    private val gameRepository = GameRepository.create(db.jogadorDao(), db.upgradeDao())

    private val _uiState = MutableStateFlow(UpgradesUiState(isLoading = true))
    val uiState: StateFlow<UpgradesUiState> = _uiState.asStateFlow()

    private var jogadorId: Int = 0

    init {
        viewModelScope.launch {
            jogadorId = gameRepository.getCurrentJogadorId()
            loadUpgrades()
            observePlayerData()
        }
    }

    private fun observePlayerData() {

        // Note: Se sua entidade comprada foi renomeada para Upgrade, você precisará
        // garantir que gameRepository.getOwnedUpgrades retorna List<Upgrade>.
        gameRepository.getOwnedUpgrades(jogadorId)
            .onEach { ownedUpgrades ->
                _uiState.update { it.copy(upgradesComprados = ownedUpgrades) }
            }.launchIn(viewModelScope)


        gameRepository.getJogadorById(jogadorId)
            .filterNotNull()
            .onEach { jogador ->
                _uiState.update { it.copy(jogadorPontos = jogador.pontos) }
            }.launchIn(viewModelScope)
    }

    private suspend fun loadUpgrades() {
        _uiState.update { it.copy(isLoading = true, mensagemErro = null) }
        try {
            val upgrades = gameRepository.getAvailableUpgrades()
            _uiState.update { it.copy(upgradesDisponiveis = upgrades, isLoading = false) }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    mensagemErro = "Erro ao carregar upgrades: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun onSearchTermChange(term: String) {
        _uiState.update { it.copy(termoPesquisa = term) }
    }

    fun buyUpgrade(upgrade: UpgradeDisponivel) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, mensagemErro = null, mensagemSucesso = null) }

            val success = gameRepository.buyUpgrade(jogadorId, upgrade)

            _uiState.update { it.copy(isLoading = false) }

            if (success) {
                _uiState.update {
                    it.copy(mensagemSucesso = "Upgrade ${upgrade.nome} comprado!")
                }
            } else {
                _uiState.update {
                    it.copy(mensagemErro = "Pontos insuficientes para comprar ${upgrade.nome}.")
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(mensagemErro = null, mensagemSucesso = null) }
    }
}