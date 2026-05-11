package proway.capgemini.kotlin.atividade2.repository

import kotlinx.coroutines.flow.Flow
import proway.capgemini.kotlin.atividade2.database.ContatoDao
import proway.capgemini.kotlin.atividade2.model.Contato
import proway.capgemini.kotlin.atividade2.model.ViaCepResponse
import proway.capgemini.kotlin.atividade2.network.RetrofitInstance

class ContatoRepository(private val dao: ContatoDao) {
    val contatos: Flow<List<Contato>> = dao.listarContatos()

    suspend fun add(contato: Contato) = dao.criar(contato)
    suspend fun edit(contato: Contato) = dao.atualizar(contato)
    suspend fun remove(contato: Contato) = dao.deletar(contato)

    suspend fun buscarCep(cep: String): ViaCepResponse {
        return RetrofitInstance.service.buscarCep(cep)
    }
}