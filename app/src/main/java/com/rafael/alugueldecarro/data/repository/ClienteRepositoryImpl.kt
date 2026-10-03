package com.rafael.alugueldecarro.data.repository

import com.rafael.alugueldecarro.data.local.ClienteDao
import com.rafael.alugueldecarro.data.local.ClienteEntity
import com.rafael.alugueldecarro.domain.model.Cliente
import com.rafael.alugueldecarro.domain.repository.ClienteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClienteRepositoryImpl(
    private val clienteDao: ClienteDao
) : ClienteRepository {

    override fun listarTodos(): Flow<List<Cliente>> {
        return clienteDao.listarTodos().map { lista ->
            lista.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun inserir(cliente: Cliente): Long {
        return clienteDao.inserir(cliente.toEntity())
    }

    override suspend fun buscarPorId(id: Int): Cliente? {
        return clienteDao.buscarPorId(id)?.toDomain()
    }

    override suspend fun buscarPorContatoId(contatoId: Long): Cliente? {
        return clienteDao.buscarPorContatoId(contatoId)?.toDomain()
    }

    private fun ClienteEntity.toDomain(): Cliente {
        return Cliente(
            id = id,
            nome = nome,
            telefone = telefone,
            contatoId = contatoId
        )
    }

    private fun Cliente.toEntity(): ClienteEntity {
        return ClienteEntity(
            id = id,
            nome = nome,
            telefone = telefone,
            contatoId = contatoId
        )
    }
}