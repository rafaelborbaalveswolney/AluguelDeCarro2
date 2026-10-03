package com.rafael.alugueldecarro.data.remote

data class LocacaoSyncRequest(
    val id: Int,
    val veiculoId: Int,
    val clienteId: Int,
    val dataSaida: Long,
    val dataEntregaPrevista: Long,
    val valorTotal: Double,
    val status: String
)