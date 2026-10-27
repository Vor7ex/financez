package com.personal.financez.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Contrato para acceder a las preferencias del usuario.
 */
interface UserPreferencesRepository {

    /** Emite la moneda base actual (ej. "COP"), o null si el usuario aún no ha configurado ninguna. */
    val baseCurrency: Flow<String?>

    /** Guarda la moneda base seleccionada en el almacenamiento persistente. */
    suspend fun setBaseCurrency(currencyCode: String)
}

/**
 * Implementación que guarda y lee las preferencias del disco usando Android DataStore.
 */
class DataStoreUserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    // Clave que identifica la moneda base dentro del archivo de configuración
    private val baseCurrencyKey = stringPreferencesKey("base_currency")

    // 1. Lectura en tiempo real de la moneda base
    override val baseCurrency: Flow<String?> = dataStore.data
        .catch { error ->
            // Si el archivo falla al leerse en disco, asumimos preferencias vacías en vez de cerrar la app
            if (error is IOException) {
                emit(emptyPreferences())
            } else {
                throw error
            }
        }
        .map { preferences ->
            // Retorna el valor guardado (o null si la clave aún no existe)
            preferences[baseCurrencyKey]
        }

    // 2. Escritura de la nueva moneda base
    override suspend fun setBaseCurrency(currencyCode: String) {
        dataStore.edit { preferences ->
            preferences[baseCurrencyKey] = currencyCode
        }
    }
}
