package proway.capgemini.kotlin.atividade2.model

data class ViaCepResponse(
    val cep: String,
    val logradouro: String,
    val complemento: String,
    val unidade: String,
    val bairro: String,
    val localidade: String,
    val uf: String,
    val estado: String,
    val regiao: String,
    val ibge: String,
    val gia: String,
    val ddd: String,
    val siafi: String,
    val erro: String? = null
)
