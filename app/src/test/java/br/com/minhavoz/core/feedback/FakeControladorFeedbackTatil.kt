package br.com.minhavoz.core.feedback

/**
 * Implementacao fake de [ControladorFeedbackTatil] para testes unitarios de UI.
 */
class FakeControladorFeedbackTatil : ControladorFeedbackTatil {

    var vibracoesNormais: Int = 0
    var vibracoesUrgentes: Int = 0
    var vibracoesBotoes: Int = 0

    override fun vibrarToqueNormal() {
        vibracoesNormais++
    }

    override fun vibrarChamadoUrgente() {
        vibracoesUrgentes++
    }

    override fun vibrarCliqueBotao() {
        vibracoesBotoes++
    }
}
