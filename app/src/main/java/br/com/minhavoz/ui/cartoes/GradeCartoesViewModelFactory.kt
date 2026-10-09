package br.com.minhavoz.ui.cartoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.minhavoz.core.audio.CartaoAudioPlayer
import br.com.minhavoz.core.audio.GerenciadorChamadoUrgente
import br.com.minhavoz.core.data.CartaoRepository

/**
 * Fabrica para instanciacao desacoplada de [GradeCartoesViewModel].
 */
class GradeCartoesViewModelFactory(
    private val cartaoRepository: CartaoRepository,
    private val audioPlayer: CartaoAudioPlayer,
    private val gerenciadorChamadoUrgente: GerenciadorChamadoUrgente
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GradeCartoesViewModel::class.java)) {
            return GradeCartoesViewModel(cartaoRepository, audioPlayer, gerenciadorChamadoUrgente) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida: ${modelClass.name}")
    }
}
