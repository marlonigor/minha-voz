package br.com.minhavoz.core.data

import br.com.minhavoz.core.data.local.FakeCartaoDao
import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Testes unitarios para [RoomCartaoRepository].
 */
class RoomCartaoRepositoryTest {

    private lateinit var fakeDao: FakeCartaoDao
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

    @Before
    fun configurar() {
        fakeDao = FakeCartaoDao()
    }

    @Test
    fun devePopularDadosIniciaisDoJsonQuandoDaoVazio() {
        val dataSource = JsonDataSource { jsonExemplo }
        val repository = RoomCartaoRepository(fakeDao, dataSource)

        val todos = repository.obterTodos()

        assertEquals(2, todos.size)
        assertEquals("agua", todos[0].id)
        assertEquals("sim", todos[1].id)
    }

    @Test
    fun naoDeveSobrescreverDadosSeDaoJaPossuiRegistros() {
        val cartaoExistente = Cartao(
            id = "personalizado",
            rotulo = "FRIO",
            frase = "Estou com frio.",
            pictograma = "pictograma_frio",
            corFundoHex = "#000000",
            corTextoHex = "#FFFFFF",
            urgencia = NivelUrgencia.NORMAL,
            arquivoVoz = "voz_frio.ogg",
            posicao = 10,
            barraFixa = false
        )
        fakeDao.inserir(br.com.minhavoz.core.data.local.CartaoEntity.fromDomain(cartaoExistente))

        val dataSource = JsonDataSource { jsonExemplo }
        val repository = RoomCartaoRepository(fakeDao, dataSource)

        val todos = repository.obterTodos()
        assertEquals(1, todos.size)
        assertEquals("personalizado", todos[0].id)
    }

    @Test
    fun deveAtualizarFraseComSucesso() {
        val dataSource = JsonDataSource { jsonExemplo }
        val repository = RoomCartaoRepository(fakeDao, dataSource)

        val sucesso = repository.atualizarFrase("agua", "Quero água bem gelada")
        val atualizado = repository.obterPorId("agua")

        assertTrue(sucesso)
        assertNotNull(atualizado)
        assertEquals("Quero água bem gelada", atualizado?.frase)
    }

    @Test
    fun deveRetornarFalsoAoAtualizarFraseComIdInexistenteOuVazio() {
        val dataSource = JsonDataSource { jsonExemplo }
        val repository = RoomCartaoRepository(fakeDao, dataSource)

        assertFalse(repository.atualizarFrase("", "Nova frase"))
        assertFalse(repository.atualizarFrase("inexistente", "Nova frase"))
        assertFalse(repository.atualizarFrase("agua", "   "))
    }

    @Test
    fun deveSalvarERemoverCartaoPersonalizado() {
        val dataSource = JsonDataSource { jsonExemplo }
        val repository = RoomCartaoRepository(fakeDao, dataSource)

        val novoCartao = Cartao(
            id = "frio",
            rotulo = "Frio",
            frase = "Estou com frio",
            pictograma = "pictograma_frio",
            corFundoHex = "#0288D1",
            corTextoHex = "#FFFFFF",
            urgencia = NivelUrgencia.NORMAL,
            arquivoVoz = "voz_frio.ogg",
            posicao = 9,
            barraFixa = false
        )

        repository.salvar(novoCartao)
        assertNotNull(repository.obterPorId("frio"))
        assertEquals(3, repository.obterTodos().size)

        val removido = repository.remover("frio")
        assertTrue(removido)
        assertEquals(2, repository.obterTodos().size)
    }
}
