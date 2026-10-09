package br.com.minhavoz.core.audio

/**
 * Contrato para ajuste e restauracao defensiva do volume de alarme do dispositivo (ADR-003 item 5).
 *
 * Exemplo de uso:
 * ```kotlin
 * val controller: ControladorVolumeAlarme = ...
 * controller.elevarParaMinimo(0.8f)
 * controller.restaurarVolumeOriginal()
 * ```
 */
interface ControladorVolumeAlarme {

    /**
     * Volume de alarme atualmente configurado no sistema.
     */
    val volumeAtual: Int

    /**
     * Volume maximo de alarme permitido pelo hardware.
     */
    val volumeMaximo: Int

    /**
     * Eleva o volume para a fracao minima informada caso o nivel atual seja inferior.
     * Preserva o volume anterior para restauracao posterior.
     */
    fun elevarParaMinimo(fracaoMinima: Float = 0.8f): Boolean

    /**
     * Restaura o volume de alarme ao valor gravado antes do chamado urgente.
     */
    fun restaurarVolumeOriginal(): Boolean
}
