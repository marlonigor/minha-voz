package br.com.minhavoz.core.data

import android.content.Context
import java.io.IOException

/**
 * Implementacao de [JsonDataSource] baseada nos assets do pacote Android.
 *
 * Exemplo de uso:
 * ```kotlin
 * val source = AssetJsonDataSource(context, "cartoes.json")
 * val json = source.readJsonString()
 * ```
 */
class AssetJsonDataSource(
    private val context: Context,
    private val assetPath: String = "cartoes.json"
) : JsonDataSource {

    override fun readJsonString(): String {
        return try {
            context.assets.open(assetPath).bufferedReader().use { reader ->
                reader.readText()
            }
        } catch (exception: IOException) {
            throw IllegalStateException(
                "Falha ao ler asset em '$assetPath'. Verifique se o arquivo existe no diretorio assets.",
                exception
            )
        }
    }
}
