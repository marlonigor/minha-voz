package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.Cartao

/**
 * Contrato unificado para emissao sonora associada aos cartoes de comunicacao (RF-04).
 *
 * Exemplo de uso:
 * ```kotlin
 * val player: CartaoAudioPlayer = ...
 * player.reproduzir(cartao)
 * ```
 */
interface CartaoAudioPlayer {

    /**
     * Inicia a reproducao sonora do cartao informado.
     */
    fun reproduzir(cartao: Cartao)

    /**
     * Interrompe qualquer reproducao sonora ativa.
     */
    fun parar()

    /**
     * Libera recursos alocados pelo reprodutor sonoro.
     */
    fun liberar()
}
