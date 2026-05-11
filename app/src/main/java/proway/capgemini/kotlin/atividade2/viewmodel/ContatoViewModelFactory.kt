package proway.capgemini.kotlin.atividade2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import proway.capgemini.kotlin.atividade2.repository.ContatoRepository

class ContatoViewModelFactory(
    private val repository: ContatoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContatoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContatoViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")

    }
}