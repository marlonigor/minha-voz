package br.com.minhavoz.core.data

import br.com.minhavoz.core.model.Cartao

/**
 * Implementacao fake de [CartaoRepository] para testes unitarios previsiveis e isolados.
 */
class FakeCartaoRepository(
    private var cartoes: List<Cartao> = emptyList(),
    var deveLancarErro: Boolean = false
) : CartaoRepository {

    fun definirCartoes(novosCartoes: List<Cartao>) {
        cartoes = novosCartoes
    }

    override fun obterTodos(): List<Cartao> {
        if (deveLancarErro) throw IllegalStateException("Erro simulado no repositorio")
        return cartoes.sortedBy { it.posicao }
    }

    override fun obterGrade(): List<Cartao> {
        if (deveLancarErro) throw IllegalStateException("Erro simulado no repositorio")
        return cartoes.filter { !it.barraFixa }.sortedBy { it.posicao }
    }

    override fun obterBarraFixa(): List<Cartao> {
        if (deveLancarErro) throw IllegalStateException("Erro simulado no repositorio")
        return cartoes.filter { it.barraFixa }.sortedBy { it.posicao }
    }

    override fun obterPorId(id: String): Cartao? {
        if (deveLancarErro) throw IllegalStateException("Erro simulado no repositorio")
        return cartoes.firstOrNull { it.id == id }
    }
}
