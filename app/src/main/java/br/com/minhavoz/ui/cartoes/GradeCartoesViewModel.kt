package br.com.minhavoz.ui.cartoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.minhavoz.core.data.CartaoRepository
import br.com.minhavoz.core.model.Cartao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Gerencia o estado e as interacoes de usuario da tela principal de cartoes.
 *
 * Implementa as regras de negocio do MVP:
 * - Carregamento separado da grade (RF-01) e barra fixa.
 * - Bloqueio temporario de repetição por 1.5s apos ativacao de cartao (RF-06).
 * - Exibicao e fechamento de cartao em tela cheia (RF-03).
 *
 * Exemplo de uso:
 * ```kotlin
 * val viewModel = GradeCartoesViewModel(repository)
 * viewModel.selecionarCartao(cartao)
 * ```
 */
class GradeCartoesViewModel(
    private val cartaoRepository: CartaoRepository,
    private val debounceMs: Long = 1500L
) : ViewModel() {

    private val _uiState: MutableStateFlow<GradeCartoesUiState> = MutableStateFlow(GradeCartoesUiState())
    val uiState: StateFlow<GradeCartoesUiState> = _uiState.asStateFlow()

    init {
        carregarCartoes()
    }

    /**
     * Carrega o catalogo de cartoes a partir do repositorio.
     */
    fun carregarCartoes() {
        try {
            val grade: List<Cartao> = cartaoRepository.obterGrade()
            val barraFixa: List<Cartao> = cartaoRepository.obterBarraFixa()
            _uiState.update { estado ->
                estado.copy(
                    cartoesGrade = grade,
                    cartoesBarraFixa = barraFixa,
                    erro = null
                )
            }
        } catch (exception: Exception) {
            _uiState.update { estado ->
                estado.copy(erro = "Falha ao carregar catálogo: ${exception.message.orEmpty()}")
            }
        }
    }

    /**
     * Processa a ativacao de um cartao, aplicando bloqueio temporario de repeticao (RF-06).
     */
    fun selecionarCartao(cartao: Cartao) {
        if (_uiState.value.bloqueadoPorRepeticao) {
            return
        }

        _uiState.update { estado ->
            estado.copy(
                cartaoEmDetalhe = cartao,
                bloqueadoPorRepeticao = true
            )
        }

        iniciarDesbloqueioTemporizado()
    }

    /**
     * Fecha a visualizacao em tela cheia do cartao selecionado.
     */
    fun fecharDetalhe() {
        _uiState.update { estado ->
            estado.copy(cartaoEmDetalhe = null)
        }
    }

    /**
     * Reinicia a apresentacao do cartao atualmente em tela cheia.
     */
    fun repetirCartaoAtual() {
        val cartaoAtual: Cartao = _uiState.value.cartaoEmDetalhe ?: return
        _uiState.update { estado ->
            estado.copy(cartaoEmDetalhe = cartaoAtual)
        }
    }

    private fun iniciarDesbloqueioTemporizado() {
        viewModelScope.launch {
            delay(debounceMs)
            _uiState.update { estado ->
                estado.copy(bloqueadoPorRepeticao = false)
            }
        }
    }
}
