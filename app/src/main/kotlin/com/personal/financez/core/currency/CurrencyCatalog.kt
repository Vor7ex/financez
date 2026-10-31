package com.personal.financez.core.currency

import android.content.Context
import com.personal.financez.core.utils.getFlagForCurrency
import com.personal.financez.core.utils.unaccent
import org.json.JSONArray

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
 * Catálogo y cargador diferido de divisas desde assets/currencies.json.
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
