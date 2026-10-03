package com.rafael.alugueldecarro.data.repository

import com.rafael.alugueldecarro.data.local.VeiculoDao
import com.rafael.alugueldecarro.data.local.VeiculoEntity
import com.rafael.alugueldecarro.domain.model.Veiculo
import com.rafael.alugueldecarro.domain.repository.VeiculoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VeiculoRepositoryImpl(
    private val veiculoDao: VeiculoDao
) : VeiculoRepository {

    override fun listarTodos(): Flow<List<Veiculo>> {
        return veiculoDao.listarTodos().map { lista ->
            lista.map { entity ->
                entity.toDomain()
            }
        }
    }

    override fun listarDisponiveis(): Flow<List<Veiculo>> {
        return veiculoDao.listarDisponiveis().map { lista ->
            lista.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun inserir(veiculo: Veiculo) {
        veiculoDao.inserir(veiculo.toEntity())
    }

    override suspend fun atualizar(veiculo: Veiculo) {
        veiculoDao.atualizar(veiculo.toEntity())
    }

    override suspend fun buscarPorId(id: Int): Veiculo? {
        return veiculoDao.buscarPorId(id)?.toDomain()
    }

    private fun VeiculoEntity.toDomain(): Veiculo {
        return Veiculo(
            id = id,
            marca = marca,
            modelo = modelo,
            placa = placa,
            ano = ano,
            valorDiaria = valorDiaria,
            status = status
        )
    }

    private fun Veiculo.toEntity(): VeiculoEntity {
        return VeiculoEntity(
            id = id,
            marca = marca,
            modelo = modelo,
            placa = placa,
            ano = ano,
            valorDiaria = valorDiaria,
            status = status
        )
    }
}