package com.rafael.alugueldecarro.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "clientes")
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nome: String,
    val telefone: String,
    val contatoId: Long
)