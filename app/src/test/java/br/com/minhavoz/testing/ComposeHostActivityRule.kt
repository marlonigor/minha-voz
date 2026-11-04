package br.com.minhavoz.testing

import android.content.ComponentName
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.test.core.app.ApplicationProvider
import org.junit.rules.ExternalResource
import org.robolectric.Shadows.shadowOf

/**
 * Registra o host Compose apenas no ambiente Robolectric, inclusive na variante release.
 *
 * Exemplo: declarar esta regra com ordem 0 e createComposeRule com ordem 1.
 */
class ComposeHostActivityRule : ExternalResource() {

    override fun before(): Unit {
        val context: Context = ApplicationProvider.getApplicationContext()
        val component: ComponentName = ComponentName(context, ComponentActivity::class.java)
        // A Activity do ui-test-manifest existe apenas em debug; nao deve entrar no APK release.
        shadowOf(context.packageManager).addActivityIfNotPresent(component)
    }
}
