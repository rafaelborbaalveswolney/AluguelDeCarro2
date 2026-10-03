package com.rafael.alugueldecarro.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(cliente: ClienteEntity): Long

    @Query("SELECT * FROM clientes ORDER BY nome")
    fun listarTodos(): Flow<List<ClienteEntity>>

    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): ClienteEntity?

    @Query("SELECT * FROM clientes WHERE contatoId = :contatoId LIMIT 1")
    suspend fun buscarPorContatoId(contatoId: Long): ClienteEntity?
}