package br.com.minhavoz.core.audio

import android.media.AudioManager
import kotlin.math.roundToInt

/**
 * Implementacao nativa de [ControladorVolumeAlarme] que gerencia o canal STREAM_ALARM (ADR-003 item 5).
 *
 * Exemplo de uso:
 * ```kotlin
 * val controller = AndroidControladorVolumeAlarme(audioManager)
 * controller.elevarParaMinimo(0.8f)
 * ```
 */
class AndroidControladorVolumeAlarme(
    private val audioManager: AudioManager
) : ControladorVolumeAlarme {

    private var volumeOriginal: Int? = null

    override val volumeAtual: Int
        get() = try {
            audioManager.getStreamVolume(AudioManager.STREAM_ALARM)
        } catch (_: Exception) {
            0
        }

    override val volumeMaximo: Int
        get() = try {
            audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
        } catch (_: Exception) {
            1
        }

    override fun elevarParaMinimo(fracaoMinima: Float): Boolean {
        val max: Int = volumeMaximo
        val atual: Int = volumeAtual
        val alvo: Int = (max * fracaoMinima).roundToInt().coerceIn(0, max)

        if (atual >= alvo) {
            return false
        }

        volumeOriginal = atual
        return aplicarVolume(alvo)
    }

    override fun restaurarVolumeOriginal(): Boolean {
        val original: Int = volumeOriginal ?: return false
        volumeOriginal = null
        return aplicarVolume(original)
    }

    private fun aplicarVolume(novoVolume: Int): Boolean {
        return try {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, novoVolume, 0)
            true
        } catch (_: Exception) {
            false
        }
    }
}
