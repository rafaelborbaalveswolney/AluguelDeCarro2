package com.rafael.alugueldecarro.domain.repository

import com.rafael.alugueldecarro.domain.model.Cliente
import kotlinx.coroutines.flow.Flow

interface ClienteRepository {

    fun listarTodos(): Flow<List<Cliente>>

    suspend fun inserir(cliente: Cliente): Long

    suspend fun buscarPorId(id: Int): Cliente?

    suspend fun buscarPorContatoId(contatoId: Long): Cliente?
}