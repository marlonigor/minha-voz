package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.Cartao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Implementacao padrao de [GerenciadorChamadoUrgente] para controle ciclico de alarmes (RF-05, ADR-003).
 *
 * Exemplo de uso:
 * ```kotlin
 * val gerenciador = PadraoGerenciadorChamadoUrgente(audioPlayer, controladorVolume)
 * gerenciador.iniciarCicloUrgente(cartao, viewModelScope)
 * ```
 */
class PadraoGerenciadorChamadoUrgente(
    private val audioPlayer: CartaoAudioPlayer,
    private val controladorVolume: ControladorVolumeAlarme,
    private val intervaloRepeticaoMs: Long = 20_000L,
    private val maximoEmissoes: Int = 3,
    private val percentualVolumeMinimo: Float = 0.8f
) : GerenciadorChamadoUrgente {

    private var repeticaoJob: Job? = null

    override val emExecucao: Boolean
        get() = repeticaoJob?.isActive == true

    override fun iniciarCicloUrgente(cartao: Cartao, scope: CoroutineScope) {
        interromperExecucaoInterna()
        controladorVolume.elevarParaMinimo(percentualVolumeMinimo)
        audioPlayer.reproduzir(cartao)
        repeticaoJob = scope.launch {
            executarRepeticoesSeguintes(cartao)
        }
    }

    override fun reiniciarCicloUrgente(cartao: Cartao, scope: CoroutineScope) {
        iniciarCicloUrgente(cartao, scope)
    }

    override fun cancelarCicloUrgente() {
        interromperExecucaoInterna()
        audioPlayer.parar()
        controladorVolume.restaurarVolumeOriginal()
    }

    private fun interromperExecucaoInterna() {
        repeticaoJob?.cancel()
        repeticaoJob = null
    }

    private suspend fun executarRepeticoesSeguintes(cartao: Cartao) {
        var repeticoesRestantes: Int = maximoEmissoes - 1
        while (repeticoesRestantes > 0) {
            delay(intervaloRepeticaoMs)
            audioPlayer.reproduzir(cartao)
            repeticoesRestantes--
        }
        controladorVolume.restaurarVolumeOriginal()
        repeticaoJob = null
    }
}
