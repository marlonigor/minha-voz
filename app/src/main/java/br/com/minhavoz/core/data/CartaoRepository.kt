package br.com.minhavoz.core.data

import br.com.minhavoz.core.model.Cartao

/**
 * Contrato de acesso ao catalogo de cartoes de comunicacao.
 *
 * Isola as camadas superiores (ViewModels, UI) da fonte fisica de dados
 * (JSON em assets no MVP, Room/SQLite na V2 conforme ADR-002).
 *
 * Exemplo de uso:
 * ```kotlin
 * val cartoesGrade = repository.obterGrade()
 * val cartaoAgua = repository.obterPorId("agua")
 * ```
 */
interface CartaoRepository {

    /**
     * Retorna todos os cartoes cadastrados no catalogo ordenados por posicao.
     */
    fun obterTodos(): List<Cartao>

    /**
     * Retorna apenas os cartoes pertencentes a grade principal (sem barra fixa).
     */
    fun obterGrade(): List<Cartao>

    /**
     * Retorna apenas os cartoes pertencentes a barra fixa inferior (SIM e NAO).
     */
    fun obterBarraFixa(): List<Cartao>

    /**
     * Busca um cartao pelo identificador unico.
     */
    fun obterPorId(id: String): Cartao?
}
