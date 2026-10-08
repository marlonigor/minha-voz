package br.com.minhavoz.ui.cartoes

import androidx.compose.ui.graphics.Color

/**
 * Converte string hexadecimal (#RRGGBB ou #AARRGGBB) para [Color].
 *
 * Em caso de formato invalido, retorna a cor de fallback especificada.
 */
fun parseHexColor(hex: String, fallback: Color = Color.LightGray): Color {
    val cleanHex: String = hex.trim().removePrefix("#")
    if (cleanHex.length != 6 && cleanHex.length != 8) {
        return fallback
    }

    return try {
        val parsed: Long = cleanHex.toLong(16)
        if (cleanHex.length == 6) {
            Color(parsed or 0xFF000000)
        } else {
            Color(parsed)
        }
    } catch (_: NumberFormatException) {
        fallback
    }
}
