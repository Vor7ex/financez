package com.personal.financez.ui.dashboard

import com.personal.financez.data.AccountRepository
import com.personal.financez.data.local.AccountEntity
import com.personal.financez.data.local.FinancialInstitutionEntity
import com.personal.financez.data.local.AccountType
import com.personal.financez.ui.onboarding.FakeUserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
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

    @Test
    fun dashboard_state_exposes_accounts_and_net_worth_when_accounts_exist() = runTest {
        val initialAccounts = listOf(
            AccountEntity(id = "1", name = "Bancolombia", type = AccountType.CUENTA_BANCARIA, institutionId = "bancolombia", balance = 2_000_000_00L),
            AccountEntity(id = "2", name = "Efectivo", type = AccountType.EFECTIVO, balance = 500_000_00L)
        )
        val fakeAccountRepo = FakeAccountRepository(initialAccounts = initialAccounts)
        val fakePrefsRepo = FakeUserPreferencesRepository(initialCurrency = "COP")

        val viewModel = DashboardViewModel(
            accountRepository = fakeAccountRepo,
            preferencesRepository = fakePrefsRepo
        )

        val state = viewModel.uiState.value
        assertTrue("Cuando hay cuentas debe mostrar Success", state is DashboardUiState.Success)
        val successState = state as DashboardUiState.Success
        assertEquals(2, successState.accounts.size)
        assertEquals(2_500_000_00L, successState.consolidatedNetWorth)
        assertEquals("COP", successState.baseCurrency)
    }

    @Test
    fun switching_dashboard_tab_toggles_between_net_worth_and_accounts() = runTest {
        val initialAccounts = listOf(
            AccountEntity(id = "1", name = "Bancolombia", type = AccountType.CUENTA_BANCARIA, institutionId = "bancolombia", balance = 2_000_000_00L)
        )
        val fakeAccountRepo = FakeAccountRepository(initialAccounts = initialAccounts)
        val fakePrefsRepo = FakeUserPreferencesRepository(initialCurrency = "COP")

        val viewModel = DashboardViewModel(
            accountRepository = fakeAccountRepo,
            preferencesRepository = fakePrefsRepo
        )

        val initialState = viewModel.uiState.value as DashboardUiState.Success
        assertEquals(DashboardTab.PATRIMONIO, initialState.selectedTab)

        viewModel.selectTab(DashboardTab.CUENTAS)
        val accountsTabState = viewModel.uiState.value as DashboardUiState.Success
        assertEquals(DashboardTab.CUENTAS, accountsTabState.selectedTab)

        viewModel.selectTab(DashboardTab.PATRIMONIO)
        val netWorthTabState = viewModel.uiState.value as DashboardUiState.Success
        assertEquals(DashboardTab.PATRIMONIO, netWorthTabState.selectedTab)
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
        return accountsFlow.map { accounts -> accounts.sumOf { it.balance } }
    }

    override suspend fun getAccountById(id: String): AccountEntity? {
        return accountsFlow.value.find { it.id == id }
    }

    override suspend fun createAccount(account: AccountEntity) {
        accountsFlow.value = accountsFlow.value + account
    }
}
