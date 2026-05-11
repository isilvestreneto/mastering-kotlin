package proway.capgemini.kotlin.atividade2.network

import proway.capgemini.kotlin.atividade2.model.ViaCepResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("ws/{cep}/json")
    suspend fun buscarCep(@Path("cep") cep: String): ViaCepResponse
}