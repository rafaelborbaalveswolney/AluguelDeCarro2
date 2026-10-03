package com.rafael.alugueldecarro.data.local

import androidx.room3.Embedded
import androidx.room3.Relation

data class LocacaoComDetalhes(

    @Embedded
    val locacao: LocacaoEntity,

    @Relation(
        parentColumns = ["veiculoId"],
        entityColumns = ["id"]
    )
    val veiculo: VeiculoEntity,

    @Relation(
        parentColumns = ["clienteId"],
        entityColumns = ["id"]
    )
    val cliente: ClienteEntity
)