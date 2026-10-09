package br.com.minhavoz.core.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia

/**
 * Entidade de persistencia SQLite para cartoes do catalogo MinhaVoz.
 *
 * Mapeia os dados do modelo de dominio para armazenamento local
 * duravel sem dependencia de servicos remotos (RNF-01 e ADR-002).
 *
 * Exemplo de uso:
 * ```kotlin
 * val entity = cartao.toEntity()
 * val domain = entity.toDomain()
 * ```
 */
@Entity(tableName = "cartoes")
data class CartaoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "rotulo")
    val rotulo: String,

    @ColumnInfo(name = "frase")
    val frase: String,

    @ColumnInfo(name = "pictograma")
    val pictograma: String,

    @ColumnInfo(name = "cor_fundo_hex")
    val corFundoHex: String,

    @ColumnInfo(name = "cor_texto_hex")
    val corTextoHex: String,

    @ColumnInfo(name = "urgencia")
    val urgencia: String,

    @ColumnInfo(name = "arquivo_voz")
    val arquivoVoz: String,

    @ColumnInfo(name = "posicao")
    val posicao: Int,

    @ColumnInfo(name = "barra_fixa")
    val barraFixa: Boolean,

    @ColumnInfo(name = "personalizado")
    val personalizado: Boolean = false
) {

    /**
     * Converte esta entidade persistida para o modelo de dominio [Cartao].
     */
    fun toDomain(): Cartao {
        val nivelUrgencia: NivelUrgencia = if (urgencia == NivelUrgencia.URGENTE.name) {
            NivelUrgencia.URGENTE
        } else {
            NivelUrgencia.NORMAL
        }

        return Cartao(
            id = id,
            rotulo = rotulo,
            frase = frase,
            pictograma = pictograma,
            corFundoHex = corFundoHex,
            corTextoHex = corTextoHex,
            urgencia = nivelUrgencia,
            arquivoVoz = arquivoVoz,
            posicao = posicao,
            barraFixa = barraFixa
        )
    }

    companion object {
        /**
         * Cria uma instancia de [CartaoEntity] a partir do objeto de dominio [Cartao].
         */
        fun fromDomain(cartao: Cartao, personalizado: Boolean = false): CartaoEntity {
            return CartaoEntity(
                id = cartao.id,
                rotulo = cartao.rotulo,
                frase = cartao.frase,
                pictograma = cartao.pictograma,
                corFundoHex = cartao.corFundoHex,
                corTextoHex = cartao.corTextoHex,
                urgencia = cartao.urgencia.name,
                arquivoVoz = cartao.arquivoVoz,
                posicao = cartao.posicao,
                barraFixa = cartao.barraFixa,
                personalizado = personalizado
            )
        }
    }
}
