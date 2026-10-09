package br.com.minhavoz.core.audio

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.MediaPlayer
import br.com.minhavoz.core.model.NivelUrgencia
import java.io.IOException

/**
 * Implementacao nativa de [VozGravadaPlayer] utilizando o MediaPlayer do Android e assets (ADR-003 item 1).
 *
 * Exemplo de uso:
 * ```kotlin
 * val player = AndroidVozGravadaPlayer(context, audioFocusController)
 * player.reproduzirArquivo("voz_agua.ogg", NivelUrgencia.NORMAL)
 * ```
 */
class AndroidVozGravadaPlayer(
    private val context: Context,
    private val audioFocusController: AudioFocusController? = null
) : VozGravadaPlayer {

    private var mediaPlayer: MediaPlayer? = null

    override fun reproduzirArquivo(nomeArquivo: String, urgencia: NivelUrgencia): Boolean {
        parar()
        val afd: AssetFileDescriptor = abrirAsset(nomeArquivo) ?: return false
        val attributes: AudioAttributes = criarAtributosAudio(urgencia)

        return try {
            iniciarMediaPlayer(afd, attributes)
            true
        } catch (exception: Exception) {
            liberar()
            false
        }
    }

    override fun parar() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
        } catch (_: IllegalStateException) {
            // MediaPlayer em estado inconsistente sera liberado a seguir
        }
        audioFocusController?.abandonarFoco()
        mediaPlayer?.reset()
    }

    override fun liberar() {
        parar()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun abrirAsset(nomeArquivo: String): AssetFileDescriptor? {
        return try {
            context.assets.openFd(nomeArquivo)
        } catch (_: IOException) {
            null
        }
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

    private fun iniciarMediaPlayer(afd: AssetFileDescriptor, attributes: AudioAttributes) {
        val player: MediaPlayer = MediaPlayer().apply {
            setAudioAttributes(attributes)
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            setOnCompletionListener {
                audioFocusController?.abandonarFoco()
            }
            setOnErrorListener { _, _, _ ->
                audioFocusController?.abandonarFoco()
                true
            }
            prepare()
        }
        audioFocusController?.solicitarFoco(attributes)
        player.start()
        mediaPlayer = player
    }
}
