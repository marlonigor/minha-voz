package br.com.minhavoz.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Objeto de acesso a dados (DAO) para a tabela de cartoes.
 *
 * Centraliza operacoes atomicas de consulta, insercao, atualizacao
 * e remocao de cartoes no SQLite local (ADR-002).
 *
 * Exemplo de uso:
 * ```kotlin
 * val lista = dao.obterGrade()
 * dao.atualizarFrase("agua", "Agua bem gelada, por favor")
 * ```
 */
@Dao
interface CartaoDao {

    /**
     * Retorna todos os cartoes ordenados pela ordem visual de posicao.
     */
    @Query("SELECT * FROM cartoes ORDER BY posicao ASC")
    fun obterTodos(): List<CartaoEntity>

    /**
     * Retorna os cartoes que ocupam a grade central da tela.
     */
    @Query("SELECT * FROM cartoes WHERE barra_fixa = 0 ORDER BY posicao ASC")
    fun obterGrade(): List<CartaoEntity>

    /**
     * Retorna os cartoes da barra fixa inferior (SIM e NAO).
     */
    @Query("SELECT * FROM cartoes WHERE barra_fixa = 1 ORDER BY posicao ASC")
    fun obterBarraFixa(): List<CartaoEntity>

    /**
     * Busca um cartao pelo identificador unico.
     */
    @Query("SELECT * FROM cartoes WHERE id = :id LIMIT 1")
    fun obterPorId(id: String): CartaoEntity?

    /**
     * Insere ou atualiza um cartao no banco.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserir(cartao: CartaoEntity)

    /**
     * Insere um lote de cartoes, ignorando duplicidades existentes.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun inserirLoteInicial(cartoes: List<CartaoEntity>)

    /**
     * Atualiza especificamente a frase vocal de um cartao existente.
     */
    @Query("UPDATE cartoes SET frase = :novaFrase WHERE id = :id")
    fun atualizarFrase(id: String, novaFrase: String): Int

    /**
     * Remove um cartao personalizado pelo id.
     */
    @Query("DELETE FROM cartoes WHERE id = :id")
    fun remover(id: String): Int

    /**
     * Retorna a contagem total de cartoes cadastrados.
     */
    @Query("SELECT COUNT(*) FROM cartoes")
    fun contar(): Int
}
