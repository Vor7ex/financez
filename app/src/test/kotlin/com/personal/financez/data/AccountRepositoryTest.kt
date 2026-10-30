package com.personal.financez.data

import com.personal.financez.data.local.AccountDao
import com.personal.financez.data.local.AccountEntity
import com.personal.financez.data.local.FinancialInstitutionEntity
import com.personal.financez.data.local.InstitutionDao
import com.personal.financez.data.local.AccountType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AccountRepositoryTest {

    @Test
    fun load_institutions_from_assets_seeds_reference_institutions() = runTest {
        val fakeDao = FakeInstitutionDao()
        val repository = DefaultAccountRepository(
            institutionDao = fakeDao,
            accountDao = FakeAccountDao(),
            institutionJsonProvider = {
                """
                [
                    {"id": "bancolombia", "name": "Bancolombia"},
                    {"id": "nu", "name": "Nu"},
                    {"id": "davivienda", "name": "Davivienda"},
                    {"id": "nequi", "name": "Nequi"},
                    {"id": "lulo", "name": "Lulo Bank"}
                ]
                """.trimIndent()
            }
        )

        repository.seedInstitutionsIfEmpty()

        val institutions = repository.observeInstitutions().first()
        assertEquals(5, institutions.size)
        val names = institutions.map { it.name }.toSet()
        assertTrue(names.containsAll(listOf("Bancolombia", "Nu", "Davivienda", "Nequi", "Lulo Bank")))
    }

    @Test
    fun create_bank_account_persists_institution_balance_and_financial_attributes() = runTest {
        val repository = DefaultAccountRepository(
            institutionDao = FakeInstitutionDao(),
            accountDao = FakeAccountDao()
        )

        val account = AccountEntity(
            id = "acc-bank-1",
            name = "Ahorros Nu",
            type = AccountType.CUENTA_BANCARIA,
            institutionId = "nu",
            balance = 1_500_000_00L,
            currency = "COP",
            isGmfExempt = true,
            annualEffectiveRate = 13.0
        )

        repository.createAccount(account)

        val stored = repository.getAccountById("acc-bank-1")
        assertNotNull(stored)
        assertEquals("Ahorros Nu", stored?.name)
        assertEquals(AccountType.CUENTA_BANCARIA, stored?.type)
        assertEquals("nu", stored?.institutionId)
        assertEquals(1_500_000_00L, stored?.balance)
        assertEquals("COP", stored?.currency)
        assertEquals(true, stored?.isGmfExempt)
        assertEquals(13.0, stored?.annualEffectiveRate)
    }
}

class FakeInstitutionDao : InstitutionDao {
    private val data = MutableStateFlow<List<FinancialInstitutionEntity>>(emptyList())

    override fun observeAll(): Flow<List<FinancialInstitutionEntity>> = data

    override suspend fun getById(id: String): FinancialInstitutionEntity? {
        return data.value.find { it.id == id }
    }

    override suspend fun insert(institution: FinancialInstitutionEntity) {
        data.value = data.value.filterNot { it.id == institution.id } + institution
    }

    override suspend fun insertAll(institutions: List<FinancialInstitutionEntity>) {
        val map = data.value.associateBy { it.id }.toMutableMap()
        institutions.forEach { map[it.id] = it }
        data.value = map.values.toList()
    }

    override suspend fun update(institution: FinancialInstitutionEntity) {
        insert(institution)
    }

    override suspend fun delete(institution: FinancialInstitutionEntity) {
        data.value = data.value.filterNot { it.id == institution.id }
    }
}

class FakeAccountDao : AccountDao {
    private val data = MutableStateFlow<List<AccountEntity>>(emptyList())

    override fun observeAll(): Flow<List<AccountEntity>> = data

    override suspend fun getById(id: String): AccountEntity? {
        return data.value.find { it.id == id }
    }

    override fun observeById(id: String): Flow<AccountEntity?> {
        return data.map { list -> list.find { it.id == id } }
    }

    override suspend fun insert(account: AccountEntity) {
        data.value = data.value.filterNot { it.id == account.id } + account
    }

    override suspend fun insertAll(accounts: List<AccountEntity>) {
        val map = data.value.associateBy { it.id }.toMutableMap()
        accounts.forEach { map[it.id] = it }
        data.value = map.values.toList()
    }

    override suspend fun update(account: AccountEntity) {
        insert(account)
    }

    override suspend fun delete(account: AccountEntity) {
        data.value = data.value.filterNot { it.id == account.id }
    }
}
