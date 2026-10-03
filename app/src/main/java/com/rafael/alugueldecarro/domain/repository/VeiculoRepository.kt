package com.rafael.alugueldecarro.domain.repository

import com.rafael.alugueldecarro.domain.model.Veiculo
import kotlinx.coroutines.flow.Flow

interface VeiculoRepository {

    fun listarTodos(): Flow<List<Veiculo>>

    fun listarDisponiveis(): Flow<List<Veiculo>>

    suspend fun inserir(veiculo: Veiculo)

    suspend fun atualizar(veiculo: Veiculo)

    suspend fun buscarPorId(id: Int): Veiculo?
}