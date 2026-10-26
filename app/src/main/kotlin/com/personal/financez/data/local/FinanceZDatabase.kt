package com.personal.financez.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

// ==========================================
// Convertidores de Tipos para SQLite
// ==========================================

class Converters {
    @TypeConverter
    fun fromAccountType(value: AccountType?): String? = value?.name

    @TypeConverter
    fun toAccountType(value: String?): AccountType? = value?.let { AccountType.valueOf(it) }

    @TypeConverter
    fun fromYieldPeriod(value: YieldPeriod?): String? = value?.name

    @TypeConverter
    fun toYieldPeriod(value: String?): YieldPeriod? = value?.let { YieldPeriod.valueOf(it) }

    @TypeConverter
    fun fromAccountMovementType(value: AccountMovementType?): String? = value?.name

    @TypeConverter
    fun toAccountMovementType(value: String?): AccountMovementType? = value?.let { AccountMovementType.valueOf(it) }

    @TypeConverter
    fun fromCreditMovementType(value: CreditMovementType?): String? = value?.name

    @TypeConverter
    fun toCreditMovementType(value: String?): CreditMovementType? = value?.let { CreditMovementType.valueOf(it) }

    @TypeConverter
    fun fromCategoryType(value: CategoryType?): String? = value?.name

    @TypeConverter
    fun toCategoryType(value: String?): CategoryType? = value?.let { CategoryType.valueOf(it) }
}

// ==========================================
// Base de Datos Central Room
// ==========================================

@Database(
    entities = [
        FinancialInstitutionEntity::class,
        AccountEntity::class,
        YieldBoxEntity::class,
        CreditCardEntity::class,
        CategoryEntity::class,
        AccountMovementEntity::class,
        CreditCardMovementEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FinanceZDatabase : RoomDatabase() {
    abstract fun institutionDao(): InstitutionDao
    abstract fun accountDao(): AccountDao
    abstract fun yieldBoxDao(): YieldBoxDao
    abstract fun creditCardDao(): CreditCardDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountMovementDao(): AccountMovementDao
    abstract fun creditCardMovementDao(): CreditCardMovementDao

    companion object {
        const val DATABASE_NAME = "financez_database.db"
    }
}
