package com.personal.financez

import android.app.Application
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
 * Contenedor de dependencias de la aplicación (Inyección manual sin librerías externas).
 */
interface AppContainer {
    val preferencesRepository: UserPreferencesRepository
    val database: FinanceZDatabase
}

class DefaultAppContainer(private val context: Context) : AppContainer {

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

/**
 * Instancia de aplicación que vive durante todo el ciclo del proceso.
 */
class FinanceZApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
