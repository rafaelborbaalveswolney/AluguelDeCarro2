package com.rafael.alugueldecarro.ui.contato

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContatoViewModel(
    private val repository: ContatoRepository
) : ViewModel() {

    private val _contatos = MutableStateFlow<List<Contato>>(emptyList())
    val contatos: StateFlow<List<Contato>> = _contatos.asStateFlow()

    private val _busca = MutableStateFlow("")
    val busca: StateFlow<String> = _busca.asStateFlow()

    private val _carregando = MutableStateFlow(false)
    val carregando: StateFlow<Boolean> = _carregando.asStateFlow()

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> = _erro.asStateFlow()

    private var todosContatos: List<Contato> = emptyList()

    fun carregarContatos() {

        viewModelScope.launch {

            _carregando.value = true
            _erro.value = null

            try {

                todosContatos = withContext(Dispatchers.IO) {
                    repository.buscarContatos()
                }

                filtrarContatos()

            } catch (e: Exception) {

                _erro.value = "Erro ao carregar contatos."

            } finally {

                _carregando.value = false
            }
        }
    }

    fun atualizarBusca(texto: String) {

        _busca.value = texto

        filtrarContatos()
    }

    private fun filtrarContatos() {

        val texto = _busca.value.trim()

        _contatos.value =
            if (texto.isBlank()) {
                todosContatos
            } else {
                todosContatos.filter { contato ->
                    contato.nome.contains(
                        texto,
                        ignoreCase = true
                    )
                }
            }
    }
}