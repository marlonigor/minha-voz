package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.NivelUrgencia

/**
 * Contrato para reproducao de arquivos de voz gravados em formato de audio (ADR-003 item 1).
 *
 * Exemplo de uso:
 * ```kotlin
 * val player: VozGravadaPlayer = ...
 * val tocou = player.reproduzirArquivo("voz_agua.ogg", NivelUrgencia.NORMAL)
 * ```
 */
interface VozGravadaPlayer {

    /**
     * Tenta reproduzir o arquivo gravado com os atributos de urgencia correspondentes.
     * Retorna true se a reproducao foi iniciada com sucesso, ou false caso o arquivo nao exista ou falhe.
     */
    fun reproduzirArquivo(nomeArquivo: String, urgencia: NivelUrgencia): Boolean

    /**
     * Interrompe qualquer reproducao de arquivo de audio em andamento.
     */
    fun parar()

    /**
     * Libera decodificadores e recursos do sistema operacional.
     */
    fun liberar()
}
