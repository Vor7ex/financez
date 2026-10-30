package com.personal.financez.ui.account

import com.personal.financez.data.local.AccountType
import com.personal.financez.ui.dashboard.FakeAccountRepository
import com.personal.financez.ui.onboarding.FakeUserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateAccountViewModelTest {

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
    fun submitting_bank_account_fails_validation_when_institution_is_missing() = runTest {
        val fakeRepo = FakeAccountRepository()
        val fakePrefs = FakeUserPreferencesRepository(initialCurrency = "COP")
        val viewModel = CreateAccountViewModel(
            accountRepository = fakeRepo,
            preferencesRepository = fakePrefs
        )

        viewModel.updateName("Mi Cuenta Nu")
        viewModel.updateType(AccountType.CUENTA_BANCARIA)
        viewModel.selectInstitution(null)

        val success = viewModel.submit()

        assertFalse("El envío debe fallar si falta la institución en cuenta bancaria", success)
        assertNotNull("Debe registrar un error de institución", viewModel.uiState.value.institutionError)
        assertEquals(0, fakeRepo.observeAccounts().first().size)
    }

    @Test
    fun submitting_account_fails_when_name_is_blank() = runTest {
        val fakeRepo = FakeAccountRepository()
        val fakePrefs = FakeUserPreferencesRepository(initialCurrency = "COP")
        val viewModel = CreateAccountViewModel(
            accountRepository = fakeRepo,
            preferencesRepository = fakePrefs
        )

        viewModel.updateName("   ")
        viewModel.updateType(AccountType.EFECTIVO)

        val success = viewModel.submit()

        assertFalse("El envío debe fallar si el nombre está en blanco", success)
        assertNotNull("Debe registrar error de nombre", viewModel.uiState.value.nameError)
        assertEquals(0, fakeRepo.observeAccounts().first().size)
    }

    @Test
    fun submitting_cash_account_succeeds_without_institution_and_signals_dismiss() = runTest {
        val fakeRepo = FakeAccountRepository()
        val fakePrefs = FakeUserPreferencesRepository(initialCurrency = "COP")
        val viewModel = CreateAccountViewModel(
            accountRepository = fakeRepo,
            preferencesRepository = fakePrefs
        )

        viewModel.updateName("Billetera Efectivo")
        viewModel.updateType(AccountType.EFECTIVO)
        viewModel.updateBalance("150000") // $150.000 COP

        val success = viewModel.submit()

        assertTrue("Cuenta de efectivo válida debe crearse exitosamente", success)
        val accounts = fakeRepo.observeAccounts().first()
        assertEquals(1, accounts.size)
        val created = accounts.first()
        assertEquals("Billetera Efectivo", created.name)
        assertEquals(AccountType.EFECTIVO, created.type)
        assertNull(created.institutionId)
        assertEquals(150_000_00L, created.balance)
        assertEquals("COP", created.currency) // Usa la moneda base por defecto
        assertFalse(created.isGmfExempt) // Valor por defecto en opcional
        assertNull(created.annualEffectiveRate) // Valor por defecto en opcional
    }

    @Test
    fun submitting_account_with_all_optional_fields_persists_expected_attributes() = runTest {
        val fakeRepo = FakeAccountRepository()
        val fakePrefs = FakeUserPreferencesRepository(initialCurrency = "COP")
        val viewModel = CreateAccountViewModel(
            accountRepository = fakeRepo,
            preferencesRepository = fakePrefs
        )

        viewModel.updateName("Cuenta Rentable Nu")
        viewModel.updateType(AccountType.CUENTA_BANCARIA)
        viewModel.selectInstitution("nu")
        viewModel.updateBalance("5000.50")
        // Opcionales configurados:
        viewModel.updateCurrency("USD") // Divisa alternativa
        viewModel.updateAnnualEffectiveRate("13.25%") // Rendimiento % E.A.
        viewModel.setGmfExempt(true) // Exención 4x1000 activada

        val success = viewModel.submit()

        assertTrue("Cuenta con todos los atributos opcionales debe crearse exitosamente", success)
        val accounts = fakeRepo.observeAccounts().first()
        assertEquals(1, accounts.size)
        val created = accounts.first()
        assertEquals("Cuenta Rentable Nu", created.name)
        assertEquals(AccountType.CUENTA_BANCARIA, created.type)
        assertEquals("nu", created.institutionId)
        assertEquals(500050L, created.balance) // 5000.50 en centavos
        assertEquals("USD", created.currency) // Divisa alternativa aplicada
        assertTrue(created.isGmfExempt) // 4x1000 exento
        assertEquals(13.25, created.annualEffectiveRate ?: 0.0, 0.001) // % E.A.
    }
}
