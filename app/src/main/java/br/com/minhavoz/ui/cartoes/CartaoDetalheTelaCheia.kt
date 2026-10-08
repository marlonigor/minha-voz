package br.com.minhavoz.ui.cartoes

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia

/**
 * Exibe o cartao ativado em tela cheia com pictograma ampliado e botoes de acao (RF-03).
 */
@Composable
fun CartaoDetalheTelaCheia(
    cartao: Cartao,
    onRepetir: () -> Unit,
    onFechar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fundo: Color = parseHexColor(cartao.corFundoHex)
    val textoCor: Color = parseHexColor(cartao.corTextoHex, Color.White)

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
            BotoesAcao(onRepetir = onRepetir, onFechar = onFechar)
        }
    }
}

@Composable
private fun DetalheCentro(cartao: Cartao, textoCor: Color) {
    val iconeRes = resolverRecursoPictograma(cartao.pictograma)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
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
            Spacer(modifier = Modifier.height(12.dp))
        }
        Icon(
            painter = painterResource(id = iconeRes),
            contentDescription = cartao.rotulo,
            tint = textoCor,
            modifier = Modifier
                .size(108.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = cartao.rotulo.uppercase(),
            fontSize = 72.sp,
            fontWeight = FontWeight.Black,
            color = textoCor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "\"${cartao.frase}\"",
            fontSize = 28.sp,
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
            onClick = onRepetir,
            corFundo = Color.White,
            corTexto = Color.Black,
            modifier = Modifier.weight(1f)
        )
        BotaoAcao(
            texto = "FECHAR",
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
    onClick: () -> Unit,
    corFundo: Color,
    corTexto: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = corFundo, contentColor = corTexto),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.defaultMinSize(minHeight = 96.dp)
    ) {
        Text(
            text = texto,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
