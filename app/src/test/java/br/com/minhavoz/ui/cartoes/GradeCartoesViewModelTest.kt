package br.com.minhavoz.ui.cartoes

import br.com.minhavoz.core.data.FakeCartaoRepository
import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Testes unitarios para [GradeCartoesViewModel].
 * Valida carregamento inicial, fluxo de selecao, protecao de repeticao e fechamento automatico.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GradeCartoesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = FakeCartaoRepository()

    private val cartaoAgua = Cartao(
        id = "agua",
        rotulo = "ÁGUA",
        frase = "Eu quero água, por favor.",
        pictograma = "pictograma_agua",
        corFundoHex = "#1E88E5",
        corTextoHex = "#FFFFFF",
        urgencia = NivelUrgencia.NORMAL,
        arquivoVoz = "voz_agua.ogg",
        posicao = 1,
        barraFixa = false
    )

    private val cartaoSim = Cartao(
        id = "sim",
        rotulo = "SIM",
        frase = "Sim.",
        pictograma = "pictograma_sim",
        corFundoHex = "#43A047",
        corTextoHex = "#FFFFFF",
        urgencia = NivelUrgencia.NORMAL,
        arquivoVoz = "voz_sim.ogg",
        posicao = 7,
        barraFixa = true
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository.definirCartoes(listOf(cartaoAgua, cartaoSim))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun deveCarregarCartoesAoInicializar() {
        val viewModel = GradeCartoesViewModel(repository)
        val estado = viewModel.uiState.value

        assertEquals(1, estado.cartoesGrade.size)
        assertEquals(1, estado.cartoesBarraFixa.size)
        assertEquals("agua", estado.cartoesGrade.first().id)
        assertEquals("sim", estado.cartoesBarraFixa.first().id)
        assertNull(estado.erro)
    }

    @Test
    fun deveSelecionarCartaoEAtivarBloqueio() = runTest {
        val viewModel = GradeCartoesViewModel(repository)
        viewModel.selecionarCartao(cartaoAgua)

        val estado = viewModel.uiState.value
        assertEquals("agua", estado.cartaoEmDetalhe?.id)
        assertTrue(estado.bloqueadoPorRepeticao)
    }

    @Test
    fun deveIgnorarNovoToqueDuranteBloqueioDeRepeticao() = runTest {
        val viewModel = GradeCartoesViewModel(repository)
        viewModel.selecionarCartao(cartaoAgua)
        viewModel.selecionarCartao(cartaoSim)

        val estado = viewModel.uiState.value
        assertEquals("agua", estado.cartaoEmDetalhe?.id)
    }

    @Test
    fun deveLiberarBloqueioAposDebounce() = runTest {
        val viewModel = GradeCartoesViewModel(repository, debounceMs = 1500L)
        viewModel.selecionarCartao(cartaoAgua)

        assertTrue(viewModel.uiState.value.bloqueadoPorRepeticao)
        testDispatcher.scheduler.advanceTimeBy(1501L)
        assertFalse(viewModel.uiState.value.bloqueadoPorRepeticao)
    }

    @Test
    fun deveFecharDetalheCorretamente() = runTest {
        val viewModel = GradeCartoesViewModel(repository)
        viewModel.selecionarCartao(cartaoAgua)
        viewModel.fecharDetalhe()

        assertNull(viewModel.uiState.value.cartaoEmDetalhe)
    }

    @Test
    fun deveFecharCartaoNormalAutomaticamenteAposTimeout() = runTest {
        val viewModel = GradeCartoesViewModel(
            cartaoRepository = repository,
            tempoFechamentoNormalMs = 1000L
        )
        viewModel.selecionarCartao(cartaoAgua)
        assertNotNull(viewModel.uiState.value.cartaoEmDetalhe)

        testDispatcher.scheduler.advanceTimeBy(1001L)
        assertNull(viewModel.uiState.value.cartaoEmDetalhe)
    }

    @Test
    fun deveReiniciarTimeoutAoRepetirCartao() = runTest {
        val viewModel = GradeCartoesViewModel(
            cartaoRepository = repository,
            tempoFechamentoNormalMs = 1000L
        )
        viewModel.selecionarCartao(cartaoAgua)

        testDispatcher.scheduler.advanceTimeBy(600L)
        viewModel.repetirCartaoAtual()

        testDispatcher.scheduler.advanceTimeBy(600L)
        assertNotNull(viewModel.uiState.value.cartaoEmDetalhe)

        testDispatcher.scheduler.advanceTimeBy(401L)
        assertNull(viewModel.uiState.value.cartaoEmDetalhe)
    }

    @Test
    fun deveTratarErroDeCarregamento() {
        repository.deveLancarErro = true
        val viewModel = GradeCartoesViewModel(repository)

        val estado = viewModel.uiState.value
        assertTrue(estado.cartoesGrade.isEmpty())
        assertNotNull(estado.erro)
    }
}
