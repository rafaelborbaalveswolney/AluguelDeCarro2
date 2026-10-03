package com.rafael.alugueldecarro.ui.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafael.alugueldecarro.domain.model.Locacao
import com.rafael.alugueldecarro.domain.model.Veiculo
import com.rafael.alugueldecarro.domain.repository.SyncRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SyncViewModel(
    private val repository: SyncRepository
) : ViewModel() {

    private val _mensagem =
        MutableStateFlow<String?>(null)

    val mensagem: StateFlow<String?> =
        _mensagem.asStateFlow()

    private val _carregando =
        MutableStateFlow(false)

    val carregando: StateFlow<Boolean> =
        _carregando.asStateFlow()

    fun sincronizarVeiculo(
        veiculo: Veiculo
    ) {

        viewModelScope.launch {

            _carregando.value = true

            val resultado =
                repository.sincronizarVeiculo(
                    veiculo
                )

            resultado
                .onSuccess {
                    _mensagem.value = it
                }
                .onFailure {
                    _mensagem.value =
                        "Erro ao sincronizar veículo."
                }

            _carregando.value = false
        }
    }

    fun sincronizarLocacao(
        locacao: Locacao
    ) {

        viewModelScope.launch {

            _carregando.value = true

            val resultado =
                repository.sincronizarLocacao(
                    locacao
                )

            resultado
                .onSuccess {
                    _mensagem.value = it
                }
                .onFailure {
                    _mensagem.value =
                        "Erro ao sincronizar locação."
                }

            _carregando.value = false
        }
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}