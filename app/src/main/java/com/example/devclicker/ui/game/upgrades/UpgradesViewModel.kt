package com.example.devclicker.ui.game.upgrades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devclicker.data.model.Jogador
import com.example.devclicker.data.model.UpgradeComprado
import com.example.devclicker.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.combine // O import está correto
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpgradesViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    // --- Fontes de Dados Internas ---
    private val _jogadorId = flow { emit(gameRepository.getCurrentJogadorId()) }
    private val _jogadorFlow: Flow<Jogador?> = _jogadorId.flatMapLatest { gameRepository.getJogadorById(it) }
    private val _upgradesCompradosFlow: Flow<List<UpgradeComprado>> = _jogadorId.flatMapLatest { gameRepository.getOwnedUpgrades(it) }
    private val _upgradesDisponiveisFlow: Flow<List<UpgradeDisponivel>> = flow {
        emit(gameRepository.getAvailableUpgrades())
    }
    private val _termoPesquisa = MutableStateFlow("")
    private val _mensagemErro = MutableStateFlow<String?>(null)
    private val _mensagemSucesso = MutableStateFlow<String?>(null)

    // --- ESTADO PÚBLICO (A CORREÇÃO) ---

    // 1. Combine os 3 fluxos de DADOS (Jogador, Loja, Inventário)
    private val dadosFlow = combine(
        _jogadorFlow,
        _upgradesDisponiveisFlow,
        _upgradesCompradosFlow
    ) { jogador, disponiveis, comprados ->
        // Crie um "pacote" temporário com esses 3 valores
        Triple(jogador, disponiveis, comprados)
    }

    // 2. Combine os 3 fluxos de ESTADO (Pesquisa, Erro, Sucesso)
    private val estadoUiFlow = combine(
        _termoPesquisa,
        _mensagemErro,
        _mensagemSucesso
    ) { pesquisa, erro, sucesso ->
        // Crie um "pacote" temporário com esses 3 valores
        Triple(pesquisa, erro, sucesso)
    }

    // 3. Combine os DOIS "pacotes" resultantes
    val uiState: StateFlow<UpgradesUiState> = combine(
        dadosFlow,
        estadoUiFlow
    ) { dados, estado ->

        // Desempacota os valores
        val (jogador, disponiveis, comprados) = dados
        val (pesquisa, erro, sucesso) = estado

        // Agora, finalmente, crie o UiState
        UpgradesUiState(
            jogadorPontos = jogador?.pontos ?: 0L,
            upgradesDisponiveis = disponiveis,
            upgradesComprados = comprados,
            termoPesquisa = pesquisa,
            isLoading = false,
            mensagemErro = erro,
            mensagemSucesso = sucesso
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UpgradesUiState(isLoading = true)
    )

    // --- Funções de Evento (Chamadas pela UI) ---
    // (O resto do seu código está 100% correto e não precisa mudar)

    fun onSearchTermChanged(termo: String) {
        _termoPesquisa.value = termo
    }

    fun onBuyUpgrade(upgradeUi: UpgradeDisponivel) {
        viewModelScope.launch {
            val jogadorId = _jogadorId.first()

            if (uiState.value.jogadorPontos < upgradeUi.preco) {
                _mensagemErro.value = "Pontos insuficientes!"
                return@launch
            }

            try {
                val sucesso = gameRepository.buyUpgrade(jogadorId, upgradeUi)

                if (sucesso) {
                    _mensagemSucesso.value = "${upgradeUi.nome} comprado!"
                } else {
                    _mensagemErro.value = "Não foi possível comprar."
                }
            } catch (e: Exception) {
                _mensagemErro.value = e.message ?: "Erro desconhecido"
            }
        }
    }

    fun onErrorMessageShown() {
        _mensagemErro.value = null
    }

    fun onSuccessMessageShown() {
        _mensagemSucesso.value = null
    }
}