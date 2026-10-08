package br.com.minhavoz.core.data

import br.com.minhavoz.core.model.Cartao
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * Implementacao de [CartaoRepository] que carrega e armazena em memoria
 * a lista imutavel de cartoes a partir de uma fonte JSON.
 *
 * Exemplo de uso:
 * ```kotlin
 * val repository = JsonCartaoRepository(dataSource)
 * val cartoes = repository.obterTodos()
 * ```
 */
class JsonCartaoRepository(
    private val jsonDataSource: JsonDataSource,
    private val jsonSerializer: Json = Json {
        ignoreUnknownKeys = true
        isLenient = false
    }
) : CartaoRepository {

    private val cartoesEmMemoria: List<Cartao> by lazy { carregarEValidarCartoes() }

    override fun obterTodos(): List<Cartao> = cartoesEmMemoria

    override fun obterGrade(): List<Cartao> = cartoesEmMemoria.filter { !it.barraFixa }

    override fun obterBarraFixa(): List<Cartao> = cartoesEmMemoria.filter { it.barraFixa }

    override fun obterPorId(id: String): Cartao? {
        if (id.isBlank()) return null
        return cartoesEmMemoria.firstOrNull { it.id == id }
    }

    private fun carregarEValidarCartoes(): List<Cartao> {
        val jsonString: String = jsonDataSource.readJsonString()
        if (jsonString.isBlank()) {
            throw IllegalStateException("Conteudo JSON recebido esta vazio ou em branco. Esperava-se array JSON de cartoes.")
        }
        val lista: List<Cartao> = try {
            jsonSerializer.decodeFromString(jsonString)
        } catch (exception: SerializationException) {
            throw IllegalStateException("Falha ao desserializar JSON de cartoes. Formato invalido.", exception)
        }
        if (lista.isEmpty()) {
            throw IllegalStateException("Catalogo de cartoes vazio. O MVP requer 8 cartoes configurados.")
        }
        return lista.sortedBy { it.posicao }
    }
}
