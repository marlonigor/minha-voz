package br.com.minhavoz.core.data

import br.com.minhavoz.core.data.local.CartaoDao
import br.com.minhavoz.core.data.local.CartaoEntity
import br.com.minhavoz.core.model.Cartao
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
    }
) : CartaoRepository {

    init {
        popularDadosIniciaisSeNecessario()
    }

    override fun obterTodos(): List<Cartao> {
        return cartaoDao.obterTodos().map { it.toDomain() }
    }

    override fun obterGrade(): List<Cartao> {
        return cartaoDao.obterGrade().map { it.toDomain() }
    }

    override fun obterBarraFixa(): List<Cartao> {
        return cartaoDao.obterBarraFixa().map { it.toDomain() }
    }

    override fun obterPorId(id: String): Cartao? {
        if (id.isBlank()) return null
        return cartaoDao.obterPorId(id)?.toDomain()
    }

    override fun salvar(cartao: Cartao) {
        val entidade = CartaoEntity.fromDomain(cartao, personalizado = true)
        cartaoDao.inserir(entidade)
    }

    override fun atualizarFrase(id: String, novaFrase: String): Boolean {
        if (id.isBlank() || novaFrase.isBlank()) return false
        val linhasAfetadas: Int = cartaoDao.atualizarFrase(id, novaFrase.trim())
        return linhasAfetadas > 0
    }

    override fun remover(id: String): Boolean {
        if (id.isBlank()) return false
        val linhasRemovidas: Int = cartaoDao.remover(id)
        return linhasRemovidas > 0
    }

    private fun popularDadosIniciaisSeNecessario() {
        if (cartaoDao.contar() > 0 || jsonDataSource == null) {
            return
        }

        val jsonString: String = jsonDataSource.readJsonString()
        if (jsonString.isBlank()) {
            return
        }

        val cartoesPadrao: List<Cartao> = jsonSerializer.decodeFromString(jsonString)
        val entidades: List<CartaoEntity> = cartoesPadrao.map {
            CartaoEntity.fromDomain(it, personalizado = false)
        }
        cartaoDao.inserirLoteInicial(entidades)
    }
}
