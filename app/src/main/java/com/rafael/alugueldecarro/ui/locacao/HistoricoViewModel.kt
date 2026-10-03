package com.rafael.alugueldecarro.ui.locacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafael.alugueldecarro.domain.model.LocacaoDetalhada
import com.rafael.alugueldecarro.domain.repository.LocacaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoricoViewModel(
    private val repository: LocacaoRepository
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

    init {
        carregarHistorico()
    }

    private fun carregarHistorico() {

        viewModelScope.launch {

            try {

                repository
                    .listarTodas()
                    .collect { lista ->

                        _locacoes.value = lista
                        _carregando.value = false
                    }

            } catch (e: Exception) {

                _carregando.value = false

                _erro.value =
                    "Erro ao carregar o histórico."
            }
        }
    }
}