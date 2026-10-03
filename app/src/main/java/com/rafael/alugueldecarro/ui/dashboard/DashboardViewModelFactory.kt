package com.rafael.alugueldecarro.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rafael.alugueldecarro.domain.repository.LocacaoRepository
import com.rafael.alugueldecarro.domain.repository.VeiculoRepository

class DashboardViewModelFactory(
    private val locacaoRepository: LocacaoRepository,
    private val veiculoRepository: VeiculoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {

            return DashboardViewModel(
                locacaoRepository = locacaoRepository,
                veiculoRepository = veiculoRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido")
    }
}