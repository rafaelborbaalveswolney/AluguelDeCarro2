package com.rafael.alugueldecarro.ui.locacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rafael.alugueldecarro.domain.repository.LocacaoRepository

class HistoricoViewModelFactory(
    private val repository: LocacaoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(HistoricoViewModel::class.java)) {
            return HistoricoViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido")
    }
}