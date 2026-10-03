package com.rafael.alugueldecarro.ui.locacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafael.alugueldecarro.domain.model.Cliente
import com.rafael.alugueldecarro.domain.model.Locacao
import com.rafael.alugueldecarro.domain.model.Veiculo
import com.rafael.alugueldecarro.domain.repository.ClienteRepository
import com.rafael.alugueldecarro.domain.repository.LocacaoRepository
import com.rafael.alugueldecarro.domain.repository.VeiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class LocacaoViewModel(
    private val veiculoRepository: VeiculoRepository,
    private val clienteRepository: ClienteRepository,
    private val locacaoRepository: LocacaoRepository
) : ViewModel() {

    private val _veiculosDisponiveis =
        MutableStateFlow<List<Veiculo>>(emptyList())

    val veiculosDisponiveis: StateFlow<List<Veiculo>> =
        _veiculosDisponiveis.asStateFlow()

    private val _clienteSelecionado =
        MutableStateFlow<Cliente?>(null)

    val clienteSelecionado: StateFlow<Cliente?> =
        _clienteSelecionado.asStateFlow()

    private val _mensagem =
        MutableStateFlow<String?>(null)

    val mensagem: StateFlow<String?> =
        _mensagem.asStateFlow()

    init {
        carregarVeiculosDisponiveis()
    }

    private fun carregarVeiculosDisponiveis() {

        viewModelScope.launch {

            veiculoRepository
                .listarDisponiveis()
                .collect { lista ->

                    _veiculosDisponiveis.value = lista
                }
        }
    }

    fun selecionarCliente(
        nome: String,
        telefone: String,
        contatoId: Long
    ) {

        viewModelScope.launch {

            val clienteExistente =
                clienteRepository.buscarPorContatoId(
                    contatoId
                )

            if (clienteExistente != null) {

                _clienteSelecionado.value =
                    clienteExistente

            } else {

                val novoCliente = Cliente(
                    nome = nome,
                    telefone = telefone,
                    contatoId = contatoId
                )

                val novoId =
                    clienteRepository.inserir(
                        novoCliente
                    )

                _clienteSelecionado.value =
                    novoCliente.copy(
                        id = novoId.toInt()
                    )
            }
        }
    }

    fun calcularValorTotal(
        veiculo: Veiculo?,
        dataSaida: Long?,
        dataEntrega: Long?
    ): Double {

        if (
            veiculo == null ||
            dataSaida == null ||
            dataEntrega == null
        ) {
            return 0.0
        }

        val saida = Instant
            .ofEpochMilli(dataSaida)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val entrega = Instant
            .ofEpochMilli(dataEntrega)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val dias =
            ChronoUnit.DAYS.between(
                saida,
                entrega
            )

        if (dias <= 0) {
            return 0.0
        }

        return dias * veiculo.valorDiaria
    }

    fun confirmarLocacao(
        veiculo: Veiculo?,
        dataSaida: Long?,
        dataEntrega: Long?
    ) {

        val cliente = _clienteSelecionado.value

        if (veiculo == null) {
            _mensagem.value =
                "Selecione um veículo."
            return
        }

        if (cliente == null) {
            _mensagem.value =
                "Selecione um cliente."
            return
        }

        if (
            dataSaida == null ||
            dataEntrega == null
        ) {
            _mensagem.value =
                "Selecione as datas."
            return
        }

        val valorTotal =
            calcularValorTotal(
                veiculo,
                dataSaida,
                dataEntrega
            )

        if (valorTotal <= 0) {
            _mensagem.value =
                "A data de entrega deve ser posterior à data de saída."
            return
        }

        viewModelScope.launch {

            val locacao = Locacao(
                veiculoId = veiculo.id,
                clienteId = cliente.id,
                dataSaida = dataSaida,
                dataEntregaPrevista = dataEntrega,
                valorTotal = valorTotal,
                status = "ATIVA"
            )

            locacaoRepository.inserir(locacao)

            veiculoRepository.atualizar(
                veiculo.copy(
                    status = "ALUGADO"
                )
            )

            _mensagem.value =
                "Locação realizada com sucesso."
        }
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}