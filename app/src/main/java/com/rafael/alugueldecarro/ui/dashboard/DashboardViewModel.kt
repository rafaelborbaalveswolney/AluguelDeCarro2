package com.rafael.alugueldecarro.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafael.alugueldecarro.domain.model.LocacaoDetalhada
import com.rafael.alugueldecarro.domain.repository.LocacaoRepository
import com.rafael.alugueldecarro.domain.repository.VeiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val locacaoRepository: LocacaoRepository,
    private val veiculoRepository: VeiculoRepository
) : ViewModel() {

    private val _locacoes =
        MutableStateFlow<List<LocacaoDetalhada>>(emptyList())

    val locacoes: StateFlow<List<LocacaoDetalhada>> =
        _locacoes.asStateFlow()

    private val _carregando =
        MutableStateFlow(true)

    val carregando: StateFlow<Boolean> =
        _carregando.asStateFlow()

    private val _erro =
        MutableStateFlow<String?>(null)

    val erro: StateFlow<String?> =
        _erro.asStateFlow()

    private val _mensagem =
        MutableStateFlow<String?>(null)

    val mensagem: StateFlow<String?> =
        _mensagem.asStateFlow()

    init {
        carregarLocacoesAtivas()
    }

    private fun carregarLocacoesAtivas() {

        viewModelScope.launch {

            try {

                locacaoRepository
                    .listarAtivas()
                    .collect { lista ->

                        _locacoes.value = lista
                        _carregando.value = false
                    }

            } catch (e: Exception) {

                _carregando.value = false
                _erro.value =
                    "Erro ao carregar as locações."
            }
        }
    }

    fun finalizarLocacao(
        item: LocacaoDetalhada
    ) {

        viewModelScope.launch {

            try {

                // Finaliza a locação
                locacaoRepository.atualizarStatus(
                    locacaoId = item.locacao.id,
                    status = "FINALIZADA"
                )

                // Libera o veículo novamente
                veiculoRepository.atualizar(
                    item.veiculo.copy(
                        status = "DISPONIVEL"
                    )
                )

                _mensagem.value =
                    "Locação finalizada com sucesso."

            } catch (e: Exception) {

                _erro.value =
                    "Erro ao finalizar a locação."
            }
        }
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}