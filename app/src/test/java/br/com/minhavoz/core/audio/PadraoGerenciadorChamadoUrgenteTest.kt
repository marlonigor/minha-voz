package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Testes unitarios para [PadraoGerenciadorChamadoUrgente].
 * Valida o ciclo ciclico de 3 emissoes a cada 20s e a elevacao de volume de alarme (RF-05, TC-12, TC-16).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PadraoGerenciadorChamadoUrgenteTest {

    private lateinit var fakeAudioPlayer: FakeCartaoAudioPlayer
    private lateinit var fakeControladorVolume: FakeControladorVolumeAlarme
    private lateinit var gerenciador: PadraoGerenciadorChamadoUrgente

    private val cartaoUrgente = Cartao(
        id = "dor",
        rotulo = "DOR",
        frase = "Estou com dor. Preciso de ajuda.",
        pictograma = "pictograma_dor",
        corFundoHex = "#E53935",
        corTextoHex = "#FFFFFF",
        urgencia = NivelUrgencia.URGENTE,
        arquivoVoz = "voz_dor.ogg",
        posicao = 5,
        barraFixa = false
    )

    @Before
    fun setUp() {
        fakeAudioPlayer = FakeCartaoAudioPlayer()
        fakeControladorVolume = FakeControladorVolumeAlarme(volumeAtual = 2, volumeMaximo = 10)
        gerenciador = PadraoGerenciadorChamadoUrgente(
            audioPlayer = fakeAudioPlayer,
            controladorVolume = fakeControladorVolume,
            intervaloRepeticaoMs = 20_000L,
            maximoEmissoes = 3,
            percentualVolumeMinimo = 0.8f
        )
    }

    @Test
    fun deveElevarVolumeEEmitirImediatamenteAoIniciar() = runTest {
        gerenciador.iniciarCicloUrgente(cartaoUrgente, this)

        assertEquals(8, fakeControladorVolume.volumeAtual)
        assertEquals(1, fakeAudioPlayer.chamadasReproduzir.size)
        assertTrue(gerenciador.emExecucao)
    }

    @Test
    fun deveRepetirEmissaoApos20SegundosAteLimiteDeTres() = runTest {
        gerenciador.iniciarCicloUrgente(cartaoUrgente, this)

        advanceTimeBy(20_001L)
        runCurrent()
        assertEquals(2, fakeAudioPlayer.chamadasReproduzir.size)

        advanceTimeBy(20_001L)
        runCurrent()
        assertEquals(3, fakeAudioPlayer.chamadasReproduzir.size)

        advanceTimeBy(20_001L)
        runCurrent()
        assertEquals(3, fakeAudioPlayer.chamadasReproduzir.size)
        assertEquals(2, fakeControladorVolume.volumeAtual)
        assertFalse(gerenciador.emExecucao)
    }

    @Test
    fun deveCancelarCicloERestaurarVolumeAoChamarCancelar() = runTest {
        gerenciador.iniciarCicloUrgente(cartaoUrgente, this)
        advanceTimeBy(10_000L)

        gerenciador.cancelarCicloUrgente()

        assertEquals(2, fakeControladorVolume.volumeAtual)
        assertEquals(1, fakeAudioPlayer.paradas)
        assertFalse(gerenciador.emExecucao)

        advanceTimeBy(25_000L)
        runCurrent()
        assertEquals(1, fakeAudioPlayer.chamadasReproduzir.size)
    }

    @Test
    fun deveReiniciarCicloAoChamarReiniciar() = runTest {
        gerenciador.iniciarCicloUrgente(cartaoUrgente, this)
        advanceTimeBy(15_000L)

        gerenciador.reiniciarCicloUrgente(cartaoUrgente, this)

        assertEquals(2, fakeAudioPlayer.chamadasReproduzir.size)
        assertTrue(gerenciador.emExecucao)
    }

    @Test
    fun naoDeveAlterarVolumeSeJaEstiverAcimaDoMinimo() = runTest {
        fakeControladorVolume.volumeAtual = 9

        gerenciador.iniciarCicloUrgente(cartaoUrgente, this)

        assertEquals(9, fakeControladorVolume.volumeAtual)
        assertEquals(0, fakeControladorVolume.elevacoesContadas)
    }
}
