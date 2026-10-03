package com.rafael.alugueldecarro.ui.veiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rafael.alugueldecarro.domain.repository.VeiculoRepository

class VeiculoViewModelFactory(
    private val repository: VeiculoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(VeiculoViewModel::class.java)) {
            return VeiculoViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido")
    }
}