package com.rafael.alugueldecarro.domain.model

data class LocacaoDetalhada(
    val locacao: Locacao,
    val veiculo: Veiculo,
    val cliente: Cliente
)