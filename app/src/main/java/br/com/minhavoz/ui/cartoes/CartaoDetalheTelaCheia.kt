package br.com.minhavoz.ui.cartoes

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.minhavoz.R
import br.com.minhavoz.core.feedback.ControladorFeedbackTatil
import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia
import br.com.minhavoz.ui.feedback.LocalControladorFeedbackTatil

/**
 * Exibe o cartao ativado em tela cheia com pictograma ampliado, botoes acessiveis e tela acesa (RF-03).
 */
@Composable
fun CartaoDetalheTelaCheia(
    cartao: Cartao,
    onRepetir: () -> Unit,
    onFechar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context: Context = LocalContext.current
    val feedbackTatil: ControladorFeedbackTatil = LocalControladorFeedbackTatil.current
    val fundo: Color = parseHexColor(cartao.corFundoHex)
    val textoCor: Color = parseHexColor(cartao.corTextoHex, Color.White)

    ManterTelaLigadaEfeito(context)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(fundo)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetalheCentro(cartao = cartao, textoCor = textoCor)
            BotoesAcao(
                onRepetir = {
                    feedbackTatil.vibrarCliqueBotao()
                    onRepetir()
                },
                onFechar = {
                    feedbackTatil.vibrarCliqueBotao()
                    onFechar()
                }
            )
        }
    }
}

@Composable
private fun ManterTelaLigadaEfeito(context: Context) {
    DisposableEffect(Unit) {
        val activity = context.findActivity()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
private fun DetalheCentro(cartao: Cartao, textoCor: Color) {
    val iconeRes = resolverRecursoPictograma(cartao.pictograma)
    val tamanhoRotulo = if (cartao.rotulo.length > 7) 54.sp else 68.sp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (cartao.urgencia == NivelUrgencia.URGENTE) {
            Text(
                text = "ALERTA URGENTE",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = textoCor,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Icon(
            painter = painterResource(id = iconeRes),
            contentDescription = cartao.rotulo,
            tint = textoCor,
            modifier = Modifier
                .size(112.dp)
                .padding(bottom = 12.dp)
        )
        Text(
            text = cartao.rotulo.uppercase(),
            fontSize = tamanhoRotulo,
            fontWeight = FontWeight.Black,
            color = textoCor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "\"${cartao.frase}\"",
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            color = textoCor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BotoesAcao(onRepetir: () -> Unit, onFechar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BotaoAcao(
            texto = "REPETIR",
            iconeRes = R.drawable.ic_repetir,
            onClick = onRepetir,
            corFundo = Color.White,
            corTexto = Color.Black,
            modifier = Modifier.weight(1f)
        )
        BotaoAcao(
            texto = "FECHAR",
            iconeRes = R.drawable.ic_fechar,
            onClick = onFechar,
            corFundo = Color.Black,
            corTexto = Color.White,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BotaoAcao(
    texto: String,
    iconeRes: Int,
    onClick: () -> Unit,
    corFundo: Color,
    corTexto: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = corFundo, contentColor = corTexto),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .defaultMinSize(minHeight = 96.dp)
            .border(2.dp, corTexto.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconeRes),
                contentDescription = null,
                tint = corTexto,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = texto,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
