package com.rafael.alugueldecarro.data.remote

data class VeiculoSyncRequest(
    val id: Int,
    val marca: String,
    val modelo: String,
    val placa: String,
    val ano: Int,
    val valorDiaria: Double,
    val status: String
)