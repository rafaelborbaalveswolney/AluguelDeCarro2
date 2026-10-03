package com.rafael.alugueldecarro.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("veiculos/sincronizar")
    suspend fun sincronizarVeiculo(
        @Body veiculo: VeiculoSyncRequest
    ): SyncResponse

    @POST("locacoes/sincronizar")
    suspend fun sincronizarLocacao(
        @Body locacao: LocacaoSyncRequest
    ): SyncResponse
}