package proway.capgemini.kotlin.atividade2.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import proway.capgemini.kotlin.atividade2.model.Contato
import proway.capgemini.kotlin.atividade2.viewmodel.ContatoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Listagem(
    contatos: List<Contato>, viewModel: ContatoViewModel, navController: NavHostController
) {
    var contatoParaDeletar by remember { mutableStateOf<Contato?>(null) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Contatos") })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate("cadastro") }) {
            Icon(Icons.Default.Add, contentDescription = "Novo contato")
        }
    }) { paddingValues ->

        contatoParaDeletar?.let { contato ->
            AlertDialog(
                onDismissRequest = { contatoParaDeletar = null },
                title = { Text("Confirmar exclusão") },
                text = { Text("Deseja excluir ${contato.nome}?") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.deletar(contato)
                        contatoParaDeletar = null
                    }) { Text("Excluir") }
                },
                dismissButton = {
                    Button(onClick = { contatoParaDeletar = null }) { Text("Cancelar") }
                })
        }


        if (contatos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Column() {
                    Text("Sem contato cadastrado")
                }
            }
        } else {

            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contatos) { contato ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    contato.nome,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    contato.email, style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    contato.telefone, style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row() {
                                IconButton(onClick = {
                                    viewModel.selecionarContato(contato)
                                    navController.navigate("cadastro")
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")

                                }
                                IconButton(onClick = { contatoParaDeletar = contato }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Deletar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }

                            }

                        }
                    }
                }
            }
        }
    }

}