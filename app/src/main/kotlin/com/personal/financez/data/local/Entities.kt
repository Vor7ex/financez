package com.personal.financez.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

// ==========================================
// Enumeraciones de Dominio
// ==========================================

enum class AccountType {
    CUENTA_BANCARIA,
    EFECTIVO
}

enum class YieldPeriod {
    DIARIO,
    MENSUAL
}

enum class AccountMovementType {
    INGRESO,
    GASTO,
    TRANSFERENCIA
}

enum class CreditMovementType {
    CONSUMO,
    ABONO
}

enum class CategoryType {
    INGRESO,
    GASTO
}

// ==========================================
// Entidades Relacionales (Tablas SQLite)
// ==========================================

@Entity(tableName = "institutions")
data class FinancialInstitutionEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    @ColumnInfo(name = "logo_uri")
    val logoUri: String? = null
)

@Entity(
    tableName = "accounts",
    foreignKeys = [
        ForeignKey(
            entity = FinancialInstitutionEntity::class,
            parentColumns = ["id"],
            childColumns = ["institution_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["institution_id"])]
)
data class AccountEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "institution_id")
    val institutionId: String? = null,
    val name: String,
    val type: AccountType,
    val balance: Long = 0L,
    val currency: String = "COP",
    @ColumnInfo(name = "is_gmf_exempt")
    val isGmfExempt: Boolean = false,
    @ColumnInfo(name = "annual_effective_rate")
    val annualEffectiveRate: Double? = null
)

@Entity(
    tableName = "yield_boxes",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["account_id"])]
)
data class YieldBoxEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "account_id")
    val accountId: String,
    val name: String,
    val balance: Long = 0L,
    @ColumnInfo(name = "annual_effective_rate")
    val annualEffectiveRate: Double = 0.0,
    @ColumnInfo(name = "yield_period")
    val yieldPeriod: YieldPeriod = YieldPeriod.DIARIO
)

@Entity(
    tableName = "credit_cards",
    foreignKeys = [
        ForeignKey(
            entity = FinancialInstitutionEntity::class,
            parentColumns = ["id"],
            childColumns = ["institution_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["institution_id"])]
)
data class CreditCardEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "institution_id")
    val institutionId: String,
    val name: String,
    @ColumnInfo(name = "total_credit_limit")
    val totalCreditLimit: Long,
    @ColumnInfo(name = "total_debt")
    val totalDebt: Long = 0L,
    @ColumnInfo(name = "cut_off_day")
    val cutOffDay: Int,
    @ColumnInfo(name = "due_day")
    val dueDay: Int,
    @ColumnInfo(name = "last_four_digits")
    val lastFourDigits: String? = null
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: CategoryType,
    val icon: String
)

@Entity(
    tableName = "account_movements",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["origin_account_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["destination_account_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = YieldBoxEntity::class,
            parentColumns = ["id"],
            childColumns = ["yield_box_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["origin_account_id"]),
        Index(value = ["destination_account_id"]),
        Index(value = ["yield_box_id"]),
        Index(value = ["category_id"]),
        Index(value = ["timestamp"])
    ]
)
data class AccountMovementEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val type: AccountMovementType,
    val amount: Long,
    @ColumnInfo(name = "origin_account_id")
    val originAccountId: String,
    @ColumnInfo(name = "destination_account_id")
    val destinationAccountId: String? = null,
    @ColumnInfo(name = "yield_box_id")
    val yieldBoxId: String? = null,
    @ColumnInfo(name = "category_id")
    val categoryId: String? = null,
    @ColumnInfo(name = "exchange_rate")
    val exchangeRate: Double? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val concept: String,
    val note: String? = null
)

@Entity(
    tableName = "credit_card_movements",
    foreignKeys = [
        ForeignKey(
            entity = CreditCardEntity::class,
            parentColumns = ["id"],
            childColumns = ["credit_card_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["payment_source_account_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["credit_card_id"]),
        Index(value = ["category_id"]),
        Index(value = ["payment_source_account_id"]),
        Index(value = ["timestamp"])
    ]
)
data class CreditCardMovementEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "credit_card_id")
    val creditCardId: String,
    val type: CreditMovementType,
    val amount: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: String? = null,
    @ColumnInfo(name = "total_installments")
    val totalInstallments: Int = 1,
    @ColumnInfo(name = "pending_installments")
    val pendingInstallments: Int = 1,
    @ColumnInfo(name = "interest_rate")
    val interestRate: Double = 0.0,
    @ColumnInfo(name = "payment_source_account_id")
    val paymentSourceAccountId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val concept: String,
    val note: String? = null
)
