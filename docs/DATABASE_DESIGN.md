# Diseño y Decisiones de Persistencia — FinanceZ

## 1. Decisiones de Arquitectura y Tipado
- **Motor:** SQLite gestionado a través de AndroidX Room ORM con compilador KSP.
- **Aritmética Determinista:** Montos monetarios (`balance`, `amount`, `totalDebt`) almacenados como enteros `Long` en unidades mínimas (centavos) para erradicar imprecisiones de coma flotante (`IEEE 754`).
- **Fechas y Marcas Temporales:** Almacenadas en `Long` (milisegundos UTC Epoch) indexados para ordenamiento temporal rápido.
- **Identificadores Inmutables:** Cadenas UUID v4 generadas al instanciar.

## 2. Estructura Pragmática de Código (`data/local/`)
En lugar de hiper-fragmentar en decenas de archivos individuales, el código se consolida en 3 piezas cohesivas:
1. **`Entities.kt`:** 5 enums de dominio (`AccountType`, `YieldPeriod`, `AccountMovementType`, `CreditMovementType`, `CategoryType`) y las 7 entidades relacionales (`FinancialInstitutionEntity`, `AccountEntity`, `YieldBoxEntity`, `CreditCardEntity`, `CategoryEntity`, `AccountMovementEntity`, `CreditCardMovementEntity`).
2. **`Daos.kt`:** Los 7 DAOs con consultas reactivas (`Flow`) y operaciones de escritura asíncronas (`suspend`).
3. **`FinanceZDatabase.kt`:** Convertidores de tipos (`Converters`) y la clase abstracta de base de datos Room (`FinanceZDatabase`).

## 3. Esquema de Tablas y Relaciones
- **`institutions`:** Entidades financieras emisoras (`id` PK, `name`, `logo_uri`).
- **`accounts`:** Cuentas líquidas (`id` PK, `institution_id` FK nullable, `name`, `type` [`CUENTA_BANCARIA`|`EFECTIVO`], `balance`, `currency`, `is_gmf_exempt`). Efectivo no tiene institución y es exento de GMF.
- **`yield_boxes`:** Cajitas satélite (`id` PK, `account_id` FK CASCADE, `name`, `balance`, `annual_effective_rate`, `yield_period` [`DIARIO`|`MENSUAL`]).
- **`credit_cards`:** Deuda y cupo (`id` PK, `institution_id` FK, `name`, `total_credit_limit`, `total_debt`, `cut_off_day`, `due_day`, `last_four_digits`).
- **`categories`:** Clasificador de movimientos (`id` PK, `name`, `type` [`INGRESO`|`GASTO`], `icon`).
- **`account_movements`:** Flujo de cuentas líquidas (`id` PK, `type`, `amount`, `origin_account_id` FK, `destination_account_id` FK, `yield_box_id` FK, `category_id` FK, `exchange_rate`, `timestamp`, `concept`, `note`).
- **`credit_card_movements`:** Consumos y pagos de tarjeta (`id` PK, `credit_card_id` FK, `type`, `amount`, `category_id` FK, `total_installments`, `pending_installments`, `interest_rate`, `payment_source_account_id` FK, `timestamp`, `concept`, `note`).

## 4. Integridad y Estrategia de Migración
- **Índices obligatorios:** Todas las claves foráneas y la columna `timestamp` cuentan con índices para garantizar consultas sub-milisegundo.
- **Evolución:** Modificaciones al esquema deben incrementar `version` en `FinanceZDatabase` y registrar una migración explícita sin pérdida de datos locales.