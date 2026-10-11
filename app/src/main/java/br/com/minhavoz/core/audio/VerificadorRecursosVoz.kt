package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.Cartao

/**
 * Contrato para inspecao de recursos de audio e voz no inicio do app (RF-09).
 *
 * Exemplo de uso:
 * ```kotlin
 * val verificador: VerificadorRecursosVoz = ...
 * val relatorio = verificador.verificar(cartoes)
 * if (relatorio.possuiPendenciaCritica) {
 *     // Exibir aviso tecnico no modo cuidador
 * }
 * ```
 */
interface VerificadorRecursosVoz {

    /**
     * Executa a verificacao de presenca de arquivos de voz gravada, alerta urgente e TTS.
     */
    fun verificar(cartoes: List<Cartao>): RelatorioIntegridadeVoz
}
