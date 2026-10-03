package com.rafael.alugueldecarro.domain.repository

import com.rafael.alugueldecarro.domain.model.Locacao
import com.rafael.alugueldecarro.domain.model.LocacaoDetalhada
import kotlinx.coroutines.flow.Flow

interface LocacaoRepository {

    fun listarAtivas(): Flow<List<LocacaoDetalhada>>

    fun listarTodas(): Flow<List<LocacaoDetalhada>>

    suspend fun inserir(locacao: Locacao): Long

    suspend fun atualizarStatus(
        locacaoId: Int,
        status: String
    )
}