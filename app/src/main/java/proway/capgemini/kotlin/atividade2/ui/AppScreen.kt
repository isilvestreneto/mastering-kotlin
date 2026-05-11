package proway.capgemini.kotlin.atividade2.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import proway.capgemini.kotlin.atividade2.repository.ContatoRepository
import proway.capgemini.kotlin.atividade2.viewmodel.ContatoViewModel
import proway.capgemini.kotlin.atividade2.viewmodel.ContatoViewModelFactory
import proway.capgemini.kotlin.atividade2.database.ContatoDB


@Composable
fun AppScreen() {
    val context = LocalContext.current
    val db = ContatoDB.getDatabase(context)
    val repository = ContatoRepository(db.contatoDao())
    val factory = ContatoViewModelFactory(repository)
    val viewModel: ContatoViewModel = viewModel(factory = factory)

    val navController: NavHostController = rememberNavController()
    // currentBackStackEntryAsState() expõe a rota ativa como State — recompõe o bottomBar automaticamente
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            val contatos by viewModel.contatos.collectAsState()
            Listagem(contatos = contatos, viewModel = viewModel, navController = navController)
        }
        composable("cadastro") {
            Cadastro(viewModel = viewModel, navController = navController)
        }
    }

}