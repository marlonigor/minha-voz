package br.com.minhavoz.ui.cartoes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia

/**
 * Componente visual para exibicao e interacao de um cartao de comunicacao.
 */
@Composable
fun CartaoItemView(
    cartao: Cartao,
    onAtivar: (Cartao) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic: HapticFeedback = LocalHapticFeedback.current
    val fundo: Color = parseHexColor(cartao.corFundoHex)
    val textoCor: Color = parseHexColor(cartao.corTextoHex, Color.White)

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 96.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(fundo)
            .configurarClique(cartao, haptic, onAtivar)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        CartaoConteudoTexto(cartao = cartao, corTexto = textoCor)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.configurarClique(
    cartao: Cartao,
    haptic: HapticFeedback,
    onAtivar: (Cartao) -> Unit
): Modifier {
    val isUrgente: Boolean = cartao.urgencia == NivelUrgencia.URGENTE
    return this.combinedClickable(
        onClick = {
            if (!isUrgente) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onAtivar(cartao)
            }
        },
        onLongClick = {
            if (isUrgente) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onAtivar(cartao)
            }
        }
    )
}

@Composable
private fun CartaoConteudoTexto(cartao: Cartao, corTexto: Color) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = cartao.rotulo.uppercase(),
            color = corTexto,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        if (cartao.urgencia == NivelUrgencia.URGENTE) {
            Text(
                text = "TOQUE LONGO",
                color = corTexto.copy(alpha = 0.85f),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
