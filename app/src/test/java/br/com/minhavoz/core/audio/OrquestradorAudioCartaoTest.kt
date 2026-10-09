package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Testes unitarios para [OrquestradorAudioCartao].
 * Valida a prioridade de arquivos gravados e fallback defensivo para sintese de fala (RF-04, ADR-003).
 */
class OrquestradorAudioCartaoTest {

    private lateinit var fakeGravado: FakeVozGravadaPlayer
    private lateinit var fakeSintetizador: FakeSintetizadorVoz
    private lateinit var orquestrador: OrquestradorAudioCartao

    private val cartaoNormal = Cartao(
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
        fakeGravado = FakeVozGravadaPlayer()
        fakeSintetizador = FakeSintetizadorVoz()
        orquestrador = OrquestradorAudioCartao(fakeGravado, fakeSintetizador)
    }

    @Test
    fun deveReproduzirArquivoGravadoQuandoDisponivel() {
        fakeGravado.simularSucesso = true

        orquestrador.reproduzir(cartaoNormal)

        assertEquals(1, fakeGravado.chamadasReproduzir.size)
        assertEquals("voz_agua.ogg", fakeGravado.chamadasReproduzir.first().first)
        assertEquals(NivelUrgencia.NORMAL, fakeGravado.chamadasReproduzir.first().second)
        assertTrue(fakeSintetizador.chamadasFalar.isEmpty())
    }

    @Test
    fun deveAcionarSintetizadorVozComoFallbackQuandoGravacaoFalhar() {
        fakeGravado.simularSucesso = false

        orquestrador.reproduzir(cartaoUrgente)

        assertEquals(1, fakeGravado.chamadasReproduzir.size)
        assertEquals(1, fakeSintetizador.chamadasFalar.size)
        assertEquals("Estou com dor. Preciso de ajuda.", fakeSintetizador.chamadasFalar.first().first)
        assertEquals(NivelUrgencia.URGENTE, fakeSintetizador.chamadasFalar.first().second)
    }

    @Test
    fun devePropagarComandoDeParadaParaAmbosReprodutores() {
        orquestrador.parar()

        assertEquals(1, fakeGravado.paradas)
        assertEquals(1, fakeSintetizador.paradas)
    }

    @Test
    fun devePropagarComandoDeLiberacaoParaAmbosReprodutores() {
        orquestrador.liberar()

        assertEquals(1, fakeGravado.liberacoes)
        assertEquals(1, fakeSintetizador.liberacoes)
    }
}
