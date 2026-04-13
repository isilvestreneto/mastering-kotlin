package proway.capgemini.kotlin.ui.ganhos

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import proway.capgemini.kotlin.model.Categoria
import proway.capgemini.kotlin.model.Ganho
import java.time.LocalDate
import proway.capgemini.kotlin.ui.util.formatarMoeda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaGanhos(
    ganhos: List<Ganho>,
    showSheet: Boolean,
    onDismissSheet: () -> Unit,
    onAdd: (Ganho) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (ganhos.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sem registros.", style = MaterialTheme.typography.bodyLarge)
            Text("Toque em + para adicionar.", style = MaterialTheme.typography.bodySmall)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ganhos) { ganho ->
                GanhoCard(ganho)
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissSheet,
            sheetState = sheetState
        ) {
            FormularioGanho(
                onConfirm = { ganho ->
                    onAdd(ganho)
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
private fun GanhoCard(ganho: Ganho) {
    val valorFormatado = formatarMoeda(ganho.valor)
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(ganho.categoria.icon, contentDescription = ganho.categoria.name)
                Column {
                    Text(ganho.descricao, style = MaterialTheme.typography.bodyLarge)
                    Text(ganho.data.toString(), style = MaterialTheme.typography.labelSmall)
                }
            }
            Text(valorFormatado, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun FormularioGanho(
    onConfirm: (Ganho) -> Unit,
    onCancel: () -> Unit
) {
    var descricao by remember { mutableStateOf("") }
    var valorTexto by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf(Categoria.OUTROS) }
    var valorErro by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Novo Ganho", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = valorTexto,
            onValueChange = {
                valorTexto = it
                valorErro = false
            },
            label = { Text("Valor") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = valorErro,
            supportingText = { if (valorErro) Text("Valor inválido") },
            singleLine = true
        )

        Text("Categoria", style = MaterialTheme.typography.labelLarge)
        // RadioButton por ser enum pequeno (4 opções) — mais legível que Dropdown em modal
        Column {
            Categoria.entries.forEach { categoria ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = categoriaSelecionada == categoria,
                        onClick = { categoriaSelecionada = categoria }
                    )
                    Icon(
                        categoria.icon,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(categoria.name)
                }
            }
        }

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
                    onConfirm(
                        Ganho(
                            descricao = descricao,
                            valor = valor,
                            data = LocalDate.now(),
                            categoria = categoriaSelecionada
                        )
                    )
                },
                modifier = Modifier.weight(1f),
                enabled = descricao.isNotBlank()
            ) {
                Text("Confirmar")
            }
        }
    }
}