package br.com.minhavoz.ui.cartoes

import br.com.minhavoz.R

/**
 * Resolve o identificador textual de pictograma cadastrado no catalogo
 * para o identificador de recurso vetorial correspondente.
 */
fun resolverRecursoPictograma(pictogramaId: String): Int {
    return when (pictogramaId) {
        "pictograma_agua" -> R.drawable.pictograma_agua
        "pictograma_comida" -> R.drawable.pictograma_comida
        "pictograma_remedio" -> R.drawable.pictograma_remedio
        "pictograma_banheiro" -> R.drawable.pictograma_banheiro
        "pictograma_dor" -> R.drawable.pictograma_dor
        "pictograma_ajuda" -> R.drawable.pictograma_ajuda
        "pictograma_sim" -> R.drawable.pictograma_sim
        "pictograma_nao" -> R.drawable.pictograma_nao
        else -> R.drawable.pictograma_ajuda
    }
}
