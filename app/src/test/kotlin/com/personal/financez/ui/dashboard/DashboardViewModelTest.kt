package com.personal.financez.ui.dashboard

import com.personal.financez.data.AccountRepository
import com.personal.financez.data.local.AccountEntity
import com.personal.financez.data.local.FinancialInstitutionEntity
import com.personal.financez.ui.onboarding.FakeUserPreferencesRepository
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
class DashboardViewModelTest {

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
    fun initial_dashboard_state_shows_empty_welcome_when_no_accounts_exist() = runTest {
        val fakeAccountRepo = FakeAccountRepository(initialAccounts = emptyList())
        val fakePrefsRepo = FakeUserPreferencesRepository(initialCurrency = "COP")

        val viewModel = DashboardViewModel(
            accountRepository = fakeAccountRepo,
            preferencesRepository = fakePrefsRepo
        )

        val state = viewModel.uiState.value
        assertTrue("Con 0 cuentas debe mostrar el estado Empty", state is DashboardUiState.Empty)
        assertEquals("COP", (state as DashboardUiState.Empty).baseCurrency)
    }
}

class FakeAccountRepository(
    initialAccounts: List<AccountEntity> = emptyList()
) : AccountRepository {
    private val accountsFlow = MutableStateFlow(initialAccounts)
    private val institutionsFlow = MutableStateFlow<List<FinancialInstitutionEntity>>(emptyList())

    override fun observeInstitutions(): Flow<List<FinancialInstitutionEntity>> = institutionsFlow

    override suspend fun seedInstitutionsIfEmpty() {}

    override fun observeAccounts(): Flow<List<AccountEntity>> = accountsFlow

    override fun observeConsolidatedNetWorth(): Flow<Long> {
        return MutableStateFlow(accountsFlow.value.sumOf { it.balance })
    }

    override suspend fun getAccountById(id: String): AccountEntity? {
        return accountsFlow.value.find { it.id == id }
    }

    override suspend fun createAccount(account: AccountEntity) {
        accountsFlow.value = accountsFlow.value + account
    }
}
