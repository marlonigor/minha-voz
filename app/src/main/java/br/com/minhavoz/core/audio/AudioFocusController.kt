package br.com.minhavoz.core.audio

import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager

/**
 * Controla as requisicoes transientes de foco de audio do sistema operacional (ADR-003 item 6).
 *
 * Garante que outras fontes de midia pausem ou reduzam o volume durante a reproducao de um cartao.
 *
 * Exemplo de uso:
 * ```kotlin
 * val controller = AudioFocusController(audioManager)
 * controller.solicitarFoco(attributes)
 * controller.abandonarFoco()
 * ```
 */
class AudioFocusController(
    private val audioManager: AudioManager
) {
    private var focusRequest: AudioFocusRequest? = null

    /**
     * Solicita foco transiente de audio para pausar outras fontes durante a fala.
     */
    fun solicitarFoco(audioAttributes: AudioAttributes): Boolean {
        abandonarFoco()
        val request: AudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
            .setAudioAttributes(audioAttributes)
            .build()
        focusRequest = request
        val result: Int = audioManager.requestAudioFocus(request)
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    /**
     * Libera o foco de audio previamente concedido ao app.
     */
    fun abandonarFoco() {
        val request: AudioFocusRequest = focusRequest ?: return
        audioManager.abandonAudioFocusRequest(request)
        focusRequest = null
    }
}
