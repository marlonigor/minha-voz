package br.com.minhavoz.core.audio

import android.content.Context
import br.com.minhavoz.core.model.Cartao
import java.io.IOException

/**
 * Implementacao nativa de [VerificadorRecursosVoz] que consulta os assets e o motor TTS (RF-09).
 *
 * Exemplo de uso:
 * ```kotlin
 * val verificador = AndroidVerificadorRecursosVoz(context, sintetizadorVoz)
 * val relatorio = verificador.verificar(cartoes)
 * ```
 */
class AndroidVerificadorRecursosVoz(
    private val context: Context,
    private val sintetizadorVoz: SintetizadorVoz
) : VerificadorRecursosVoz {

    override fun verificar(cartoes: List<Cartao>): RelatorioIntegridadeVoz {
        val ttsOk: Boolean = sintetizadorVoz.disponivel
        val alertaOk: Boolean = verificarAssetPresente(ARQUIVO_ALERTA_URGENTE)
        val listaStatus: List<StatusRecursoCartao> = cartoes.map { cartao ->
            StatusRecursoCartao(
                cartaoId = cartao.id,
                rotulo = cartao.rotulo,
                arquivoVoz = cartao.arquivoVoz,
                arquivoGravadoPresente = verificarAssetPresente(cartao.arquivoVoz),
                ttsDisponivel = ttsOk
            )
        }
        return RelatorioIntegridadeVoz(
            statusCartoes = listaStatus,
            alertaUrgentePresente = alertaOk,
            ttsDisponivel = ttsOk
        )
    }

    private fun verificarAssetPresente(nomeArquivo: String): Boolean {
        if (nomeArquivo.isBlank()) return false
        return try {
            context.assets.openFd(nomeArquivo).close()
            true
        } catch (_: IOException) {
            false
        }
    }

    companion object {
        const val ARQUIVO_ALERTA_URGENTE: String = "alerta_urgente.ogg"
    }
}
