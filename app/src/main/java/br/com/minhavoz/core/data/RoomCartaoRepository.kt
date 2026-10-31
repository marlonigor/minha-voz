package br.com.minhavoz.core.data

import br.com.minhavoz.core.data.local.CartaoDao
import br.com.minhavoz.core.data.local.CartaoEntity
import br.com.minhavoz.core.model.Cartao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

/**
 * Implementacao de [CartaoRepository] baseada em banco de dados local Room/SQLite.
 *
 * Realiza o pre-povoamento inicial a partir de [JsonDataSource] (conforme ADR-002)
 * e permite a personalizacao duravel de frases e novos cartoes mantendo
 * operacao 100% offline (RNF-01).
 *
 * Exemplo de uso:
 * ```kotlin
 * val repository = RoomCartaoRepository(db.cartaoDao(), jsonDataSource)
 * val cartoes = repository.obterGrade()
 * repository.atualizarFrase("agua", "Eu quero agua mineral com gas, por favor.")
 * ```
 */
class RoomCartaoRepository(
    private val cartaoDao: CartaoDao,
    private val jsonDataSource: JsonDataSource? = null,
    private val jsonSerializer: Json = Json {
        ignoreUnknownKeys = true
        isLenient = false
    },
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CartaoRepository {

    init {
        runBlocking(ioDispatcher) {
            popularDadosIniciaisSeNecessario()
        }
    }

    override fun obterTodos(): List<Cartao> = runBlocking(ioDispatcher) {
        cartaoDao.obterTodos().map { it.toDomain() }
    }

    override fun obterGrade(): List<Cartao> = runBlocking(ioDispatcher) {
        cartaoDao.obterGrade().map { it.toDomain() }
    }

    override fun obterBarraFixa(): List<Cartao> = runBlocking(ioDispatcher) {
        cartaoDao.obterBarraFixa().map { it.toDomain() }
    }

    override fun obterPorId(id: String): Cartao? = runBlocking(ioDispatcher) {
        if (id.isBlank()) return@runBlocking null
        cartaoDao.obterPorId(id)?.toDomain()
    }

    override fun salvar(cartao: Cartao) = runBlocking(ioDispatcher) {
        val entidade = CartaoEntity.fromDomain(cartao, personalizado = true)
        cartaoDao.inserir(entidade)
    }

    override fun atualizarFrase(id: String, novaFrase: String): Boolean = runBlocking(ioDispatcher) {
        if (id.isBlank() || novaFrase.isBlank()) return@runBlocking false
        val linhasAfetadas: Int = cartaoDao.atualizarFrase(id, novaFrase.trim())
        linhasAfetadas > 0
    }

    override fun remover(id: String): Boolean = runBlocking(ioDispatcher) {
        if (id.isBlank()) return@runBlocking false
        val linhasRemovidas: Int = cartaoDao.remover(id)
        linhasRemovidas > 0
    }

    private fun popularDadosIniciaisSeNecessario() {
        if (jsonDataSource == null) {
            return
        }

        val jsonString: String = jsonDataSource.readJsonString()
        if (jsonString.isBlank()) {
            return
        }

        val cartoesPadrao: List<Cartao> = jsonSerializer.decodeFromString(jsonString)
        val idsExistentes: Set<String> = cartaoDao.obterTodos().map { it.id }.toSet()
        val cartoesAusentes: List<Cartao> = cartoesPadrao.filter { it.id !in idsExistentes }

        if (cartoesAusentes.isNotEmpty()) {
            val entidades: List<CartaoEntity> = cartoesAusentes.map {
                CartaoEntity.fromDomain(it, personalizado = false)
            }
            cartaoDao.inserirLoteInicial(entidades)
        }
    }
}
