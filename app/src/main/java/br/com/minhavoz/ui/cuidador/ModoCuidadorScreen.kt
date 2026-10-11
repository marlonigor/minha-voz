package br.com.minhavoz.ui.cuidador

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.minhavoz.core.audio.RelatorioIntegridadeVoz
import br.com.minhavoz.core.audio.StatusRecursoCartao
import br.com.minhavoz.core.model.Cartao

/**
 * Painel protegido do Modo Cuidador (RF-08, RF-09).
 *
 * Apresenta diagnostico tecnico dos arquivos de audio/TTS, testes individuais de som,
 * parametros operacionais do sistema e creditos de licenciamento dos pictogramas ARASAAC.
 */
@Composable
fun ModoCuidadorScreen(
    relatorio: RelatorioIntegridadeVoz?,
    cartoes: List<Cartao>,
    onTestarSom: (Cartao) -> Unit,
    onFechar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121214))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CabecalhoCuidador(onFechar = onFechar)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { PainelResumoDiagnostico(relatorio = relatorio) }
            item { TituloSecao(titulo = "TESTE DE SOM E RECURSOS (RF-08)") }
            items(cartoes, key = { it.id }) { cartao ->
                val status = relatorio?.statusCartoes?.find { it.cartaoId == cartao.id }
                ItemCartaoDiagnostico(
                    cartao = cartao,
                    status = status,
                    onTestar = { onTestarSom(cartao) }
                )
            }
            item { SecaoParametrosSistema() }
            item { SecaoCreditosLicenca() }
        }
    }
}

@Composable
private fun CabecalhoCuidador(onFechar: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "MODO CUIDADOR",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = onFechar,
            modifier = Modifier.height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2A32)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "VOLTAR", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TituloSecao(titulo: String) {
    Text(
        text = titulo,
        color = Color(0xFFA0A0B0),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun PainelResumoDiagnostico(relatorio: RelatorioIntegridadeVoz?) {
    val corBorda = if (relatorio?.possuiPendenciaCritica == true) Color(0xFFFFA000) else Color(0xFF43A047)
    val statusTexto = if (relatorio?.possuiPendenciaCritica == true) {
        "ATENCAO: Existem recursos de audio pendentes (RF-09)."
    } else {
        "SISTEMA OPERACIONAL: Recursos sonoros prontos para uso."
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, corBorda, RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1E24))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = statusTexto, color = corBorda, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            val alerta = if (relatorio?.alertaUrgentePresente == true) "Presente" else "Ausente (usando TTS)"
            val tts = if (relatorio?.ttsDisponivel == true) "Ativo (pt-BR)" else "Indisponivel"
            Text(text = "Alerta urgente: $alerta", color = Color.White, fontSize = 13.sp)
            Text(text = "Sintetizador TTS: $tts", color = Color.White, fontSize = 13.sp)
        }
    }
}

@Composable
private fun ItemCartaoDiagnostico(
    cartao: Cartao,
    status: StatusRecursoCartao?,
    onTestar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E24), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = cartao.rotulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            val gravado = if (status?.arquivoGravadoPresente == true) "Voz gravada OK" else "Sem arquivo (reserva TTS)"
            Text(text = gravado, color = Color(0xFFA0A0B0), fontSize = 12.sp)
        }
        Button(
            onClick = onTestar,
            modifier = Modifier.height(44.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "OUVIR", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SecaoParametrosSistema() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E24), RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TituloSecao(titulo = "PARAMETROS OPERACIONAIS")
        ItemParametro(chave = "Toque longo urgente (RF-02)", valor = "800 ms")
        ItemParametro(chave = "Volume minimo de alarme (RF-05)", valor = "80%")
        ItemParametro(chave = "Fechamento tela cheia normal (RF-03)", valor = "60 s")
        ItemParametro(chave = "Fechamento chamado urgente (RF-03)", valor = "5 min (300 s)")
        ItemParametro(chave = "Bloqueio de repeticao (RF-06)", valor = "1,5 s")
    }
}

@Composable
private fun ItemParametro(chave: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = chave, color = Color(0xFFA0A0B0), fontSize = 13.sp)
        Text(text = valor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
private fun SecaoCreditosLicenca() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E24), RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        TituloSecao(titulo = "CREDITOS E LICENCA (SECAO 12)")
        Text(
            text = "Pictogramas: ARASAAC (arasaac.org), propriedade do Governo de Aragao, autoria de Sergio Palao.",
            color = Color.White,
            fontSize = 12.sp
        )
        Text(
            text = "Licenca: Creative Commons BY-NC-SA. Uso pessoal e sem fins comerciais.",
            color = Color(0xFFA0A0B0),
            fontSize = 12.sp
        )
    }
}
