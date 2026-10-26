package com.personal.financez.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InstitutionDao {
    @Query("SELECT * FROM institutions ORDER BY name ASC")
    fun observeAll(): Flow<List<FinancialInstitutionEntity>>

    @Query("SELECT * FROM institutions WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): FinancialInstitutionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(institution: FinancialInstitutionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(institutions: List<FinancialInstitutionEntity>)

    @Update
    suspend fun update(institution: FinancialInstitutionEntity)

    @Delete
    suspend fun delete(institution: FinancialInstitutionEntity)
}

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY name ASC")
    fun observeAll(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): AccountEntity?

    @Query("SELECT * FROM accounts WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<AccountEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<AccountEntity>)

    @Update
    suspend fun update(account: AccountEntity)

    @Delete
    suspend fun delete(account: AccountEntity)
}

@Dao
interface YieldBoxDao {
    @Query("SELECT * FROM yield_boxes WHERE account_id = :accountId ORDER BY name ASC")
    fun observeByAccount(accountId: String): Flow<List<YieldBoxEntity>>

    @Query("SELECT * FROM yield_boxes ORDER BY name ASC")
    fun observeAll(): Flow<List<YieldBoxEntity>>

    @Query("SELECT * FROM yield_boxes WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): YieldBoxEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(yieldBox: YieldBoxEntity)

    @Update
    suspend fun update(yieldBox: YieldBoxEntity)

    @Delete
    suspend fun delete(yieldBox: YieldBoxEntity)
}

@Dao
interface CreditCardDao {
    @Query("SELECT * FROM credit_cards ORDER BY name ASC")
    fun observeAll(): Flow<List<CreditCardEntity>>

    @Query("SELECT * FROM credit_cards WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CreditCardEntity?

    @Query("SELECT * FROM credit_cards WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<CreditCardEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(creditCard: CreditCardEntity)

    @Update
    suspend fun update(creditCard: CreditCardEntity)

    @Delete
    suspend fun delete(creditCard: CreditCardEntity)
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun observeAll(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE type = :type ORDER BY name ASC")
    fun observeByType(type: CategoryType): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Update
    suspend fun update(category: CategoryEntity)

    @Delete
    suspend fun delete(category: CategoryEntity)
}

@Dao
interface AccountMovementDao {
    @Query("SELECT * FROM account_movements ORDER BY timestamp DESC LIMIT :limit")
    fun observeRecent(limit: Int = 50): Flow<List<AccountMovementEntity>>

    @Query("SELECT * FROM account_movements WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun observeBetween(startTime: Long, endTime: Long): Flow<List<AccountMovementEntity>>

    @Query("SELECT * FROM account_movements WHERE origin_account_id = :accountId OR destination_account_id = :accountId ORDER BY timestamp DESC")
    fun observeByAccount(accountId: String): Flow<List<AccountMovementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movement: AccountMovementEntity)

    @Delete
    suspend fun delete(movement: AccountMovementEntity)
}

@Dao
interface CreditCardMovementDao {
    @Query("SELECT * FROM credit_card_movements WHERE credit_card_id = :cardId ORDER BY timestamp DESC")
    fun observeByCard(cardId: String): Flow<List<CreditCardMovementEntity>>

    @Query("SELECT * FROM credit_card_movements WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun observeBetween(startTime: Long, endTime: Long): Flow<List<CreditCardMovementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movement: CreditCardMovementEntity)

    @Delete
    suspend fun delete(movement: CreditCardMovementEntity)
}
