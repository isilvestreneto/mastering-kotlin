package proway.capgemini.kotlin.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TelaInicio(saldo: Float, ganhos: Float, gastos: Float) {
    val saldoFormatado = formatarMoeda(saldo)

    val corFundo = if (saldo > 0) Color(0xFF4A6FA5)
    else if (saldo < 0) Color(0xFFE8C84A)
    else MaterialTheme.colorScheme.surfaceVariant

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                .border(width = 6.dp, color = corFundo, shape = CircleShape)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Saldo disponível", style = MaterialTheme.typography.labelLarge
                )

                Text(
                    saldoFormatado,
                    style = MaterialTheme.typography.headlineLarge,
                    color = corFundo,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                val ganhosFormatado = formatarMoeda(ganhos)
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Ganhos", style = MaterialTheme.typography.labelMedium)
                    Text(ganhosFormatado, style = MaterialTheme.typography.headlineSmall)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val gastosFormatado = formatarMoeda(gastos)
                    Text("Gastos", style = MaterialTheme.typography.labelMedium)
                    Text(gastosFormatado, style = MaterialTheme.typography.headlineSmall)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }


}

private fun formatarMoeda(saldo: Float): String {
    return when {
        saldo >= 1_000_000f -> "R$ %.1fM".format(saldo / 1_000_000f)
        saldo >= 1_000f -> "R$ %.1fk".format(saldo / 1_000f)
        else -> NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(saldo)
    }
}