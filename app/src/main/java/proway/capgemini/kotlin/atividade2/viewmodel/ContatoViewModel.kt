package proway.capgemini.kotlin.atividade2.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import proway.capgemini.kotlin.atividade2.model.Contato
import proway.capgemini.kotlin.atividade2.model.ViaCepResponse
import proway.capgemini.kotlin.atividade2.network.RetrofitInstance
import proway.capgemini.kotlin.atividade2.repository.ContatoRepository

class ContatoViewModel(private val repository: ContatoRepository) : ViewModel() {
    private val _contatos = MutableStateFlow<List<Contato>>(emptyList())
    private val _endereco = MutableStateFlow<ViaCepResponse?>(null)
    private val _contatoSelecionado = MutableStateFlow<Contato?>(null)

    private val _erroCep = MutableStateFlow<String?>(null)
    private val _isLoadingCep = MutableStateFlow<Boolean>(false)

    val contatos: StateFlow<List<Contato>> = _contatos
    val endereco: StateFlow<ViaCepResponse?> = _endereco
    val contatoSelecionado: StateFlow<Contato?> = _contatoSelecionado
    val erroCep: StateFlow<String?> = _erroCep
    val isLoadingCep: StateFlow<Boolean> = _isLoadingCep

    init {
        viewModelScope.launch {
            repository.contatos.collect { lista ->
                _contatos.value = lista  // ← só isso aqui dentro
            }
        }
    }

    fun criar(contato: Contato) {
        viewModelScope.launch {
            repository.add(contato)
        }
    }

    fun atualizar(contato: Contato) {
        viewModelScope.launch {
            repository.edit(contato)
        }
    }

    fun deletar(contato: Contato) {
        viewModelScope.launch {
            repository.remove(contato)
        }
    }

    fun buscarCep(cep: String) {
        viewModelScope.launch {
            _isLoadingCep.value = true
            try {
                val resultado = repository.buscarCep(cep)
                if (resultado.erro != null) {
                    _erroCep.value = "CEP não encontrado"
                    _endereco.value = null
                } else {
                    _erroCep.value = null
                    _endereco.value = resultado
                }
            } catch (e: Exception) {
                _erroCep.value = "Erro ao buscar CEP"
                Log.e("ContatoViewModel", "Erro ao buscar CEP: ${e.message}")
            } finally {
                _isLoadingCep.value = false
            }
        }
    }

    fun selecionarContato(contato: Contato?) {
        _contatoSelecionado.value = contato
    }

    fun limparEndereco() {
        _endereco.value = null
    }

}