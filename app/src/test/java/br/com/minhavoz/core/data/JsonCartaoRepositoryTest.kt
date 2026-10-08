package br.com.minhavoz.core.data

import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.File

/**
 * Testes unitarios para [JsonCartaoRepository].
 * Valida desserializacao, filtros de grade/barra e integridade do arquivo oficial cartoes.json.
 */
class JsonCartaoRepositoryTest {

    private val jsonExemplo: String = """
        [
          {
            "id": "agua",
            "rotulo": "ÁGUA",
            "frase": "Eu quero água, por favor.",
            "pictograma": "pictograma_agua",
            "corFundoHex": "#1E88E5",
            "corTextoHex": "#FFFFFF",
            "urgencia": "NORMAL",
            "arquivoVoz": "voz_agua.ogg",
            "posicao": 1,
            "barraFixa": false
          },
          {
            "id": "dor",
            "rotulo": "DOR",
            "frase": "Estou com dor. Preciso de ajuda.",
            "pictograma": "pictograma_dor",
            "corFundoHex": "#E53935",
            "corTextoHex": "#FFFFFF",
            "urgencia": "URGENTE",
            "arquivoVoz": "voz_dor.ogg",
            "posicao": 5,
            "barraFixa": false
          },
          {
            "id": "sim",
            "rotulo": "SIM",
            "frase": "Sim.",
            "pictograma": "pictograma_sim",
            "corFundoHex": "#43A047",
            "corTextoHex": "#FFFFFF",
            "urgencia": "NORMAL",
            "arquivoVoz": "voz_sim.ogg",
            "posicao": 7,
            "barraFixa": true
          }
        ]
    """.trimIndent()

    @Test
    fun deveCarregarCartoesOrdenadosPorPosicao() {
        val repository: CartaoRepository = JsonCartaoRepository(JsonDataSource { jsonExemplo })
        val cartoes: List<Cartao> = repository.obterTodos()

        assertEquals(3, cartoes.size)
        assertEquals("agua", cartoes[0].id)
        assertEquals("dor", cartoes[1].id)
        assertEquals("sim", cartoes[2].id)
    }

    @Test
    fun deveFiltrarGradeEBarraFixa() {
        val repository: CartaoRepository = JsonCartaoRepository(JsonDataSource { jsonExemplo })
        val grade: List<Cartao> = repository.obterGrade()
        val barraFixa: List<Cartao> = repository.obterBarraFixa()

        assertEquals(2, grade.size)
        assertEquals(1, barraFixa.size)
        assertEquals("sim", barraFixa.first().id)
    }

    @Test
    fun deveBuscarCartaoPorIdCorretamente() {
        val repository: CartaoRepository = JsonCartaoRepository(JsonDataSource { jsonExemplo })

        val agua: Cartao? = repository.obterPorId("agua")
        val inexistente: Cartao? = repository.obterPorId("desconhecido")
        val vazio: Cartao? = repository.obterPorId("")

        assertNotNull(agua)
        assertEquals(NivelUrgencia.NORMAL, agua?.urgencia)
        assertNull(inexistente)
        assertNull(vazio)
    }

    @Test
    fun deveFalharQuandoJsonEstiverVazio() {
        val repository: CartaoRepository = JsonCartaoRepository(JsonDataSource { "   " })
        try {
            repository.obterTodos()
            fail("Deveria lancar IllegalStateException para JSON vazio")
        } catch (exception: IllegalStateException) {
            assertTrue(exception.message?.contains("vazio") == true)
        }
    }

    @Test
    fun deveValidarArquivoOficialDeAssetsDoMvp() {
        val arquivoAssets = File("src/main/assets/cartoes.json").takeIf { it.exists() }
            ?: File("app/src/main/assets/cartoes.json")
        assertTrue("Arquivo de assets oficial deve existir", arquivoAssets.exists())

        val repository: CartaoRepository = JsonCartaoRepository(JsonDataSource { arquivoAssets.readText() })
        val todos: List<Cartao> = repository.obterTodos()
        val grade: List<Cartao> = repository.obterGrade()
        val barraFixa: List<Cartao> = repository.obterBarraFixa()

        assertEquals("O catalogo oficial deve conter exatamente 8 cartoes", 8, todos.size)
        assertEquals("A grade fixa deve conter 6 cartoes", 6, grade.size)
        assertEquals("A barra inferior deve conter 2 cartoes", 2, barraFixa.size)

        val posicoes = todos.map { it.posicao }
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8), posicoes)
    }
}
