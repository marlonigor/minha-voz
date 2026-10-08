package br.com.minhavoz.ui.cartoes

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Testes unitarios para [parseHexColor].
 */
class ColorUtilsTest {

    @Test
    fun deveConverterHexDeSeisDigitosComSucesso() {
        val cor: Color = parseHexColor("#1E88E5")
        assertEquals(Color(0xFF1E88E5), cor)
    }

    @Test
    fun deveConverterHexDeOitoDigitosComAlfa() {
        val cor: Color = parseHexColor("#801E88E5")
        assertEquals(Color(0x801E88E5), cor)
    }

    @Test
    fun deveRetornarFallbackParaFormatoInvalido() {
        val fallback = Color.Red
        assertEquals(fallback, parseHexColor("invalido", fallback))
        assertEquals(fallback, parseHexColor("#123", fallback))
        assertEquals(fallback, parseHexColor("", fallback))
    }
}
