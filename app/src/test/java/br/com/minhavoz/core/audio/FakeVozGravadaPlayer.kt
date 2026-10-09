package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.NivelUrgencia

/**
 * Implementacao fake de [VozGravadaPlayer] para testes unitarios isolados.
 */
class FakeVozGravadaPlayer(
    var simularSucesso: Boolean = true
) : VozGravadaPlayer {

    val chamadasReproduzir: MutableList<Pair<String, NivelUrgencia>> = mutableListOf()
    var paradas: Int = 0
    var liberacoes: Int = 0

    override fun reproduzirArquivo(nomeArquivo: String, urgencia: NivelUrgencia): Boolean {
        chamadasReproduzir.add(Pair(nomeArquivo, urgencia))
        return simularSucesso
    }

    override fun parar() {
        paradas++
    }

    override fun liberar() {
        liberacoes++
    }
}
