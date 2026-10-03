package com.rafael.alugueldecarro.ui.veiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafael.alugueldecarro.domain.model.Veiculo
import com.rafael.alugueldecarro.domain.repository.VeiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VeiculoViewModel(
    private val repository: VeiculoRepository
) : ViewModel() {

    private val _veiculos =
        MutableStateFlow<List<Veiculo>>(emptyList())

    val veiculos: StateFlow<List<Veiculo>> =
        _veiculos.asStateFlow()

    private val _mensagem =
        MutableStateFlow<String?>(null)

    val mensagem: StateFlow<String?> =
        _mensagem.asStateFlow()

    init {
        carregarVeiculos()
    }

    private fun carregarVeiculos() {

        viewModelScope.launch {

            repository
                .listarTodos()
                .collect { lista ->

                    _veiculos.value = lista
                }
        }
    }

    fun cadastrarVeiculo(
        marca: String,
        modelo: String,
        placa: String,
        ano: Int,
        valorDiaria: Double
    ) {

        viewModelScope.launch {

            val veiculo = Veiculo(
                marca = marca,
                modelo = modelo,
                placa = placa,
                ano = ano,
                valorDiaria = valorDiaria,
                status = "DISPONIVEL"
            )

            repository.inserir(veiculo)

            _mensagem.value =
                "Veículo cadastrado com sucesso."
        }
    }

    fun colocarEmManutencao(
        veiculo: Veiculo
    ) {

        if (veiculo.status == "ALUGADO") {

            _mensagem.value =
                "Veículo alugado não pode entrar em manutenção."

            return
        }

        viewModelScope.launch {

            repository.atualizar(
                veiculo.copy(
                    status = "MANUTENCAO"
                )
            )

            _mensagem.value =
                "Veículo colocado em manutenção."
        }
    }

    fun tornarDisponivel(
        veiculo: Veiculo
    ) {

        if (veiculo.status == "ALUGADO") {

            _mensagem.value =
                "Veículo alugado não pode ser alterado manualmente."

            return
        }

        viewModelScope.launch {

            repository.atualizar(
                veiculo.copy(
                    status = "DISPONIVEL"
                )
            )

            _mensagem.value =
                "Veículo disponível para locação."
        }
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}