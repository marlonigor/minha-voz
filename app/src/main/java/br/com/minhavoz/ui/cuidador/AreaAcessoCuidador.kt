package br.com.minhavoz.ui.cuidador

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import br.com.minhavoz.core.feedback.ControladorFeedbackTatil
import br.com.minhavoz.ui.feedback.LocalControladorFeedbackTatil
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Ponto de acesso discreto ao modo cuidador no canto superior esquerdo (RF-08, TC-27).
 *
 * Exige manutencao do toque contínuo por 3000 ms. Toques curtos (como 1s) sao ignorados.
 * Exibe um indicador sutil apenas se houver pendencia tecnica nos recursos de audio (RF-09).
 */
@Composable
fun AreaAcessoCuidador(
    temAvisoTecnico: Boolean,
    onAtivar: () -> Unit,
    modifier: Modifier = Modifier,
    duracaoToqueLongoMs: Long = 3000L
) {
    val feedbackTatil: ControladorFeedbackTatil = LocalControladorFeedbackTatil.current

    Box(
        modifier = modifier
            .size(48.dp)
            .pointerInput(onAtivar, duracaoToqueLongoMs) {
                coroutineScope {
                    awaitEachGesture {
                        awaitFirstDown(requireUnconsumed = false)
                        var disparou = false
                        val job = launch {
                            delay(duracaoToqueLongoMs)
                            disparou = true
                            feedbackTatil.vibrarChamadoUrgente()
                            onAtivar()
                        }
                        val up = waitForUpOrCancellation()
                        if (up == null || !disparou) {
                            job.cancel()
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (temAvisoTecnico) {
            IndicadorAvisoTecnico()
        }
    }
}

@Composable
private fun IndicadorAvisoTecnico() {
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(Color(0xFFFFA000))
    )
}
