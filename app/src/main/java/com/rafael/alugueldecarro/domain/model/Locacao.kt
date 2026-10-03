package com.rafael.alugueldecarro.domain.model

data class Locacao(
    val id: Int = 0,
    val veiculoId: Int,
    val clienteId: Int,
    val dataSaida: Long,
    val dataEntregaPrevista: Long,
    val valorTotal: Double,
    val status: String = "ATIVA"
)