package br.com.minhavoz.ui.cartoes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.minhavoz.core.model.Cartao

/**
 * Tela principal que orquestra a grade de comunicacao, a barra fixa e a tela cheia (RF-01, RF-03).
 */
@Composable
fun GradeCartoesScreen(
    viewModel: GradeCartoesViewModel,
    modifier: Modifier = Modifier
) {
    val uiState: GradeCartoesUiState by viewModel.uiState.collectAsState()

    if (uiState.cartaoEmDetalhe != null) {
        CartaoDetalheTelaCheia(
            cartao = uiState.cartaoEmDetalhe!!,
            onRepetir = { viewModel.repetirCartaoAtual() },
            onFechar = { viewModel.fecharDetalhe() },
            modifier = modifier
        )
        return
    }

    LayoutGradePrincipal(
        uiState = uiState,
        onSelecionarCartao = { viewModel.selecionarCartao(it) },
        modifier = modifier
    )
}

@Composable
private fun LayoutGradePrincipal(
    uiState: GradeCartoesUiState,
    onSelecionarCartao: (Cartao) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (uiState.erro != null) {
            BannerErro(mensagem = uiState.erro)
        }

        GradeMatrizCartoes(
            cartoes = uiState.cartoesGrade,
            onSelecionarCartao = onSelecionarCartao,
            modifier = Modifier.weight(1f)
        )

        BarraFixaAcoes(
            cartoes = uiState.cartoesBarraFixa,
            onSelecionarCartao = onSelecionarCartao
        )
    }
}

@Composable
private fun GradeMatrizCartoes(
    cartoes: List<Cartao>,
    onSelecionarCartao: (Cartao) -> Unit,
    modifier: Modifier = Modifier
) {
    val linhas = cartoes.chunked(2)
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        for (par in linhas) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (cartao in par) {
                    CartaoItemView(
                        cartao = cartao,
                        onAtivar = onSelecionarCartao,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun BarraFixaAcoes(
    cartoes: List<Cartao>,
    onSelecionarCartao: (Cartao) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        for (cartao in cartoes) {
            CartaoItemView(
                cartao = cartao,
                onAtivar = onSelecionarCartao,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun BannerErro(mensagem: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = mensagem,
            color = MaterialTheme.colorScheme.error
        )
    }
}
