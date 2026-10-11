package br.com.minhavoz.core.audio

/**
 * Status individual dos recursos de voz (arquivo gravado e TTS) de um cartao (RF-04, RF-09).
 */
data class StatusRecursoCartao(
    val cartaoId: String,
    val rotulo: String,
    val arquivoVoz: String,
    val arquivoGravadoPresente: Boolean,
    val ttsDisponivel: Boolean
) {
    /**
     * Retorna true se houver ao menos um mecanismo sonoro disponivel (voz gravada ou reserva TTS).
     */
    val operacional: Boolean
        get() = arquivoGravadoPresente || ttsDisponivel
}

/**
 * Relatorio consolidado de integridade dos recursos sonoros do aplicativo (RF-09).
 */
data class RelatorioIntegridadeVoz(
    val statusCartoes: List<StatusRecursoCartao>,
    val alertaUrgentePresente: Boolean,
    val ttsDisponivel: Boolean
) {
    /**
     * Indica se algum cartao esta totalmente sem voz ou se o alarme urgente esta indisponivel.
     */
    val possuiPendenciaCritica: Boolean
        get() = !alertaUrgentePresente || statusCartoes.any { !it.operacional }
}
