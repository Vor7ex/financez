# AGENTS.md — Guía Operativa y Guardrails para Agentes de IA

Este documento define las reglas estrictas de arquitectura, dominio financiero y diseño UI/UX para cualquier agente de IA (Claude Code, Cursor, Windsurf, Codex) que modifique este repositorio. **El incumplimiento de cualquiera de las reglas de la sección 1 se considera un fallo crítico.**

---

## 1. Invariantes Críticas (PROHIBICIONES ABSOLUTAS)

1. **PROHIBIDO usar `Double` o `Float` para montos de dinero:**
   - En la base de datos (SQLDelight) y modelos de estado, todo monto monetario DEBE representarse en unidades menores (`Long` en centavos) envuelto en la value class `MinorUnits(val cents: Long)`.
   - En cálculos intermedios de tasas (E.A., Retefuente, GMF 4x1000, TRM), usa exclusivamente `java.math.BigDecimal` con `MathContext.DECIMAL128` y redondeo bancario explícito `RoundingMode.HALF_UP`.

2. **PROHIBIDO mezclar monedas sin conversión explícita:**
   - Nunca sumes saldos de cuentas en `COP` con cuentas en `USD`. Las consultas SQL de balance y flujo de caja SIEMPRE deben agrupar por divisa (`GROUP BY currency`).

3. **PROHIBIDO mutar saldos fuera de una transacción atómica de SQLDelight:**
   - Todo movimiento que afecte una o más cuentas (ej. transferencia + cobro de 4x1000, o pago de cuota de TC desde cuenta líquida) DEBE ejecutarse dentro de un bloque `database.transaction { ... }` de SQLDelight.

4. **PROHIBIDO contabilizar compras con Tarjeta de Crédito como "Gasto del Mes" al comprar:**
   - Una compra con TC registra un evento `CREDIT_ACQUISITION` y crea un `installment_plan`.
   - El gasto real (`EXPENSE` o `INSTALLMENT_PAYMENT`) solo impacta el flujo de caja mensual cuando se registra el pago de la cuota desde una cuenta `LIQUID`.

5. **PROHIBIDO el "Boilerplate de Clean Architecture":**
   - NO crees interfaces para repositorios o motores que tienen una sola implementación (`FooRepository` + `FooRepositoryImpl` está prohibido).
   - NO crees `UseCases` que solo llamen a una función del repositorio.
   - Conecta los `Flow` de SQLDelight directamente en los `ViewModel` a través de `FinancezRepository`.

---

## 2. Reglas de los Motores Financieros (`core/engine/`)

Toda lógica matemática debe residir en clases/objetos Kotlin **100% puros** (sin imports de `android.*`), permitiendo pruebas instantáneas en JVM:

### 2.1 `GmfEngine` (4x1000)

- Si `account.isExemptFromGmf == false` y la operación es un egreso (`EXPENSE`, `TRANSFER` saliente o `CREDIT_CARD_PAYMENT`):
  - Calcula $\text{GMF} = \text{Monto} \times 0.004$.
  - Registra el movimiento principal y, en la misma transacción SQLite, inserta una transacción automática tipo `GMF_FEE` asociada a esa cuenta.

### 2.2 `YieldEngine` (Cajitas E.A.)

- Recibe: `principal: MinorUnits`, `effectiveAnnualRate: BigDecimal`, `withholdingTaxRate: BigDecimal`, `frequency: YieldFrequency`.
- Calcula el interés bruto diario o mensual exacto usando aritmética de punto flotante de alta precisión convertida a `BigDecimal`:
  - Diario: $(1 + \text{EA})^{1/365} - 1$
  - Mensual: $(1 + \text{EA})^{1/12} - 1$
- Calcula retención: $\text{Retención} = \text{Interés Bruto} \times \text{withholdingTaxRate}$.
- Abono neto: $\text{Interés Neto} = \text{Interés Bruto} - \text{Retención}$.
- Si el usuario ejecuta una **Reconciliación Manual** hacia un `targetBalance`, genera una transacción tipo `BALANCE_ADJUSTMENT` por la diferencia exacta (`targetBalance - currentBalance`).

### 2.3 `CreditCardEngine` (Diferidos sin Interés y Cortes)

- Al comprar a $N$ cuotas: divide el monto total en $N$ partes iguales en centavos (`MinorUnits`). Si hay residuo de centavos por división inexacta, asígnalo a la primera cuota (`cuota_1 = base + residuo`).
- Al pagar una cuota: disminuye el saldo de la cuenta bancaria pagadora, reduce la deuda de la TC, incrementa `paid_installments` en `installment_plans` e inserta el gasto con el `category_id` original de la compra para que aparezca en las métricas del mes en curso.

---

## 3. Estándares de UI/UX (Estrictamente Anti "AI Slop")

La interfaz debe lucir como una herramienta financiera de precisión (inspirada en *Linear*, *Wise* y *Robinhood*), nunca como una plantilla genérica generada por IA.

### 3.1 Prohibiciones Visuales

- **Cero gradientes decorativos**, sombras difusas (`shadowElevation > 0.dp` prohibido en tarjetas) o ilustraciones innecesarias.
- **Cero gráficos de pastel (Pie Charts) gigantes:** Prioriza barras de progreso horizontales minimalistas, tablas densas bien alineadas y jerarquía tipográfica.
- **Prohibido usar el teclado del sistema (`BasicTextField` con `KeyboardType.Number`) para ingresar montos principales:** Para registrar transacciones se debe usar un componente `FinancialNumericKeypad` propio integrado en el `ModalBottomSheet`, con formato de miles en vivo.

### 3.2 Sistema de Diseño (Dark Mode First)

- **Paleta Base:**
  - `BackgroundPrimary`: `#0B0E14` (Carbón profundo, no negro `#000000` puro).
  - `SurfaceCard`: `#131722` con borde de `1.dp` en color `#222735`.
  - `TextPrimary`: `#F1F5F9` | `TextSecondary`: `#94A3B8` | `TextMuted`: `#64748B`.
  - `PositiveEmerald` (Ingresos / Rendimientos): `#10B981`.
  - `NegativeCoral` (Gastos / GMF / Deuda): `#F43F5E`.
  - `AccentIndigo` (Transferencias / Inversiones): `#6366F1`.
- **Regla Tipográfica Obligatoria para Dinero:**
  - Todo `Text` que renderice cifras monetarias, porcentajes o fechas DEBE incluir `fontFeatureSettings = "tnum"` (Tabular Figures) en su `TextStyle` para garantizar alineación vertical perfecta en listas.

---

## 4. Patrón de Estado en Pantallas (MVI Simple)

Cada pantalla dentro de `feature/<nombre>/` debe contener únicamente dos archivos:

1. `<Feature>ViewModel.kt`: Expone un único `val uiState: StateFlow<UiState>` construido con `combine(...)` sobre los `Flow` de SQLDelight, y una función `fun onIntent(intent: UiIntent)` (o métodos públicos directos sin ceremonias).
2. `<Feature>Screen.kt`: Contiene el Composable con estado (conectado a Koin con `koinViewModel()`) y el Composable *stateless* privado para `@Preview`.

---

## 5. Flujo de Trabajo del Agente (Loop de Ejecución)

Cada vez que el usuario pida implementar o modificar una funcionalidad, sigue este orden estricto:

1. **Esquema primero:** Si el cambio toca datos, edita primero los archivos `.sq` en `src/main/sqldelight/` y corre `./gradlew generateDebugDatabaseInterface` para validar el SQL.
2. **Motor y Tests antes que UI:** Si involucra cálculos (cuotas, E.A., 4x1000, TRM, exportación CSV), implementa la lógica en `core/engine/` y escribe el test en `src/test/kotlin/`.
3. **Verificación obligatoria por consola:** Ejecuta `./gradlew testDebugUnitTest`. No des por terminada una tarea ni pases a la capa de Compose si un test falla o no compila.
4. **UI concisa:** Construye el Composable aplicando los tokens de `core/designsystem/` y verifica la compilación final con `./gradlew assembleDebug`.
