package br.com.minhavoz.ui.cartoes

import br.com.minhavoz.R
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Testes unitarios para [resolverRecursoPictograma].
 */
class PictogramaResolverTest {

    @Test
    fun deveResolverTodosOsPictogramasOficiais() {
        assertEquals(R.drawable.pictograma_agua, resolverRecursoPictograma("pictograma_agua"))
        assertEquals(R.drawable.pictograma_comida, resolverRecursoPictograma("pictograma_comida"))
        assertEquals(R.drawable.pictograma_remedio, resolverRecursoPictograma("pictograma_remedio"))
        assertEquals(R.drawable.pictograma_banheiro, resolverRecursoPictograma("pictograma_banheiro"))
        assertEquals(R.drawable.pictograma_dor, resolverRecursoPictograma("pictograma_dor"))
        assertEquals(R.drawable.pictograma_ajuda, resolverRecursoPictograma("pictograma_ajuda"))
        assertEquals(R.drawable.pictograma_sim, resolverRecursoPictograma("pictograma_sim"))
        assertEquals(R.drawable.pictograma_nao, resolverRecursoPictograma("pictograma_nao"))
    }

    @Test
    fun deveRetornarIconePadraoParaIdentificadorDesconhecido() {
        assertEquals(R.drawable.pictograma_ajuda, resolverRecursoPictograma("desconhecido"))
        assertEquals(R.drawable.pictograma_ajuda, resolverRecursoPictograma(""))
    }
}
