package br.com.minhavoz.ui.feedback

import androidx.compose.runtime.staticCompositionLocalOf
import br.com.minhavoz.core.feedback.ControladorFeedbackTatil

/**
 * Provedor CompositionLocal desacoplado para acesso a resposta tatil (RF-07).
 */
val LocalControladorFeedbackTatil = staticCompositionLocalOf<ControladorFeedbackTatil> {
    object : ControladorFeedbackTatil {
        override fun vibrarToqueNormal() {}
        override fun vibrarChamadoUrgente() {}
        override fun vibrarCliqueBotao() {}
    }
}
