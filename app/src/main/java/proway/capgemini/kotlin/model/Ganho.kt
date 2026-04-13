package proway.capgemini.kotlin.model

import java.time.LocalDate

data class Ganho(
    val descricao: String, val valor: Float, val data: LocalDate, val categoria: Categoria
) {

}