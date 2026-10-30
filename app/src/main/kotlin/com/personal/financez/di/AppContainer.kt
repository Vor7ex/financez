package com.personal.financez.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.personal.financez.data.AccountRepository
import com.personal.financez.data.DefaultAccountRepository
import com.personal.financez.data.local.FinanceZDatabase
import com.personal.financez.data.preferences.DataStoreUserPreferencesRepository
import com.personal.financez.data.preferences.UserPreferencesRepository

private const val PREFERENCES_NAME = "financez_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

/**
 * Contenedor central de infraestructura e inyección de dependencias.
 * Expone la persistencia base y los repositorios núcleo para que cada
 * feature construya sus propias dependencias localmente sin acoplar este contenedor.
 */
interface AppContainer {
    val context: Context
    val database: FinanceZDatabase
    val preferencesRepository: UserPreferencesRepository
    val accountRepository: AccountRepository
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

    override val accountRepository: AccountRepository by lazy {
        DefaultAccountRepository(
            institutionDao = database.institutionDao(),
            accountDao = database.accountDao(),
            context = context
        )
    }
}
