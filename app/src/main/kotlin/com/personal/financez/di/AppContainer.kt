package com.personal.financez.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.personal.financez.data.local.FinanceZDatabase
import com.personal.financez.data.preferences.DataStoreUserPreferencesRepository
import com.personal.financez.data.preferences.UserPreferencesRepository

private const val PREFERENCES_NAME = "financez_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

/**
 * Contenedor central de infraestructura e inyección de dependencias.
 * Expone exclusivamente la persistencia y servicios base esenciales (Room Database, DataStore Preferences, Context).
 * Las features construyen sus propios repositorios localmente a partir de esta infraestructura,
 * garantizando el cumplimiento estricto del principio Abierto/Cerrado (OCP).
 */
interface AppContainer {
    val context: Context
    val database: FinanceZDatabase
    val preferencesRepository: UserPreferencesRepository
}

class DefaultAppContainer(override val context: Context) : AppContainer {

    override val preferencesRepository: UserPreferencesRepository by lazy {
        DataStoreUserPreferencesRepository(context.dataStore)
    }

    override val database: FinanceZDatabase by lazy {
        Room.databaseBuilder(
            context,
            FinanceZDatabase::class.java,
            FinanceZDatabase.DATABASE_NAME
        ).build()
    }
}
