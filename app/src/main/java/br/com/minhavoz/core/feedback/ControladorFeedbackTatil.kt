package br.com.minhavoz.core.feedback

/**
 * Contrato para fornecimento de resposta tatil / vibratoria deterministica (RF-02, RF-07).
 *
 * Exemplo de uso:
 * ```kotlin
 * val controller: ControladorFeedbackTatil = ...
 * controller.vibrarToqueNormal()
 * ```
 */
interface ControladorFeedbackTatil {

    /**
     * Emite uma vibracao curta de confirmacao para toques normais na grade.
     */
    fun vibrarToqueNormal()

    /**
     * Emite uma vibracao acentuada ao concluir o toque longo de chamado urgente.
     */
    fun vibrarChamadoUrgente()

    /**
     * Emite uma microvibracao ao clicar nos botoes da tela cheia.
     */
    fun vibrarCliqueBotao()
}
