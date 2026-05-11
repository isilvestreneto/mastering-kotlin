package proway.capgemini.kotlin.atividade2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://viacep.com.br/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ViaCepService = retrofit.create(ViaCepService::class.java)

}