package br.com.minhavoz.core.data.local

/**
 * Implementacao em memoria de [CartaoDao] para testes unitarios isolados e deterministicos.
 *
 * Evita I/O real e emuladores nos testes unitarios da camada de dados.
 *
 * Exemplo de uso:
 * ```kotlin
 * val fakeDao = FakeCartaoDao()
 * val repository = RoomCartaoRepository(fakeDao, jsonDataSource)
 * ```
 */
class FakeCartaoDao : CartaoDao {

    private val tabela: MutableMap<String, CartaoEntity> = mutableMapOf()

    override fun obterTodos(): List<CartaoEntity> {
        return tabela.values.sortedBy { it.posicao }
    }

    override fun obterGrade(): List<CartaoEntity> {
        return tabela.values.filter { !it.barraFixa }.sortedBy { it.posicao }
    }

    override fun obterBarraFixa(): List<CartaoEntity> {
        return tabela.values.filter { it.barraFixa }.sortedBy { it.posicao }
    }

    override fun obterPorId(id: String): CartaoEntity? {
        return tabela[id]
    }

    override fun inserir(cartao: CartaoEntity) {
        tabela[cartao.id] = cartao
    }

    override fun inserirLoteInicial(cartoes: List<CartaoEntity>) {
        cartoes.forEach { cartao ->
            if (!tabela.containsKey(cartao.id)) {
                tabela[cartao.id] = cartao
            }
        }
    }

    override fun atualizarFrase(id: String, novaFrase: String): Int {
        val existente = tabela[id] ?: return 0
        tabela[id] = existente.copy(frase = novaFrase)
        return 1
    }

    override fun remover(id: String): Int {
        val removido = tabela.remove(id)
        return if (removido != null) 1 else 0
    }

    override fun contar(): Int {
        return tabela.size
    }
}
