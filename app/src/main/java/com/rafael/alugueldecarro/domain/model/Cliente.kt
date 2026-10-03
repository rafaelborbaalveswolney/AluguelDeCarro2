package com.rafael.alugueldecarro.domain.model

data class Cliente(
    val id: Int = 0,
    val nome: String,
    val telefone: String,
    val contatoId: Long
)