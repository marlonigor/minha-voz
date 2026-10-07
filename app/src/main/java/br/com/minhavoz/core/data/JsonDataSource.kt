package br.com.minhavoz.core.data

/**
 * Fornecedor de conteudo textual em formato JSON.
 *
 * Isola a fonte fisica dos dados (assets, memoria, arquivo ou rede)
 * permitindo testes deterministicos sem dependencia do Android Framework.
 *
 * Exemplo de uso:
 * ```kotlin
 * val source = JsonDataSource { "[{\"id\":\"agua\"}]" }
 * val json = source.readJsonString()
 * ```
 */
fun interface JsonDataSource {
    fun readJsonString(): String
}
