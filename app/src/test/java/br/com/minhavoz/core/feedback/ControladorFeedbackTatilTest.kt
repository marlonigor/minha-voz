package br.com.minhavoz.core.feedback

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Testes unitarios para [ControladorFeedbackTatil] (RF-07).
 */
class ControladorFeedbackTatilTest {

    private lateinit var fakeFeedback: FakeControladorFeedbackTatil

    @Before
    fun setUp() {
        fakeFeedback = FakeControladorFeedbackTatil()
    }

    @Test
    fun deveRegistrarVibracaoDeToqueNormal() {
        fakeFeedback.vibrarToqueNormal()
        assertEquals(1, fakeFeedback.vibracoesNormais)
    }

    @Test
    fun deveRegistrarVibracaoDeChamadoUrgente() {
        fakeFeedback.vibrarChamadoUrgente()
        assertEquals(1, fakeFeedback.vibracoesUrgentes)
    }

    @Test
    fun deveRegistrarVibracaoDeCliqueBotao() {
        fakeFeedback.vibrarCliqueBotao()
        assertEquals(1, fakeFeedback.vibracoesBotoes)
    }
}
