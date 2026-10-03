package com.rafael.alugueldecarro.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VeiculoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(veiculo: VeiculoEntity)

    @Update
    suspend fun atualizar(veiculo: VeiculoEntity)

    @Query("SELECT * FROM veiculos ORDER BY marca, modelo")
    fun listarTodos(): Flow<List<VeiculoEntity>>

    @Query("SELECT * FROM veiculos WHERE status = 'DISPONIVEL' ORDER BY marca, modelo")
    fun listarDisponiveis(): Flow<List<VeiculoEntity>>

    @Query("SELECT * FROM veiculos WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): VeiculoEntity?
}