package proway.capgemini.kotlin.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import proway.capgemini.kotlin.model.Categoria
import proway.capgemini.kotlin.model.Ganho
import proway.capgemini.kotlin.model.Gasto
import proway.capgemini.kotlin.model.Sonho
import proway.capgemini.kotlin.ui.constantes.Rotas
import proway.capgemini.kotlin.ui.ganhos.TelaGanhos
import proway.capgemini.kotlin.ui.gastos.TelaGastos
import proway.capgemini.kotlin.ui.home.TelaInicio
import proway.capgemini.kotlin.ui.sonhos.TelaSonhos
import java.time.LocalDate

@Composable
fun AppScreen() {
    // 1. Segurar estado
    val ganhos = remember {
        mutableStateListOf(
            Ganho("Salário", 8500f, LocalDate.of(2026, 4, 1), Categoria.OUTROS),
            Ganho("Freelance app mobile", 2000f, LocalDate.of(2026, 4, 5), Categoria.OUTROS),
            Ganho("Aluguel sala", 1200f, LocalDate.of(2026, 4, 10), Categoria.CASA),
            Ganho("Dividendos", 350f, LocalDate.of(2026, 4, 12), Categoria.OUTROS)
        )
    }
    val gastos = remember {
        mutableStateListOf(
            Gasto("Supermercado", 680f, LocalDate.of(2026, 4, 3), Categoria.CASA),
            Gasto("Academia", 120f, LocalDate.of(2026, 4, 4), Categoria.SAUDE)
        )
    }
    val sonhos = remember {
        mutableStateListOf(
            Sonho("Viagem ao Japão", 15000f)
        )
    }

    val ganhosTotais by remember {
        derivedStateOf {
            ganhos.sumOf { it.valor.toDouble() }.toFloat()
        }
    }

    val gastosTotais by remember {
        derivedStateOf {
            gastos.sumOf { it.valor.toDouble() }.toFloat()
        }
    }

    val saldo by remember {
        derivedStateOf {
            ganhosTotais - gastosTotais
        }
    }

    val navController: NavHostController = rememberNavController()

    // 3. Montar o Scafoold com Bottomnavigationbar e NavHost
    Scaffold(
        bottomBar = {
            NavigationBar(

            ) {
                // Link - Início
                NavigationBarItem(
                    selected = true,
                    onClick = { navController.navigate(Rotas.INICIO) },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) })

                // Link - Ganhos
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Rotas.GANHOS) },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null
                        )
                    })

                // Link - Gastos
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Rotas.GASTOS) },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.TrendingDown, contentDescription = null
                        )
                    })

                // Link - Sonhos
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Rotas.SONHOS) },
                    icon = { Icon(Icons.Default.Cloud, contentDescription = null) })
            }
        }) { innerPadding ->

        NavHost(
            navController,
            startDestination = Rotas.INICIO,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rotas.INICIO) { TelaInicio(saldo, ganhosTotais, gastosTotais) }
            composable(Rotas.GANHOS) {
                TelaGanhos(ganhos, { ganho ->
                    ganhos.add(ganho)
                })
            }
            composable(Rotas.GASTOS) {
                TelaGastos(gastos, { gasto ->
                    gastos.add(gasto)
                })
            }
            composable(Rotas.SONHOS) { TelaSonhos(sonhos, saldo) { sonhos.add(it) } }
        }
    }

}




