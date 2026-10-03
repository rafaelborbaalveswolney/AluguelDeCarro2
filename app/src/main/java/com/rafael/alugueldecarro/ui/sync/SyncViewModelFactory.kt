package com.rafael.alugueldecarro.ui.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rafael.alugueldecarro.domain.repository.SyncRepository

class SyncViewModelFactory(
    private val repository: SyncRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(SyncViewModel::class.java)) {
            return SyncViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido")
    }
}