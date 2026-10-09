package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.Cartao

/**
 * Orquestra a reproducao de audio priorizando a voz gravada (.ogg)
 * e acionando o sintetizador de voz (TTS) como reserva defensiva (RF-04 e ADR-003).
 *
 * Exemplo de uso:
 * ```kotlin
 * val orquestrador = OrquestradorAudioCartao(vozGravadaPlayer, sintetizadorVoz)
 * orquestrador.reproduzir(cartao)
 * ```
 */
class OrquestradorAudioCartao(
    private val vozGravadaPlayer: VozGravadaPlayer,
    private val sintetizadorVoz: SintetizadorVoz
) : CartaoAudioPlayer {

    override fun reproduzir(cartao: Cartao) {
        val sucessoGravado: Boolean = vozGravadaPlayer.reproduzirArquivo(
            nomeArquivo = cartao.arquivoVoz,
            urgencia = cartao.urgencia
        )
        if (sucessoGravado) {
            return
        }
        sintetizadorVoz.falar(
            texto = cartao.frase,
            urgencia = cartao.urgencia
        )
    }

    override fun parar() {
        vozGravadaPlayer.parar()
        sintetizadorVoz.parar()
    }

    override fun liberar() {
        vozGravadaPlayer.liberar()
        sintetizadorVoz.liberar()
    }
}
