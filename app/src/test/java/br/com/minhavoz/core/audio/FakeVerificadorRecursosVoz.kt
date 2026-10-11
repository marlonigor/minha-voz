package br.com.minhavoz.core.audio

import br.com.minhavoz.core.model.Cartao

/**
 * Duble de teste configuravel para [VerificadorRecursosVoz].
 */
class FakeVerificadorRecursosVoz(
    var alertaUrgentePresente: Boolean = true,
    var ttsDisponivel: Boolean = true,
    val arquivosPresentes: MutableSet<String> = mutableSetOf()
) : VerificadorRecursosVoz {

    var chamadasVerificar: Int = 0
        private set

    override fun verificar(cartoes: List<Cartao>): RelatorioIntegridadeVoz {
        chamadasVerificar++
        val statusCartoes: List<StatusRecursoCartao> = cartoes.map { cartao ->
            StatusRecursoCartao(
                cartaoId = cartao.id,
                rotulo = cartao.rotulo,
                arquivoVoz = cartao.arquivoVoz,
                arquivoGravadoPresente = arquivosPresentes.contains(cartao.arquivoVoz),
                ttsDisponivel = ttsDisponivel
            )
        }
        return RelatorioIntegridadeVoz(
            statusCartoes = statusCartoes,
            alertaUrgentePresente = alertaUrgentePresente,
            ttsDisponivel = ttsDisponivel
        )
    }
}
