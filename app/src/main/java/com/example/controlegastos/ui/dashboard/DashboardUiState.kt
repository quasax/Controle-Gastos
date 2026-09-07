package com.example.controlegastos.ui.dashboard

import com.example.controlegastos.domain.model.Cartao
import com.example.controlegastos.domain.model.DespesaDetalhada
import com.example.controlegastos.domain.model.GastoPorCategoria
import com.example.controlegastos.domain.model.ResumoMensal
import java.time.YearMonth

data class DashboardUiState(
    val mesSelecionado: YearMonth = YearMonth.now(),

    val resumoMensal: ResumoMensal = ResumoMensal(
        totalGasto = 0L,
        totalPago = 0L,
        totalPendente = 0L
    ),

    val gastosPorCategoria: List<GastoPorCategoria> = emptyList(),
    val transacoesDoMes: List<DespesaDetalhada> = emptyList(),
    val cartoes: List<Cartao> = emptyList(),

    // NOVOS CAMPOS expostos para o Dashboard/card saldo
    val saldoPositivo: Long = 0L,   // soma de saldos das contas ativas (centavos)
    val totalFaturas: Long = 0L,    // soma das faturas abertas (centavos)
    val totalReceitas: Long = 0L,   // placeholder (centavos) -- ajuste se tiver fonte de receitas

    val numerosVisiveis: Boolean = true,
    val carregando: Boolean = true
)