package br.com.minhavoz.ui.cartoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.minhavoz.core.data.CartaoRepository

/**
 * Fabrica para instanciacao desacoplada de [GradeCartoesViewModel].
 */
class GradeCartoesViewModelFactory(
    private val cartaoRepository: CartaoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GradeCartoesViewModel::class.java)) {
            return GradeCartoesViewModel(cartaoRepository) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida: ${modelClass.name}")
    }
}
