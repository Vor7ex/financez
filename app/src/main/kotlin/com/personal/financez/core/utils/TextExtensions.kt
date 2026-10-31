package com.personal.financez.core.utils

import java.text.Normalizer

/**
 * Mapeo automático de código ISO de divisa a bandera emoji usando caracteres de indicadores regionales Unicode.
 */
fun getFlagForCurrency(code: String): String {
    return when (code.uppercase()) {
        "EUR" -> "🇪🇺"
        "ANG" -> "🇨🇼"
        "XCD" -> "🌴"
        "XOF", "XAF" -> "🌍"
        "BTC" -> "₿"
        "XAU" -> "🪙"
        else -> {
            if (code.length >= 2) {
                val country = code.substring(0, 2).uppercase()
                countryCodeToEmoji(country)
            } else "🌐"
        }
    }
}

/**
 * Convierte un código de país de dos letras (ISO 3166-1 alpha-2) en su emoji de bandera correspondiente.
 */
fun countryCodeToEmoji(countryCode: String): String {
    if (countryCode.length != 2) return "🌐"
    return try {
        val first = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6
        val second = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6
        String(Character.toChars(first)) + String(Character.toChars(second))
    } catch (e: Exception) {
        "🌐"
    }
}

/**
 * Elimina acentos y diacríticos de un String para facilitar búsquedas insensibles a tildes.
 */
fun String.unaccent(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return Regex("\\p{InCombiningDiacriticalMarks}+").replace(temp, "")
}
