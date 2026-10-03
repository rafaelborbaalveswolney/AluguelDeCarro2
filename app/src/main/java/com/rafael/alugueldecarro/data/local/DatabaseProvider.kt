package com.rafael.alugueldecarro.data.local

import android.content.Context
import androidx.room3.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {

        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "aluguel_de_carro.db"
            ).build()

            INSTANCE = instance

            instance
        }
    }
}