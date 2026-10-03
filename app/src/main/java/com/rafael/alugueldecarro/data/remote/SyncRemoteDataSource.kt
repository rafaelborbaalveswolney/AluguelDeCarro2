package com.rafael.alugueldecarro.data.remote

class SyncRemoteDataSource(
    private val apiService: ApiService
) {

    suspend fun sincronizarVeiculo(
        veiculo: VeiculoSyncRequest
    ): SyncResponse {

        return apiService.sincronizarVeiculo(
            veiculo
        )
    }

    suspend fun sincronizarLocacao(
        locacao: LocacaoSyncRequest
    ): SyncResponse {

        return apiService.sincronizarLocacao(
            locacao
        )
    }
}