package com.rafael.alugueldecarro.ui.contato

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContatoViewModelFactory(
    private val repository: ContatoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ContatoViewModel::class.java)) {
            return ContatoViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido")
    }
}