package com.personal.financez.ui.onboarding

import com.personal.financez.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initial_state_shows_currency_selection_when_no_base_currency_configured() = runTest {
        val fakeRepo = FakeUserPreferencesRepository(initialCurrency = null)
        val viewModel = OnboardingViewModel(preferencesRepository = fakeRepo)

        val state = viewModel.uiState.value
        assertTrue("El estado inicial debe mostrar la selección de moneda", state is OnboardingUiState.SelectCurrency)
    }
}

class FakeUserPreferencesRepository(initialCurrency: String? = null) : UserPreferencesRepository {
    private val currencyFlow = MutableStateFlow(initialCurrency)

    override val baseCurrency: Flow<String?> = currencyFlow

    override suspend fun setBaseCurrency(currencyCode: String) {
        currencyFlow.value = currencyCode
    }
}
