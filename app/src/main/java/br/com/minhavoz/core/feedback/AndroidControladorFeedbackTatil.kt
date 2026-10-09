package br.com.minhavoz.core.feedback

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Implementacao nativa de [ControladorFeedbackTatil] utilizando o hardware do dispositivo (RF-07).
 *
 * Exemplo de uso:
 * ```kotlin
 * val controller = AndroidControladorFeedbackTatil(context)
 * controller.vibrarToqueNormal()
 * ```
 */
class AndroidControladorFeedbackTatil(
    private val context: Context
) : ControladorFeedbackTatil {

    override fun vibrarToqueNormal() {
        executarVibracao(duracaoMs = 40L)
    }

    override fun vibrarChamadoUrgente() {
        executarVibracao(duracaoMs = 120L)
    }

    override fun vibrarCliqueBotao() {
        executarVibracao(duracaoMs = 25L)
    }

    private fun executarVibracao(duracaoMs: Long) {
        try {
            val vibrator: Vibrator = obterVibrator() ?: return
            if (!vibrator.hasVibrator()) {
                return
            }
            val efeito = VibrationEffect.createOneShot(duracaoMs, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(efeito)
        } catch (_: Exception) {
            // Falhas de hardware ou permissoes do sistema degradam suavemente sem interrupcao
        }
    }

    private fun obterVibrator(): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }
}
