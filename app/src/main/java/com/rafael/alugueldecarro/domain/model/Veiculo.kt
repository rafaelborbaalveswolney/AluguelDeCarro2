package com.rafael.alugueldecarro.domain.model

data class Veiculo(
    val id: Int = 0,
    val marca: String,
    val modelo: String,
    val placa: String,
    val ano: Int,
    val valorDiaria: Double,
    val status: String = "DISPONIVEL"
)
