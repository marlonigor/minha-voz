package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.Cartao

/**
 * Implementacao fake de [CartaoAudioPlayer] para testes unitarios de UI e ViewModel.
 */
class FakeCartaoAudioPlayer : CartaoAudioPlayer {

    val chamadasReproduzir: MutableList<Cartao> = mutableListOf()
    var paradas: Int = 0
    var liberacoes: Int = 0

    override fun reproduzir(cartao: Cartao) {
        chamadasReproduzir.add(cartao)
    }

    override fun parar() {
        paradas++
    }

    override fun liberar() {
        liberacoes++
    }
}
