package br.com.minhavoz.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import br.com.minhavoz.core.model.NivelUrgencia
import java.util.Locale

/**
 * Implementacao nativa de [SintetizadorVoz] utilizando TextToSpeech do Android em pt-BR (ADR-003 item 2).
 *
 * Exemplo de uso:
 * ```kotlin
 * val sintetizador = AndroidSintetizadorVoz(context, audioFocusController)
 * sintetizador.falar("Eu quero água, por favor.", NivelUrgencia.NORMAL)
 * ```
 */
class AndroidSintetizadorVoz(
    context: Context,
    private val audioFocusController: AudioFocusController? = null
) : SintetizadorVoz {

    private var tts: TextToSpeech? = null
    private var pronto: Boolean = false

    override val disponivel: Boolean
        get() = pronto && tts != null

    init {
        tts = TextToSpeech(context) { status ->
            configurarMotorTts(status)
        }
    }

    override fun falar(texto: String, urgencia: NivelUrgencia) {
        val motor: TextToSpeech = tts ?: return
        if (!pronto) {
            return
        }

        val attributes: AudioAttributes = criarAtributosAudio(urgencia)
        motor.setAudioAttributes(attributes)
        audioFocusController?.solicitarFoco(attributes)

        val params: Bundle = criarParametrosBundle(urgencia)
        val utteranceId = "minha_voz_${System.currentTimeMillis()}"
        motor.speak(texto, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    override fun parar() {
        tts?.stop()
        audioFocusController?.abandonarFoco()
    }

    override fun liberar() {
        parar()
        tts?.shutdown()
        tts = null
        pronto = false
    }

    private fun configurarMotorTts(status: Int) {
        if (status != TextToSpeech.SUCCESS) {
            pronto = false
            return
        }
        val idiomaResultado: Int = tts?.setLanguage(Locale("pt", "BR")) ?: TextToSpeech.LANG_NOT_SUPPORTED
        pronto = idiomaResultado != TextToSpeech.LANG_MISSING_DATA && idiomaResultado != TextToSpeech.LANG_NOT_SUPPORTED
        registrarUtteranceListener()
    }

    private fun registrarUtteranceListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) {
                audioFocusController?.abandonarFoco()
            }
            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                audioFocusController?.abandonarFoco()
            }
        })
    }

    private fun criarAtributosAudio(urgencia: NivelUrgencia): AudioAttributes {
        val usage: Int = if (urgencia == NivelUrgencia.URGENTE) {
            AudioAttributes.USAGE_ALARM
        } else {
            AudioAttributes.USAGE_MEDIA
        }
        return AudioAttributes.Builder()
            .setUsage(usage)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
    }

    private fun criarParametrosBundle(urgencia: NivelUrgencia): Bundle {
        val streamType: Int = if (urgencia == NivelUrgencia.URGENTE) {
            AudioManager.STREAM_ALARM
        } else {
            AudioManager.STREAM_MUSIC
        }
        return Bundle().apply {
            putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, streamType)
        }
    }
}
