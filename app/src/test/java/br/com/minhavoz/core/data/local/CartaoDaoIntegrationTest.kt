package br.com.minhavoz.core.data.local

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * Testes de integracao do [CartaoDao] executando queries reais sobre SQLite in-memory.
 *
 * Valida o comportamento fisico das anotacoes do Room, integridade relacional,
 * filtros de grade/barra fixa, transacoes em lote e concorrencia sob corrotinas.
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CartaoDaoIntegrationTest {

    private lateinit var database: MinhaVozDatabase
    private lateinit var dao: CartaoDao

    @Before
    fun configurar() {
        val context: Context = RuntimeEnvironment.getApplication()
        database = MinhaVozDatabase.construirEmMemoria(context)
        dao = database.cartaoDao()
    }

    @After
    fun finalizar() {
        database.close()
    }

    @Test
    fun deveInserirEConsultarCartaoPorId() {
        val cartao = criarEntidadeExemplo("agua", "AGUA", "Eu quero agua", 1, false)
        dao.inserir(cartao)

        val encontrado = dao.obterPorId("agua")
        assertNotNull(encontrado)
        assertEquals("agua", encontrado?.id)
        assertEquals("AGUA", encontrado?.rotulo)
        assertEquals("Eu quero agua", encontrado?.frase)
    }

    @Test
    fun deveFiltrarGradeEBarraFixaCorretamente() {
        dao.inserir(criarEntidadeExemplo("agua", "AGUA", "Agua", 1, false))
        dao.inserir(criarEntidadeExemplo("comida", "COMIDA", "Comida", 2, false))
        dao.inserir(criarEntidadeExemplo("sim", "SIM", "Sim", 7, true))
        dao.inserir(criarEntidadeExemplo("nao", "NAO", "Nao", 8, true))

        val grade = dao.obterGrade()
        val barraFixa = dao.obterBarraFixa()

        assertEquals(2, grade.size)
        assertEquals("agua", grade[0].id)
        assertEquals("comida", grade[1].id)

        assertEquals(2, barraFixa.size)
        assertEquals("sim", barraFixa[0].id)
        assertEquals("nao", barraFixa[1].id)
    }

    @Test
    fun deveOrdenarTodosOsCartoesPorPosicaoCrescente() {
        dao.inserir(criarEntidadeExemplo("sim", "SIM", "Sim", 7, true))
        dao.inserir(criarEntidadeExemplo("agua", "AGUA", "Agua", 1, false))
        dao.inserir(criarEntidadeExemplo("comida", "COMIDA", "Comida", 2, false))

        val todos = dao.obterTodos()

        assertEquals(3, todos.size)
        assertEquals("agua", todos[0].id)
        assertEquals("comida", todos[1].id)
        assertEquals("sim", todos[2].id)
    }

    @Test
    fun deveIgnorarDuplicadosNoLoteInicialMasPermitirNovosRegistros() {
        val original = criarEntidadeExemplo("agua", "AGUA", "Frase personalizada", 1, false)
        dao.inserir(original)

        val lotePadrao = listOf(
            criarEntidadeExemplo("agua", "AGUA", "Frase padrao do asset", 1, false),
            criarEntidadeExemplo("comida", "COMIDA", "Comida padrao", 2, false)
        )
        dao.inserirLoteInicial(lotePadrao)

        val agua = dao.obterPorId("agua")
        val comida = dao.obterPorId("comida")

        assertEquals("Frase personalizada", agua?.frase)
        assertNotNull(comida)
        assertEquals(2, dao.contar())
    }

    @Test
    fun deveAtualizarFraseDeCartaoExistente() {
        dao.inserir(criarEntidadeExemplo("agua", "AGUA", "Frase antiga", 1, false))

        val linhasAfetadas = dao.atualizarFrase("agua", "Frase atualizada pelo cuidador")
        val atualizado = dao.obterPorId("agua")

        assertEquals(1, linhasAfetadas)
        assertEquals("Frase atualizada pelo cuidador", atualizado?.frase)
    }

    @Test
    fun deveRetornarZeroAoAtualizarFraseDeIdInexistente() {
        val linhasAfetadas = dao.atualizarFrase("id_fantasma", "Nova frase")
        assertEquals(0, linhasAfetadas)
    }

    @Test
    fun deveRemoverCartaoPorId() {
        dao.inserir(criarEntidadeExemplo("temp", "TEMP", "Temporario", 99, false))
        assertEquals(1, dao.contar())

        val removidos = dao.remover("temp")
        assertEquals(1, removidos)
        assertNull(dao.obterPorId("temp"))
        assertEquals(0, dao.contar())
    }

    @Test
    fun deveSuportarOperacoesConcorrentesEmMultiplasCorrotinas() = runBlocking {
        val operacoes = (1..20).map { indice ->
            async(Dispatchers.IO) {
                val id = "cartao_$indice"
                dao.inserir(criarEntidadeExemplo(id, "ROT_$indice", "Frase $indice", indice, false))
                dao.atualizarFrase(id, "Frase mutada $indice")
                dao.obterPorId(id)
            }
        }

        val resultados = operacoes.awaitAll()
        assertEquals(20, resultados.size)
        assertTrue(resultados.all { it != null })
        assertEquals(20, dao.contar())
    }

    private fun criarEntidadeExemplo(
        id: String,
        rotulo: String,
        frase: String,
        posicao: Int,
        barraFixa: Boolean
    ): CartaoEntity {
        return CartaoEntity(
            id = id,
            rotulo = rotulo,
            frase = frase,
            pictograma = "pictograma_$id",
            corFundoHex = "#1E88E5",
            corTextoHex = "#FFFFFF",
            urgencia = "NORMAL",
            arquivoVoz = "voz_$id.ogg",
            posicao = posicao,
            barraFixa = barraFixa,
            personalizado = false
        )
    }
}
