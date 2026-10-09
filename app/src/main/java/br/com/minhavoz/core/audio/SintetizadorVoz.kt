package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.NivelUrgencia

/**
 * Contrato para sintese de fala nativa como mecanismo de reserva (ADR-003 item 2, RF-04).
 *
 * Exemplo de uso:
 * ```kotlin
 * val tts: SintetizadorVoz = ...
 * tts.falar("Eu quero água, por favor.", NivelUrgencia.NORMAL)
 * ```
 */
interface SintetizadorVoz {

    /**
     * Sintetiza o texto falado com os atributos de urgencia correspondentes.
     */
    fun falar(texto: String, urgencia: NivelUrgencia)

    /**
     * Interrompe qualquer sintese de fala ativa.
     */
    fun parar()

    /**
     * Libera o motor de sintese de voz do dispositivo.
     */
    fun liberar()

    /**
     * Indica se o motor de sintese esta pronto e com suporte a lingua portuguesa.
     */
    val disponivel: Boolean
}
