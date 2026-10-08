package br.com.minhavoz.ui.cartoes

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia

/**
 * Componente visual para exibicao e interacao de um cartao de comunicacao com pictograma e texto.
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
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()
    val isUrgente: Boolean = cartao.urgencia == NivelUrgencia.URGENTE

    val progressoAnimado: Float by animateFloatAsState(
        targetValue = if (isPressed && isUrgente) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = LinearEasing),
        label = "progressoToqueLongo"
    )

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 96.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(fundo)
            .configurarClique(cartao, haptic, interactionSource, onAtivar)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        CartaoConteudo(
            cartao = cartao,
            corTexto = textoCor,
            progresso = progressoAnimado
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.configurarClique(
    cartao: Cartao,
    haptic: HapticFeedback,
    interactionSource: MutableInteractionSource,
    onAtivar: (Cartao) -> Unit
): Modifier {
    val isUrgente: Boolean = cartao.urgencia == NivelUrgencia.URGENTE
    return this.combinedClickable(
        interactionSource = interactionSource,
        indication = null,
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
private fun CartaoConteudo(
    cartao: Cartao,
    corTexto: Color,
    progresso: Float
) {
    val tamanhoFonte = if (cartao.rotulo.length > 7) 24.sp else 30.sp
    val iconeRes = resolverRecursoPictograma(cartao.pictograma)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconeRes),
            contentDescription = cartao.rotulo,
            tint = corTexto,
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 4.dp)
        )
        Text(
            text = cartao.rotulo.uppercase(),
            color = corTexto,
            fontSize = tamanhoFonte,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        if (cartao.urgencia == NivelUrgencia.URGENTE) {
            SubtituloUrgencia(corTexto = corTexto, progresso = progresso)
        }
    }
}

@Composable
private fun SubtituloUrgencia(corTexto: Color, progresso: Float) {
    Text(
        text = "TOQUE LONGO",
        color = corTexto.copy(alpha = 0.85f),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 2.dp)
    )
    if (progresso > 0f) {
        LinearProgressIndicator(
            progress = { progresso },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(4.dp)
                .padding(top = 2.dp),
            color = corTexto,
            trackColor = corTexto.copy(alpha = 0.25f)
        )
    }
}
