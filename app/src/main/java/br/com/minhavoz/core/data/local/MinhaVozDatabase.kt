package br.com.minhavoz.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Banco de dados local SQLite gerenciado pelo Room para o MinhaVoz.
 *
 * Garante persistencia 100% offline (RNF-01) com isolamento estrito
 * e integridade de tipos (ADR-002).
 *
 * Exemplo de uso:
 * ```kotlin
 * val db = MinhaVozDatabase.construir(context)
 * val dao = db.cartaoDao()
 * ```
 */
@Database(
    entities = [CartaoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MinhaVozDatabase : RoomDatabase() {

    abstract fun cartaoDao(): CartaoDao

    companion object {
        const val NOME_BANCO: String = "minhavoz.db"

        /**
         * Cria uma instancia de banco de dados SQLite persistente em disco.
         */
        fun construir(context: Context, nomeBanco: String = NOME_BANCO): MinhaVozDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MinhaVozDatabase::class.java,
                nomeBanco
            ).build()
        }

        /**
         * Cria uma instancia de banco em memoria volatil, ideal para testes unitarios.
         */
        fun construirEmMemoria(context: Context): MinhaVozDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                MinhaVozDatabase::class.java
            ).allowMainThreadQueries().build()
        }
    }
}
