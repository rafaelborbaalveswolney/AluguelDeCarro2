package com.rafael.alugueldecarro.domain.repository

import com.rafael.alugueldecarro.domain.model.Locacao
import com.rafael.alugueldecarro.domain.model.Veiculo

interface SyncRepository {

    suspend fun sincronizarVeiculo(
        veiculo: Veiculo
    ): Result<String>

    suspend fun sincronizarLocacao(
        locacao: Locacao
    ): Result<String>
}