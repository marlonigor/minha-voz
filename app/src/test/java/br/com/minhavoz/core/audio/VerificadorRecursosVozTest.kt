package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Testes unitarios para [VerificadorRecursosVoz] e regras de integridade sonora (RF-04, RF-09).
 */
class VerificadorRecursosVozTest {

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

    private val cartaoDor = Cartao(
        id = "dor",
        rotulo = "DOR",
        frase = "Estou com dor.",
        pictograma = "pictograma_dor",
        corFundoHex = "#E53935",
        corTextoHex = "#FFFFFF",
        urgencia = NivelUrgencia.URGENTE,
        arquivoVoz = "voz_dor.ogg",
        posicao = 5,
        barraFixa = false
    )

    @Test
    fun deveIdentificarTudoOperacionalQuandoArquivosETtsEstaoPresentes() {
        val fakeVerificador = FakeVerificadorRecursosVoz(
            alertaUrgentePresente = true,
            ttsDisponivel = true,
            arquivosPresentes = mutableSetOf("voz_agua.ogg", "voz_dor.ogg")
        )

        val relatorio = fakeVerificador.verificar(listOf(cartaoAgua, cartaoDor))

        assertFalse(relatorio.possuiPendenciaCritica)
        assertEquals(2, relatorio.statusCartoes.size)
        assertTrue(relatorio.statusCartoes.all { it.operacional })
    }

    @Test
    fun deveManterOperacionalViaTtsQuandoArquivoGravadoEstiverAusente() {
        val fakeVerificador = FakeVerificadorRecursosVoz(
            alertaUrgentePresente = true,
            ttsDisponivel = true,
            arquivosPresentes = mutableSetOf()
        )

        val relatorio = fakeVerificador.verificar(listOf(cartaoAgua))

        assertFalse(relatorio.possuiPendenciaCritica)
        val status = relatorio.statusCartoes.first()
        assertFalse(status.arquivoGravadoPresente)
        assertTrue(status.ttsDisponivel)
        assertTrue(status.operacional)
    }

    @Test
    fun deveSinalizarPendenciaCriticaQuandoFaltarArquivoETts() {
        val fakeVerificador = FakeVerificadorRecursosVoz(
            alertaUrgentePresente = true,
            ttsDisponivel = false,
            arquivosPresentes = mutableSetOf()
        )

        val relatorio = fakeVerificador.verificar(listOf(cartaoAgua))

        assertTrue(relatorio.possuiPendenciaCritica)
        val status = relatorio.statusCartoes.first()
        assertFalse(status.operacional)
    }

    @Test
    fun deveSinalizarPendenciaCriticaQuandoAlertaUrgenteEstiverAusente() {
        val fakeVerificador = FakeVerificadorRecursosVoz(
            alertaUrgentePresente = false,
            ttsDisponivel = true,
            arquivosPresentes = mutableSetOf("voz_agua.ogg")
        )

        val relatorio = fakeVerificador.verificar(listOf(cartaoAgua))

        assertTrue(relatorio.possuiPendenciaCritica)
        assertFalse(relatorio.alertaUrgentePresente)
    }
}
