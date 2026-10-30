package com.personal.financez.data

import android.content.Context
import com.personal.financez.data.local.AccountDao
import com.personal.financez.data.local.AccountEntity
import com.personal.financez.data.local.AccountType
import com.personal.financez.data.local.FinancialInstitutionEntity
import com.personal.financez.data.local.InstitutionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray

interface AccountRepository {
    fun observeInstitutions(): Flow<List<FinancialInstitutionEntity>>
    suspend fun seedInstitutionsIfEmpty()
    fun observeAccounts(): Flow<List<AccountEntity>>
    fun observeConsolidatedNetWorth(): Flow<Long>
    suspend fun getAccountById(id: String): AccountEntity?
    suspend fun createAccount(account: AccountEntity)
}

class DefaultAccountRepository(
    private val institutionDao: InstitutionDao,
    private val accountDao: AccountDao,
    private val institutionJsonProvider: (() -> String)? = null,
    private val context: Context? = null
) : AccountRepository {

    override fun observeInstitutions(): Flow<List<FinancialInstitutionEntity>> {
        return institutionDao.observeAll()
    }

    override suspend fun seedInstitutionsIfEmpty() = withContext(Dispatchers.IO) {
        val existing = institutionDao.observeAll().first()
        if (existing.isNotEmpty()) return@withContext

        val jsonString = institutionJsonProvider?.invoke()
            ?: context?.assets?.open("institutions.json")?.bufferedReader()?.use { it.readText() }
            ?: return@withContext

        val jsonArray = JSONArray(jsonString)
        val institutions = mutableListOf<FinancialInstitutionEntity>()
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            val id = item.getString("id")
            val name = item.getString("name")
            val logoUri = if (item.isNull("logo_uri")) null else item.optString("logo_uri").takeIf { it.isNotBlank() }
            institutions.add(FinancialInstitutionEntity(id = id, name = name, logoUri = logoUri))
        }

        institutionDao.insertAll(institutions)
    }

    override fun observeAccounts(): Flow<List<AccountEntity>> {
        return accountDao.observeAll()
    }

    override fun observeConsolidatedNetWorth(): Flow<Long> {
        return accountDao.observeAll().map { accounts ->
            accounts.sumOf { it.balance }
        }
    }

    override suspend fun getAccountById(id: String): AccountEntity? {
        return accountDao.getById(id)
    }

    override suspend fun createAccount(account: AccountEntity) {
        if (account.type == AccountType.CUENTA_BANCARIA) {
            require(!account.institutionId.isNullOrBlank()) {
                "Una cuenta bancaria requiere una institución financiera"
            }
        }
        accountDao.insert(account)
    }
}
