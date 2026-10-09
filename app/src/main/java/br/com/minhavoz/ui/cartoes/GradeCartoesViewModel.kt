package br.com.minhavoz.ui.cartoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.minhavoz.core.audio.CartaoAudioPlayer
import br.com.minhavoz.core.data.CartaoRepository
import br.com.minhavoz.core.model.Cartao
import br.com.minhavoz.core.model.NivelUrgencia
import kotlinx.coroutines.Job
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
 * - Bloqueio temporario de repeticao por 1.5s apos ativacao de cartao (RF-06).
 * - Exibicao e fechamento de cartao em tela cheia (RF-03).
 * - Fechamento automatico de 60s para normais e 5min para urgentes (RF-03).
 * - Emissao sonora sincronizada e interrupcao de audio ao fechar (RF-04).
 */
class GradeCartoesViewModel(
    private val cartaoRepository: CartaoRepository,
    private val audioPlayer: CartaoAudioPlayer,
    private val debounceMs: Long = 1500L,
    private val tempoFechamentoNormalMs: Long = 60_000L,
    private val limiteUrgenteMs: Long = 300_000L
) : ViewModel() {

    private val _uiState: MutableStateFlow<GradeCartoesUiState> = MutableStateFlow(GradeCartoesUiState())
    val uiState: StateFlow<GradeCartoesUiState> = _uiState.asStateFlow()
    private var autoCloseJob: Job? = null

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
     * Processa a ativacao de um cartao, aplicando bloqueio temporario e agendamento de fechamento.
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

        audioPlayer.reproduzir(cartao)
        iniciarDesbloqueioTemporizado()
        agendarFechamentoAutomatico(cartao)
    }

    /**
     * Fecha a visualizacao em tela cheia do cartao selecionado.
     */
    fun fecharDetalhe() {
        autoCloseJob?.cancel()
        audioPlayer.parar()
        _uiState.update { estado ->
            estado.copy(cartaoEmDetalhe = null)
        }
    }

    /**
     * Reinicia a apresentacao do cartao atualmente em tela cheia e seu tempo de fechamento.
     */
    fun repetirCartaoAtual() {
        val cartaoAtual: Cartao = _uiState.value.cartaoEmDetalhe ?: return
        audioPlayer.reproduzir(cartaoAtual)
        agendarFechamentoAutomatico(cartaoAtual)
        _uiState.update { estado ->
            estado.copy(cartaoEmDetalhe = cartaoAtual)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.liberar()
    }

    private fun agendarFechamentoAutomatico(cartao: Cartao) {
        autoCloseJob?.cancel()
        val timeoutMs: Long = if (cartao.urgencia == NivelUrgencia.URGENTE) {
            limiteUrgenteMs
        } else {
            tempoFechamentoNormalMs
        }

        autoCloseJob = viewModelScope.launch {
            delay(timeoutMs)
            fecharDetalhe()
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
