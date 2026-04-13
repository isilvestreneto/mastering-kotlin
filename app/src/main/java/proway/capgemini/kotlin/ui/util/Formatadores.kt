package proway.capgemini.kotlin.ui.util

import java.text.NumberFormat
import java.util.Locale

private val formatoBRL = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

/** Valor completo em BRL — ex: R$ 1.234,56 */
fun formatarMoeda(valor: Float): String = formatoBRL.format(valor)

/** Abreviado para exibição em espaços reduzidos — ex: R$ 1,2k / R$ 1,2M */
fun formatarMoedaAbreviado(valor: Float): String = when {
    valor >= 1_000_000f -> "R$ %.1fM".format(valor / 1_000_000f)
    valor >= 1_000f -> "R$ %.1fk".format(valor / 1_000f)
    else -> formatoBRL.format(valor)
}
