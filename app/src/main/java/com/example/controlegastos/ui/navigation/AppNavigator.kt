@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class
)

package com.example.controlegastos.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.example.controlegastos.ui.categoria.CategoriaScreen
import com.example.controlegastos.ui.dashboard.DashboardScreen
import com.example.controlegastos.ui.despesa.InserirDespesaScreen
import com.example.controlegastos.ui.edicao.EdicaoScreen
import com.example.controlegastos.ui.gastos.GastosScreen
import com.example.controlegastos.ui.pendencias.PendenciasScreen
import com.example.controlegastos.ui.settings.SettingsScreen
import com.example.controlegastos.ui.timeline.TimelineScreen
import com.example.controlegastos.ui.transacoes.TransacoesScreen

@Composable
fun AppNavigator() {
    Navigator(
        screen = DashboardVoyagerScreen()
    )
}

// Helpers para navegação fluida por abas no Voyager.
// Substitui a pilha para não acumular telas iguais.
private fun Navigator?.irParaAba(screen: Screen) {
    this?.replaceAll(screen)
}

private class DashboardVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        DashboardScreen(
            onGerenciarCategorias = {
                navigator?.push(CategoriasScreen())
            },
            onAdicionarDespesa = {
                navigator?.push(InserirDespesaVoyagerScreen())
            },
            onVerTodasTransacoes = {
                navigator?.irParaAba(TransacoesVoyagerScreen())
            },
            onVerProjecoes = {
                navigator?.push(TimelineVoyagerScreen())
            },
            onVerPendencias = {
                navigator?.irParaAba(GastosVoyagerScreen())
            },
            onAbrirConfiguracoes = {
                navigator?.push(SettingsVoyagerScreen())
            },
            onAbrirCartoes = {
                navigator?.irParaAba(EdicaoVoyagerScreen())
            },
            onNavegarGastos = {
                navigator?.irParaAba(GastosVoyagerScreen())
            }
        )
    }
}

private class TransacoesVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        TransacoesScreen(
            // Volta explicitamente para a Dashboard.
            onVoltar = {
                navigator?.replaceAll(DashboardVoyagerScreen())
            },
            onNavegarInicio = {
                navigator?.irParaAba(DashboardVoyagerScreen())
            },
            onNavegarTransacoes = {
                // Já está na tela de transações.
            },
            onNavegarGastos = {
                navigator?.irParaAba(GastosVoyagerScreen())
            },
            onNavegarEdicao = {
                navigator?.irParaAba(EdicaoVoyagerScreen())
            },
            onAdicionarDespesa = {
                navigator?.push(InserirDespesaVoyagerScreen())
            }
        )
    }
}

private class GastosVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        GastosScreen(
            // Volta explicitamente para a Dashboard.
            onVoltar = {
                navigator?.replaceAll(DashboardVoyagerScreen())
            },
            onAbrirEdicao = {
                navigator?.push(EdicaoVoyagerScreen())
            },
            onNavegarInicio = {
                navigator?.irParaAba(DashboardVoyagerScreen())
            },
            onNavegarTransacoes = {
                navigator?.irParaAba(TransacoesVoyagerScreen())
            },
            onNavegarGastos = {
                // Já está na tela de gastos.
            },
            onNavegarEdicao = {
                navigator?.irParaAba(EdicaoVoyagerScreen())
            },
            onAdicionarDespesa = {
                navigator?.push(InserirDespesaVoyagerScreen())
            }
        )
    }
}

private class EdicaoVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        EdicaoScreen(
            // Volta explicitamente para a Dashboard.
            onVoltar = {
                navigator?.replaceAll(DashboardVoyagerScreen())
            },
            onNavegarInicio = {
                navigator?.irParaAba(DashboardVoyagerScreen())
            },
            onNavegarTransacoes = {
                navigator?.irParaAba(TransacoesVoyagerScreen())
            },
            onNavegarGastos = {
                navigator?.irParaAba(GastosVoyagerScreen())
            },
            onNavegarEdicao = {
                // Já está na tela de edição.
            },
            onAdicionarDespesa = {
                navigator?.push(InserirDespesaVoyagerScreen())
            }
        )
    }
}

// Demais telas auxiliares continuam inalteradas abaixo.
private class CategoriasScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        CategoriaScreen(
            onVoltar = {
                navigator?.pop()
            }
        )
    }
}

private class InserirDespesaVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        InserirDespesaScreen(
            onVoltar = {
                navigator?.pop()
            }
        )
    }
}

private class TimelineVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        TimelineScreen(
            onVoltar = {
                navigator?.pop()
            }
        )
    }
}

private class SettingsVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        SettingsScreen(
            onVoltar = {
                navigator?.pop()
            }
        )
    }
}

private class PendenciasVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        PendenciasScreen(
            onVoltar = {
                navigator?.pop()
            }
        )
    }
}