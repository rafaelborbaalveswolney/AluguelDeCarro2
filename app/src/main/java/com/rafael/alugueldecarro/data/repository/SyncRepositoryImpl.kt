package com.rafael.alugueldecarro.data.repository

import com.rafael.alugueldecarro.data.remote.LocacaoSyncRequest
import com.rafael.alugueldecarro.data.remote.SyncRemoteDataSource
import com.rafael.alugueldecarro.data.remote.VeiculoSyncRequest
import com.rafael.alugueldecarro.domain.model.Locacao
import com.rafael.alugueldecarro.domain.model.Veiculo
import com.rafael.alugueldecarro.domain.repository.SyncRepository

class SyncRepositoryImpl(
    private val remoteDataSource: SyncRemoteDataSource
) : SyncRepository {

    override suspend fun sincronizarVeiculo(
        veiculo: Veiculo
    ): Result<String> {

        return try {

            val request = VeiculoSyncRequest(
                id = veiculo.id,
                marca = veiculo.marca,
                modelo = veiculo.modelo,
                placa = veiculo.placa,
                ano = veiculo.ano,
                valorDiaria = veiculo.valorDiaria,
                status = veiculo.status
            )

            val response =
                remoteDataSource.sincronizarVeiculo(
                    request
                )

            if (response.sucesso) {
                Result.success(
                    response.mensagem
                )
            } else {
                Result.failure(
                    Exception(response.mensagem)
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun sincronizarLocacao(
        locacao: Locacao
    ): Result<String> {

        return try {

            val request = LocacaoSyncRequest(
                id = locacao.id,
                veiculoId = locacao.veiculoId,
                clienteId = locacao.clienteId,
                dataSaida = locacao.dataSaida,
                dataEntregaPrevista = locacao.dataEntregaPrevista,
                valorTotal = locacao.valorTotal,
                status = locacao.status
            )

            val response =
                remoteDataSource.sincronizarLocacao(
                    request
                )

            if (response.sucesso) {
                Result.success(
                    response.mensagem
                )
            } else {
                Result.failure(
                    Exception(response.mensagem)
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}