package com.personal.financez.data.currencies

import android.content.Context
import org.json.JSONArray

/**
 * Modelo de datos inmutable para representar una divisa.
 */
data class Currency(
    val code: String,
    val name: String
)

/**
 * Cargador del catálogo estático de divisas generado a partir del estándar internacional.
 */
object CurrencyLoader {

    private var cachedCurrencies: List<Currency>? = null

    /**
     * Carga y parsea la lista de monedas desde el archivo estático assets/currencies.json.
     */
    fun loadFromAssets(context: Context): List<Currency> {
        cachedCurrencies?.let { return it }

        return try {
            val jsonString = context.assets.open("currencies.json")
                .bufferedReader()
                .use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            val result = mutableListOf<Currency>()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                result.add(
                    Currency(
                        code = item.getString("code"),
                        name = item.getString("name")
                    )
                )
            }
            cachedCurrencies = result
            result
        } catch (e: Exception) {
            defaultFallbackCurrencies
        }
    }

    /** Monedas de respaldo en caso de cualquier error de lectura en assets. */
    val defaultFallbackCurrencies = listOf(
        Currency("COP", "Colombian Peso"),
        Currency("USD", "US Dollar"),
        Currency("EUR", "Euro"),
        Currency("MXN", "Mexican Peso"),
        Currency("ARS", "Argentine Peso")
    )
}
