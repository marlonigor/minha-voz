package br.com.minhavoz.core.model

import kotlinx.serialization.Serializable

/**
 * Define o nivel de urgencia de um cartao de comunicacao.
 *
 * Cartoes de urgencia NORMAL utilizam toque simples e canal de audio padrao.
 * Cartoes de urgencia URGENTE exigem toque longo (RF-02) e canal de alarme (RF-05).
 *
 * Exemplo de uso:
 * ```kotlin
 * val urgencia = NivelUrgencia.URGENTE
 * ```
 */
@Serializable
enum class NivelUrgencia {
    NORMAL,
    URGENTE
}
