package com.personal.financez.data.currencies

import android.content.Context
import org.json.JSONArray
import java.text.Normalizer

/**
 * Modelo inmutable para representar una divisa con su código, nombre y bandera emoji.
 */
data class Currency(
    val code: String,
    val name: String,
    val flagEmoji: String = getFlagForCurrency(code)
) {
    /**
     * Valida si la divisa coincide con la búsqueda (por código o nombre, ignorando acentos y mayúsculas).
     */
    fun matchesQuery(query: String): Boolean {
        if (query.isBlank()) return true
        val normalizedQuery = query.trim().unaccent().lowercase()
        val normalizedCode = code.unaccent().lowercase()
        val normalizedName = name.unaccent().lowercase()
        return normalizedCode.contains(normalizedQuery) || normalizedName.contains(normalizedQuery)
    }
}

/**
 * Catálogo y cargador de divisas.
 */
object CurrencyLoader {

    private var cachedCurrencies: List<Currency>? = null

    /**
     * Monedas más utilizadas en la región para selección rápida inicial (COP de primera).
     */
    val defaultCurrencies = listOf(
        Currency("COP", "Peso colombiano", "🇨🇴"),
        Currency("USD", "Dólar estadounidense", "🇺🇸"),
        Currency("EUR", "Euro", "🇪🇺"),
        Currency("MXN", "Peso mexicano", "🇲🇽"),
        Currency("ARS", "Peso argentino", "🇦🇷")
    )

    /**
     * Carga diferida (lazy) del catálogo completo de 178 divisas solo cuando el usuario lo solicita.
     */
    fun loadAllCurrencies(context: Context): List<Currency> {
        cachedCurrencies?.let { return it }

        return try {
            val jsonString = context.assets.open("currencies.json")
                .bufferedReader()
                .use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            val result = mutableListOf<Currency>()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val code = item.getString("code")
                val name = item.getString("name")
                val flag = item.optString("flag").ifBlank { getFlagForCurrency(code) }
                result.add(Currency(code = code, name = name, flagEmoji = flag))
            }
            cachedCurrencies = result
            result
        } catch (e: Exception) {
            defaultCurrencies
        }
    }
}

/**
 * Mapeo automático de código ISO a bandera emoji usando caracteres de indicadores regionales Unicode.
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

private fun countryCodeToEmoji(countryCode: String): String {
    if (countryCode.length != 2) return "🌐"
    return try {
        val first = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6
        val second = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6
        String(Character.toChars(first)) + String(Character.toChars(second))
    } catch (e: Exception) {
        "🌐"
    }
}

private fun String.unaccent(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return Regex("\\p{InCombiningDiacriticalMarks}+").replace(temp, "")
}
