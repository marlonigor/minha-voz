package br.com.minhavoz

import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import br.com.minhavoz.core.audio.AndroidSintetizadorVoz
import br.com.minhavoz.core.audio.AndroidVozGravadaPlayer
import br.com.minhavoz.core.audio.AudioFocusController
import br.com.minhavoz.core.audio.CartaoAudioPlayer
import br.com.minhavoz.core.audio.OrquestradorAudioCartao
import br.com.minhavoz.core.data.AssetJsonDataSource
import br.com.minhavoz.core.data.JsonCartaoRepository
import br.com.minhavoz.ui.cartoes.GradeCartoesScreen
import br.com.minhavoz.ui.cartoes.GradeCartoesViewModel
import br.com.minhavoz.ui.cartoes.GradeCartoesViewModelFactory

/**
 * Ponto de entrada principal do aplicativo MinhaVoz.
 * Configura o suporte a edge-to-edge e injeta o composable raiz com a grade de cartoes.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: GradeCartoesViewModel by viewModels {
        val dataSource = AssetJsonDataSource(applicationContext, "cartoes.json")
        val repository = JsonCartaoRepository(dataSource)
        val audioPlayer = criarAudioPlayer()
        GradeCartoesViewModelFactory(repository, audioPlayer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GradeCartoesScreen(viewModel = viewModel)
                }
            }
        }
    }

    private fun criarAudioPlayer(): CartaoAudioPlayer {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val audioFocusController = AudioFocusController(audioManager)
        val gravador = AndroidVozGravadaPlayer(applicationContext, audioFocusController)
        val sintetizador = AndroidSintetizadorVoz(applicationContext, audioFocusController)
        return OrquestradorAudioCartao(gravador, sintetizador)
    }
}
