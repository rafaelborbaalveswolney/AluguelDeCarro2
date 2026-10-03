package com.rafael.alugueldecarro.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [
        VeiculoEntity::class,
        ClienteEntity::class,
        LocacaoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun veiculoDao(): VeiculoDao

    abstract fun clienteDao(): ClienteDao

    abstract fun locacaoDao(): LocacaoDao
}