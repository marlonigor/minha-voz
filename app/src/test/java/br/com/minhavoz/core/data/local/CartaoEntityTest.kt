package br.com.minhavoz.core.data.local

import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Testes unitarios para [CartaoEntity] e seus mapeadores bidirecionais.
 */
class CartaoEntityTest {

    @Test
    fun deveConverterDominioParaEntidadeComFidelidade() {
        val cartao = Cartao(
            id = "agua",
            rotulo = "Água",
            frase = "Quero água",
            pictograma = "pictograma_agua",
            corFundoHex = "#1976D2",
            corTextoHex = "#FFFFFF",
            urgencia = NivelUrgencia.NORMAL,
            arquivoVoz = "voz_agua.ogg",
            posicao = 1,
            barraFixa = false
        )

        val entity = CartaoEntity.fromDomain(cartao, personalizado = true)

        assertEquals("agua", entity.id)
        assertEquals("Água", entity.rotulo)
        assertEquals("Quero água", entity.frase)
        assertEquals("NORMAL", entity.urgencia)
        assertTrue(entity.personalizado)
    }

    @Test
    fun deveConverterEntidadeParaDominioComUrgenciaCorreta() {
        val entityUrgente = CartaoEntity(
            id = "dor",
            rotulo = "Dor",
            frase = "Estou com dor",
            pictograma = "pictograma_dor",
            corFundoHex = "#D32F2F",
            corTextoHex = "#FFFFFF",
            urgencia = "URGENTE",
            arquivoVoz = "voz_dor.ogg",
            posicao = 5,
            barraFixa = false
        )

        val domain = entityUrgente.toDomain()

        assertEquals(NivelUrgencia.URGENTE, domain.urgencia)
        assertEquals("dor", domain.id)
        assertFalse(domain.barraFixa)
    }
}
