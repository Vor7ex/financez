package com.personal.financez.data.preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val baseCurrency: Flow<String?>
    suspend fun setBaseCurrency(currencyCode: String)
}
