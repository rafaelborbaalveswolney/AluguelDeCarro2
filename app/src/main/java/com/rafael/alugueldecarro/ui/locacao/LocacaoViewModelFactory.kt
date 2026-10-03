package com.rafael.alugueldecarro.ui.locacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rafael.alugueldecarro.domain.repository.ClienteRepository
import com.rafael.alugueldecarro.domain.repository.LocacaoRepository
import com.rafael.alugueldecarro.domain.repository.VeiculoRepository

class LocacaoViewModelFactory(
    private val veiculoRepository: VeiculoRepository,
    private val clienteRepository: ClienteRepository,
    private val locacaoRepository: LocacaoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(LocacaoViewModel::class.java)) {

            return LocacaoViewModel(
                veiculoRepository = veiculoRepository,
                clienteRepository = clienteRepository,
                locacaoRepository = locacaoRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido")
    }
}