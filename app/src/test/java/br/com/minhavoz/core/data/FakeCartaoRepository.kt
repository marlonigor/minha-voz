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

    override fun atualizarFrase(id: String, novaFrase: String): Boolean {
        if (deveLancarErro) throw IllegalStateException("Erro simulado no repositorio")
        if (id.isBlank() || novaFrase.isBlank()) return false
        val index = cartoes.indexOfFirst { it.id == id }
        if (index == -1) return false
        val cartaoAtualizado = cartoes[index].copy(frase = novaFrase.trim())
        val listaAtualizada = cartoes.toMutableList()
        listaAtualizada[index] = cartaoAtualizado
        cartoes = listaAtualizada
        return true
    }
}
