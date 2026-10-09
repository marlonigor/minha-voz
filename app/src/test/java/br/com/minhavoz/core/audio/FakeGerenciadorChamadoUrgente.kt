package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.Cartao
import kotlinx.coroutines.CoroutineScope

/**
 * Implementacao fake de [GerenciadorChamadoUrgente] para testes de ViewModel e UI.
 */
class FakeGerenciadorChamadoUrgente : GerenciadorChamadoUrgente {

    val chamadasIniciar: MutableList<Cartao> = mutableListOf()
    val chamadasReiniciar: MutableList<Cartao> = mutableListOf()
    var cancelamentosContados: Int = 0
    override var emExecucao: Boolean = false

    override fun iniciarCicloUrgente(cartao: Cartao, scope: CoroutineScope) {
        chamadasIniciar.add(cartao)
        emExecucao = true
    }

    override fun reiniciarCicloUrgente(cartao: Cartao, scope: CoroutineScope) {
        chamadasReiniciar.add(cartao)
        emExecucao = true
    }

    override fun cancelarCicloUrgente() {
        cancelamentosContados++
        emExecucao = false
    }
}
