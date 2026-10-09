package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.NivelUrgencia

/**
 * Implementacao fake de [SintetizadorVoz] para testes unitarios isolados.
 */
class FakeSintetizadorVoz(
    override var disponivel: Boolean = true
) : SintetizadorVoz {

    val chamadasFalar: MutableList<Pair<String, NivelUrgencia>> = mutableListOf()
    var paradas: Int = 0
    var liberacoes: Int = 0

    override fun falar(texto: String, urgencia: NivelUrgencia) {
        chamadasFalar.add(Pair(texto, urgencia))
    }

    override fun parar() {
        paradas++
    }

    override fun liberar() {
        liberacoes++
    }
}
