package com.rafael.alugueldecarro.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface LocacaoDao {

    @Insert
    suspend fun inserir(locacao: LocacaoEntity): Long

    @Transaction
    @Query(
        "SELECT * FROM locacoes " +
                "WHERE status = 'ATIVA' " +
                "ORDER BY dataEntregaPrevista"
    )
    fun listarAtivas(): Flow<List<LocacaoComDetalhes>>

    @Transaction
    @Query(
        "SELECT * FROM locacoes " +
                "ORDER BY dataSaida DESC"
    )
    fun listarTodas(): Flow<List<LocacaoComDetalhes>>

    @Query(
        "UPDATE locacoes " +
                "SET status = :status " +
                "WHERE id = :locacaoId"
    )
    suspend fun atualizarStatus(
        locacaoId: Int,
        status: String
    )
}