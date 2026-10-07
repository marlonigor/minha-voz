package br.com.minhavoz

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Teste de sanidade do ambiente de testes unitarios.
 * Valida a execucao headless em conformidade com os principios F.I.R.S.T.
 */
class BaselineTest {

    @Test
    fun environmentConfiguredCorrectly() {
        assertTrue("Ambiente de teste unitario configurado com sucesso", true)
    }
}
