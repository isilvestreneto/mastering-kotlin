package proway.capgemini.kotlin.atividade2.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import proway.capgemini.kotlin.atividade2.model.Contato
import proway.capgemini.kotlin.atividade2.viewmodel.ContatoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cadastro(viewModel: ContatoViewModel, navController: NavHostController) {

    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var telefone by rememberSaveable { mutableStateOf("") }
    var nascimento by rememberSaveable { mutableStateOf("") }
    var cep by rememberSaveable { mutableStateOf("") }
    var numero by rememberSaveable { mutableStateOf("") }

    val endereco by viewModel.endereco.collectAsState()

    val contatoSelecionado by viewModel.contatoSelecionado.collectAsState()

    LaunchedEffect(contatoSelecionado) { // Restart the effect when the pulse rate changes
        contatoSelecionado?.let {
            nome = it.nome
            email = it.email
            telefone = it.telefone
            nascimento = it.nascimento
            cep = it.cep?.filter { c -> c.isDigit() } ?: ""
            numero = it.numero ?: ""
        } ?: run {
            nome = ""; email = ""; telefone = ""
            nascimento = ""; cep = ""; numero = ""
        }
    }

    LaunchedEffect(cep) {
        if (cep.length == 8) viewModel.buscarCep(cep)
    }

    var erroNome by rememberSaveable { mutableStateOf(false) }
    var erroEmail by rememberSaveable { mutableStateOf(false) }
    var erroTelefone by rememberSaveable { mutableStateOf(false) }

    val isLoadingCep by viewModel.isLoadingCep.collectAsState()
    val erroCep by viewModel.erroCep.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar/editar contato") }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it; erroNome = false },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                isError = erroNome,
                supportingText = { if (erroNome) Text("Nome obrigatório") },
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; erroEmail = false },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                isError = erroEmail,
                supportingText = { if (erroEmail) Text("E-mail inválido") },
            )

            OutlinedTextField(
                value = telefone,
                onValueChange = {
                    if (it.length <= 13) telefone = it.filter { c -> c.isDigit() }; erroTelefone =
                    false
                },
                label = { Text("Telefone") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = TelefoneVisualTransformation(),
                isError = erroTelefone,
                supportingText = { if (erroTelefone) Text("Telefone inválido") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                )

            OutlinedTextField(
                value = nascimento,
                onValueChange = { if (it.length <= 8) nascimento = it.filter { c -> c.isDigit() } },
                label = { Text("Data de nascimento") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = DataVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            OutlinedTextField(
                value = cep,
                onValueChange = {
                    if (it.length <= 8) cep = it.filter { c -> c.isDigit() }
                },
                isError = erroCep != null,
                supportingText = { erroCep?.let { Text(it) } },
                label = { Text("CEP") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = CepVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            if (isLoadingCep) {
                CircularProgressIndicator()
            }

            // Campos automáticos, preenchidos pela API CEP
            OutlinedTextField(
                value = endereco?.logradouro ?: "",
                onValueChange = {},
                enabled = false,
                label = { Text("Logradouro") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = endereco?.bairro ?: "",
                onValueChange = {},
                enabled = false,
                label = { Text("Bairro") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = endereco?.localidade ?: "",
                onValueChange = {},
                enabled = false,
                label = { Text("Localidade") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = endereco?.uf ?: "",
                onValueChange = {},
                enabled = false,
                label = { Text("UF") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it },
                label = { Text("Número") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val emailValido = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    val telefoneValido = telefone.length >= 10
                    val nomeValido = nome.isNotBlank()

                    erroNome = !nomeValido
                    erroEmail = !emailValido
                    erroTelefone = !telefoneValido

                    if (!nomeValido || !emailValido || !telefoneValido) return@Button


                    val novoContato = Contato(
                        id = contatoSelecionado?.id ?: 0,
                        nome = nome,
                        email = email,
                        telefone = telefone,
                        nascimento = nascimento,
                        cep = cep,
                        numero = numero,
                        logradouro = endereco?.logradouro,
                        bairro = endereco?.bairro,
                        localidade = endereco?.localidade,
                        uf = endereco?.uf
                    )

                    if (contatoSelecionado == null) viewModel.criar(novoContato)
                    else viewModel.atualizar(novoContato)
                    viewModel.selecionarContato(null)
                    viewModel.limparEndereco()
                    navController.popBackStack()
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Salvar")
            }
        }
    }

}

class CepVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val formatted = buildString {
            digits.forEachIndexed { i, c ->
                if (i == 5) append('-')
                append(c)
            }
        }
        val offsetMap = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) =
                if (offset <= 5) offset else offset + 1

            override fun transformedToOriginal(offset: Int) =
                if (offset <= 5) offset else offset - 1
        }
        return TransformedText(AnnotatedString(formatted), offsetMap)
    }
}

// Data: 01011990 → 01/01/1990
class DataVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val formatted = buildString {
            digits.forEachIndexed { i, c ->
                if (i == 2 || i == 4) append('/')
                append(c)
            }
        }
        val offsetMap = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = when {
                offset <= 2 -> offset
                offset <= 4 -> offset + 1
                else -> offset + 2
            }

            override fun transformedToOriginal(offset: Int) = when {
                offset <= 2 -> offset
                offset <= 5 -> offset - 1
                else -> offset - 2
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMap)
    }
}

// Telefone: 5598999999999 → +55 (98) 99999-9999
class TelefoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val formatted = buildString {
            digits.forEachIndexed { i, c ->
                when (i) {
                    0 -> append("+")
                    2 -> append(" (")
                    4 -> append(") ")
                    9 -> append("-")
                }
                append(c)
            }
        }
        val offsetMap = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = when {
                offset <= 0 -> offset
                offset <= 2 -> offset + 1  // +
                offset <= 4 -> offset + 3  // + (
                offset <= 9 -> offset + 5  // + ( ) espaço
                else -> offset + 6         // + ( ) espaço -
            }

            override fun transformedToOriginal(offset: Int) = when {
                offset <= 1 -> 0
                offset <= 4 -> offset - 1
                offset <= 7 -> offset - 3
                offset <= 13 -> offset - 5
                else -> offset - 6
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMap)
    }
}