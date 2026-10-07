package br.com.minhavoz.core.model

import kotlinx.serialization.Serializable

/**
 * Representa um cartao de comunicacao visual e sonora do MinhaVoz.
 *
 * Cada cartao contem rotulo textual, frase falada, pictograma associado,
 * esquema de cores com alto contraste e indicacao de urgencia.
 *
 * Exemplo de uso:
 * ```kotlin
 * val cartao = Cartao(
 *     id = "agua",
 *     rotulo = "ÁGUA",
 *     frase = "Eu quero água, por favor.",
 *     pictograma = "pictograma_agua",
 *     corFundoHex = "#1E88E5",
 *     corTextoHex = "#FFFFFF",
 *     urgencia = NivelUrgencia.NORMAL,
 *     arquivoVoz = "voz_agua.ogg",
 *     posicao = 1,
 *     barraFixa = false
 * )
 * ```
 */
@Serializable
data class Cartao(
    val id: String,
    val rotulo: String,
    val frase: String,
    val pictograma: String,
    val corFundoHex: String,
    val corTextoHex: String,
    val urgencia: NivelUrgencia,
    val arquivoVoz: String,
    val posicao: Int,
    val barraFixa: Boolean = false
)
