package proway.capgemini.kotlin.ui.sonhos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import proway.capgemini.kotlin.model.Sonho
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaSonhos(
    sonhos: List<Sonho>,
    saldo: Float,
    showSheet: Boolean,
    onDismissSheet: () -> Unit,
    onAdd: (Sonho) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (sonhos.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sem sonhos registrados.", style = MaterialTheme.typography.bodyLarge)
            Text("Toque em + para adicionar.", style = MaterialTheme.typography.bodySmall)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sonhos) { sonho ->
                SonhoCard(sonho, saldo)
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissSheet,
            sheetState = sheetState
        ) {
            FormularioSonho(
                onConfirm = { sonho ->
                    onAdd(sonho)
                    scope.launch { sheetState.hide() }.invokeOnCompletion { onDismissSheet() }
                },
                onCancel = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion { onDismissSheet() }
                }
            )
        }
    }
}

@Composable
private fun SonhoCard(sonho: Sonho, saldo: Float) {
    val formato = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    // Progresso = saldo atual / valor alvo, limitado entre 0 e 1
    val progresso = (saldo / sonho.valorAlvo).coerceIn(0f, 1f)
    val falta = (sonho.valorAlvo - saldo).coerceAtLeast(0f)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(sonho.nome, style = MaterialTheme.typography.bodyLarge)
                Text(formato.format(sonho.valorAlvo), style = MaterialTheme.typography.bodyLarge)
            }

            LinearProgressIndicator(
                progress = { progresso },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Saldo atual: ${formato.format(saldo)}",
                    style = MaterialTheme.typography.labelSmall
                )
                if (falta > 0f) {
                    Text(
                        "Faltam: ${formato.format(falta)}",
                        style = MaterialTheme.typography.labelSmall
                    )
                } else {
                    Text(
                        "Meta atingida!",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun FormularioSonho(
    onConfirm: (Sonho) -> Unit,
    onCancel: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var valorTexto by remember { mutableStateOf("") }
    var valorErro by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Novo Sonho", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = valorTexto,
            onValueChange = {
                valorTexto = it
                valorErro = false
            },
            label = { Text("Valor alvo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = valorErro,
            supportingText = { if (valorErro) Text("Valor inválido") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = {
                    val valor = valorTexto.replace(",", ".").toFloatOrNull()
                    if (valor == null) {
                        valorErro = true
                        return@Button
                    }
                    onConfirm(Sonho(nome = nome, valorAlvo = valor))
                },
                modifier = Modifier.weight(1f),
                enabled = nome.isNotBlank()
            ) {
                Text("Confirmar")
            }
        }
    }
}