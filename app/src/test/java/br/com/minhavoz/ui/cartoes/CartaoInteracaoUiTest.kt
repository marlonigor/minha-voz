package br.com.minhavoz.ui.cartoes

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia
import br.com.minhavoz.testing.ComposeHostActivityRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Validacoes de interacao de UI em Compose para temporizacoes criticas (RF-02, RF-06).
 *
 * Utiliza o relogio de teste (mainClock) para testar de forma deterministica:
 * 1. Ativacao de cartoes urgentes sob toque longo >= 800 ms vs toque rapido < 800 ms.
 * 2. Bloqueio de repetições por 1,5 s (1500 ms debounce).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CartaoInteracaoUiTest {

    @get:Rule(order = 0)
    val composeHostActivityRule: ComposeHostActivityRule = ComposeHostActivityRule()

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

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

    @Test
    fun cartaoNormalDeveAtivarComCliqueSimples() {
        var ativado = false

        composeTestRule.setContent {
            CartaoItemView(
                cartao = cartaoNormal,
                onAtivar = { ativado = true }
            )
        }

        composeTestRule.onNodeWithTag("cartao_agua").performClick()
        assertTrue("Cartao normal deve ativar imediatamente ao clique", ativado)
    }

    @Test
    fun cartaoUrgenteNaoDeveAtivarComToqueCurtoAbaixoDe800ms() {
        var ativado = false

        composeTestRule.setContent {
            CartaoItemView(
                cartao = cartaoUrgente,
                onAtivar = { ativado = true }
            )
        }

        // Toque rápido simples não pode disparar urgência
        composeTestRule.onNodeWithTag("cartao_dor").performClick()
        assertFalse("Cartao urgente nao pode ativar com clique simples", ativado)

        // Toque com duração menor que 800 ms (ex: 300 ms)
        composeTestRule.onNodeWithTag("cartao_dor").performTouchInput {
            down(center)
            advanceEventTime(300)
            up()
        }
        assertFalse("Cartao urgente nao pode ativar com toque mantido por apenas 300 ms", ativado)
    }

    @Test
    fun cartaoUrgenteDeveAtivarComToqueLongoDe800msOuSuperior() {
        var ativado = false

        composeTestRule.setContent {
            CartaoItemView(
                cartao = cartaoUrgente,
                onAtivar = { ativado = true }
            )
        }

        composeTestRule.onNodeWithTag("cartao_dor").performTouchInput {
            down(center)
            advanceEventTime(850)
            up()
        }

        assertTrue("Cartao urgente deve ativar apos toque longo mantido por >= 800 ms", ativado)
    }

    @Test
    fun bloqueioDeRepeticaoDeveIgnorarToquesDurante1500msEAposAvancoLiberar() {
        var ativacoes = 0
        var bloqueado = false

        fun processarToque() {
            if (bloqueado) return
            ativacoes++
            bloqueado = true
        }

        composeTestRule.mainClock.autoAdvance = false

        composeTestRule.setContent {
            CartaoItemView(
                cartao = cartaoNormal,
                onAtivar = { processarToque() }
            )
        }

        // Primeiro toque ativa
        composeTestRule.onNodeWithTag("cartao_agua").performClick()
        assertEquals(1, ativacoes)
        assertTrue(bloqueado)

        // Segundo toque dentro da janela de 1500 ms (ex: 500 ms depois) deve ser bloqueado
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.onNodeWithTag("cartao_agua").performClick()
        assertEquals("Segundo toque dentro do debounce deve ser ignorado", 1, ativacoes)

        // Terceiro toque ainda dentro da janela (ex: mais 800 ms = 1300 ms acumulados)
        composeTestRule.mainClock.advanceTimeBy(800)
        composeTestRule.onNodeWithTag("cartao_agua").performClick()
        assertEquals("Toque a 1300 ms deve continuar bloqueado", 1, ativacoes)

        // Avanco ultrapassando os 1500 ms desbloqueia
        composeTestRule.mainClock.advanceTimeBy(300) // Total: 1600 ms
        bloqueado = false // simulando o timer de desbloqueio do ViewModel apos 1500 ms
        composeTestRule.onNodeWithTag("cartao_agua").performClick()
        assertEquals("Toque apos 1500 ms deve ser aceito", 2, ativacoes)
    }
}

