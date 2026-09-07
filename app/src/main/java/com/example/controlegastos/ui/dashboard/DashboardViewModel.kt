package com.example.controlegastos.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controlegastos.domain.model.ResumoMensal
import com.example.controlegastos.domain.repository.ContaSaldoRepository
import com.example.controlegastos.domain.repository.DespesaRepository
import com.example.controlegastos.domain.repository.CartaoRepository
import com.example.controlegastos.domain.usecase.GetGastosPorCategoriaUseCase
import com.example.controlegastos.domain.usecase.GetResumoMensalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getResumoMensalUseCase: GetResumoMensalUseCase,
    private val getGastosPorCategoriaUseCase: GetGastosPorCategoriaUseCase,
    private val despesaRepository: DespesaRepository,
    private val cartaoRepository: CartaoRepository,
    private val contaSaldoRepository: ContaSaldoRepository
) : ViewModel() {

    private val mesSelecionado = MutableStateFlow(YearMonth.now())
    private val numerosVisiveis = MutableStateFlow(true)

    // fluxos auxiliares
    private val cartoesAtivosFlow = cartaoRepository.observarAtivos()

    private val saldoPositivoFlow = contaSaldoRepository
        .observarTodas()
        .map { contas -> contas.filter { it.ativo }.sumOf { it.saldoCentavos } }

    private val faturasAbertasFlow = despesaRepository
        .observarFaturasAbertasPorMes()
        .map { faturas -> faturas.sumOf { it.totalCentavos } }

    // placeholder para receitas (substituir se tiver fonte real)
    private val receitasFlow = MutableStateFlow(0L)

    val uiState: StateFlow<DashboardUiState> = combine(
        mesSelecionado,
        numerosVisiveis
    ) { mesAno, valoresVisiveis ->
        mesAno to valoresVisiveis
    }
        .flatMapLatest { (mesAno, valoresVisiveis) ->
            // flows que dependem do mes selecionado
            val resumoFlow = getResumoMensalUseCase(mes = mesAno.monthValue, ano = mesAno.year)
            val gastosPorCategoriaFlow = getGastosPorCategoriaUseCase(mes = mesAno.monthValue, ano = mesAno.year)
            val despesasDoMesFlow = despesaRepository.observarDespesasDetalhadasPorMes(mes = mesAno.monthValue, ano = mesAno.year)

            // passo 1: combine resumo, gastosPorCategoria e despesasDoMes
            combine(resumoFlow, gastosPorCategoriaFlow, despesasDoMesFlow) { resumo, gastos, despesas ->
                Triple(resumo, gastos, despesas)
            }.flatMapLatest { (resumo, gastosPorCategoria, despesas) ->
                // passo 2: combine o resultado com os outros flows (cartoes, saldo, faturas, receitas)
                combine(
                    cartoesAtivosFlow,
                    saldoPositivoFlow,
                    faturasAbertasFlow,
                    receitasFlow
                ) { cartoes, saldoPositivo, totalFaturas, totalReceitas ->
                    DashboardUiState(
                        mesSelecionado = mesAno,
                        resumoMensal = resumo,
                        gastosPorCategoria = gastosPorCategoria,
                        transacoesDoMes = despesas.sortedByDescending { it.dataVencimento },
                        cartoes = cartoes,
                        saldoPositivo = saldoPositivo,
                        totalFaturas = totalFaturas,
                        totalReceitas = totalReceitas,
                        numerosVisiveis = valoresVisiveis,
                        carregando = false
                    )
                }.catch { e ->
                    // fallback em caso de erro
                    emit(
                        DashboardUiState(
                            mesSelecionado = mesAno,
                            resumoMensal = ResumoMensal(0L, 0L, 0L),
                            gastosPorCategoria = emptyList(),
                            transacoesDoMes = emptyList(),
                            cartoes = emptyList(),
                            saldoPositivo = 0L,
                            totalFaturas = 0L,
                            totalReceitas = 0L,
                            numerosVisiveis = valoresVisiveis,
                            carregando = false
                        )
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState(carregando = false)
        )

    fun irParaMesAnterior() {
        mesSelecionado.value = mesSelecionado.value.minusMonths(1)
    }

    fun irParaProximoMes() {
        mesSelecionado.value = mesSelecionado.value.plusMonths(1)
    }

    fun alternarVisibilidadeValores() {
        numerosVisiveis.value = !numerosVisiveis.value
    }
}