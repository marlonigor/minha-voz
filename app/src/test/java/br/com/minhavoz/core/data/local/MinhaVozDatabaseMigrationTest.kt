package br.com.minhavoz.core.data.local

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Valida a criacao, integridade do esquema versionado e futuras migracoes do Room (ADR-002).
 */
@RunWith(AndroidJUnit4::class)
class MinhaVozDatabaseMigrationTest {

    private val nomeBancoTeste = "migration-test.db"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        MinhaVozDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun criarBancoVersao1DeveValidarEsquemaComSucesso() {
        val db = helper.createDatabase(nomeBancoTeste, 1)
        db.close()
    }
}
