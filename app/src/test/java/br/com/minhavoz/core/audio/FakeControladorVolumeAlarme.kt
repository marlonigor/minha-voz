package br.com.minhavoz.core.audio

/**
 * Implementacao fake de [ControladorVolumeAlarme] para testes unitarios previsiveis.
 */
class FakeControladorVolumeAlarme(
    override var volumeAtual: Int = 2,
    override val volumeMaximo: Int = 10,
    var permitirAjuste: Boolean = true
) : ControladorVolumeAlarme {

    var volumeOriginalArmazenado: Int? = null
    var elevacoesContadas: Int = 0
    var restauracoesContadas: Int = 0

    override fun elevarParaMinimo(fracaoMinima: Float): Boolean {
        if (!permitirAjuste) return false
        val alvo = (volumeMaximo * fracaoMinima).toInt().coerceIn(0, volumeMaximo)
        if (volumeAtual >= alvo) return false
        volumeOriginalArmazenado = volumeAtual
        volumeAtual = alvo
        elevacoesContadas++
        return true
    }

    override fun restaurarVolumeOriginal(): Boolean {
        if (!permitirAjuste) return false
        val original = volumeOriginalArmazenado ?: return false
        volumeAtual = original
        volumeOriginalArmazenado = null
        restauracoesContadas++
        return true
    }
}
