package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.Cartao
import kotlinx.coroutines.CoroutineScope

/**
 * Contrato para orquestracao do ciclo de repeticao sonora e elevacao de volume de cartoes urgentes (RF-05).
 *
 * Exemplo de uso:
 * ```kotlin
 * gerenciador.iniciarCicloUrgente(cartao, scope)
 * gerenciador.cancelarCicloUrgente()
 * ```
 */
interface GerenciadorChamadoUrgente {

    /**
     * Inicia o ciclo de repeticoes do chamado urgente com elevacao de volume.
     */
    fun iniciarCicloUrgente(cartao: Cartao, scope: CoroutineScope)

    /**
     * Reinicia o contador e o ciclo de repeticoes para o cartao urgente informado.
     */
    fun reiniciarCicloUrgente(cartao: Cartao, scope: CoroutineScope)

    /**
     * Cancela qualquer ciclo em andamento e restaura o volume original do dispositivo.
     */
    fun cancelarCicloUrgente()

    /**
     * Indica se ha um chamado urgente em execucao ativa no momento.
     */
    val emExecucao: Boolean
}
