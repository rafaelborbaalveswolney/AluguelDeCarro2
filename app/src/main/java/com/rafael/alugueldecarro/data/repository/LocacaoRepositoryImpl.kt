package com.rafael.alugueldecarro.data.repository

import com.rafael.alugueldecarro.data.local.LocacaoComDetalhes
import com.rafael.alugueldecarro.data.local.LocacaoDao
import com.rafael.alugueldecarro.data.local.LocacaoEntity
import com.rafael.alugueldecarro.domain.model.Cliente
import com.rafael.alugueldecarro.domain.model.Locacao
import com.rafael.alugueldecarro.domain.model.LocacaoDetalhada
import com.rafael.alugueldecarro.domain.model.Veiculo
import com.rafael.alugueldecarro.domain.repository.LocacaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocacaoRepositoryImpl(
    private val locacaoDao: LocacaoDao
) : LocacaoRepository {

    override fun listarAtivas(): Flow<List<LocacaoDetalhada>> {
        return locacaoDao.listarAtivas().map { lista ->
            lista.map { item ->
                item.toDomain()
            }
        }
    }

    override fun listarTodas(): Flow<List<LocacaoDetalhada>> {
        return locacaoDao.listarTodas().map { lista ->
            lista.map { item ->
                item.toDomain()
            }
        }
    }

    override suspend fun inserir(locacao: Locacao): Long {
        return locacaoDao.inserir(locacao.toEntity())
    }

    override suspend fun atualizarStatus(
        locacaoId: Int,
        status: String
    ) {
        locacaoDao.atualizarStatus(
            locacaoId = locacaoId,
            status = status
        )
    }

    private fun LocacaoComDetalhes.toDomain(): LocacaoDetalhada {
        return LocacaoDetalhada(
            locacao = Locacao(
                id = locacao.id,
                veiculoId = locacao.veiculoId,
                clienteId = locacao.clienteId,
                dataSaida = locacao.dataSaida,
                dataEntregaPrevista = locacao.dataEntregaPrevista,
                valorTotal = locacao.valorTotal,
                status = locacao.status
            ),
            veiculo = Veiculo(
                id = veiculo.id,
                marca = veiculo.marca,
                modelo = veiculo.modelo,
                placa = veiculo.placa,
                ano = veiculo.ano,
                valorDiaria = veiculo.valorDiaria,
                status = veiculo.status
            ),
            cliente = Cliente(
                id = cliente.id,
                nome = cliente.nome,
                telefone = cliente.telefone,
                contatoId = cliente.contatoId
            )
        )
    }

    private fun Locacao.toEntity(): LocacaoEntity {
        return LocacaoEntity(
            id = id,
            veiculoId = veiculoId,
            clienteId = clienteId,
            dataSaida = dataSaida,
            dataEntregaPrevista = dataEntregaPrevista,
            valorTotal = valorTotal,
            status = status
        )
    }
}