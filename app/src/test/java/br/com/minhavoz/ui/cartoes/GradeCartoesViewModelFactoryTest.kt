package br.com.minhavoz.ui.cartoes

import androidx.lifecycle.ViewModel
import br.com.minhavoz.core.data.FakeCartaoRepository
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * Testes unitarios para [GradeCartoesViewModelFactory].
 */
class GradeCartoesViewModelFactoryTest {

    private class OutraViewModel : ViewModel()

    @Test
    fun deveCriarInstanciaDeGradeCartoesViewModel() {
        val fakeRepo = FakeCartaoRepository()
        val factory = GradeCartoesViewModelFactory(fakeRepo)

        val vm: GradeCartoesViewModel = factory.create(GradeCartoesViewModel::class.java)
        assertNotNull(vm)
    }

    @Test
    fun deveLancarExcecaoParaViewModelDesconhecida() {
        val fakeRepo = FakeCartaoRepository()
        val factory = GradeCartoesViewModelFactory(fakeRepo)

        assertThrows(IllegalArgumentException::class.java) {
            factory.create(OutraViewModel::class.java)
        }
    }
}
