\# LedgerCO — Trazabilidad Financiera Personal (Android)



Aplicación nativa Android de contabilidad y trazabilidad patrimonial personal. Diseñada bajo un enfoque \*\*Local-First\*\*, \*\*100% manual\*\*, con \*\*aritmética determinista\*\* y adaptada a la realidad financiera colombiana (GMF 4x1000, Retención en la Fuente, Cajitas de Alto Rendimiento E.A., Tarjetas de Crédito con causación diferida al pago y Operaciones Multimoneda con TRM histórica).



\---



\## 1. Filosofía del Producto



\- \*\*Trazabilidad sobre Presupuestos:\*\* El objetivo no es restringir el gasto mediante presupuestos artificiales, sino responder con exactitud matemática tres preguntas en cualquier instante:

&#x20; 1. ¿Cuánto dinero real entró este mes?

&#x20; 2. ¿Cuánto dinero real salió este mes?

&#x20; 3. ¿Dónde está distribuido exactamente todo mi patrimonio hoy?

\- \*\*Privacidad Absoluta (Offline-First):\*\* Cero servidores externos, cero cuentas en la nube, cero telemetría y cero consumo de APIs en el MVP.

\- \*\*Control Manual de Baja Fricción:\*\* El usuario registra cada movimiento y define la TRM exacta de sus conversiones cambiarias.



\---



\## 2. Reglas de Negocio Invariantes



\### 2.1 Espacios y Tipos de Cuenta

1\. \*\*Liquidez (`LIQUID`):\*\* Efectivo, cuentas de ahorro/corriente y depósitos electrónicos (ej. Bancolombia, Nu Cuenta). Configurables como \*\*Exentas o No Exentas de GMF (4x1000)\*\*.

2\. \*\*Cajitas de Rendimiento (`YIELD\_BOX`):\*\* Bolsillos remunerados con tasa Efectiva Anual (`E.A.`), capitalización configurable (`DAILY` o `MONTHLY`) y aplicación obligatoria de Retención en la Fuente (ej. 7%) cuando corresponda.

3\. \*\*Inversiones (`INVESTMENT`):\*\* Cuentas de bróker (ej. XTB) gestionadas por saldo íntegro en su moneda nativa (`USD` o `COP`).

4\. \*\*Tarjetas de Crédito (`CREDIT\_CARD`):\*\* Instrumentos de deuda con cupo total, día de corte (`cutOffDay`) y día de pago (`dueDay`).



\### 2.2 Flujo Contable de Tarjetas de Crédito (Causación al Pago)

\- \*\*Adquisición (Compra con TC):\*\* Al comprar un bien (a 1 o $N$ cuotas sin intereses), el monto \*\*NO se suma a los Gastos del Mes\*\*. Únicamente reduce el cupo disponible, incrementa el pasivo (deuda pendiente) y genera el plan de cuotas asociado a su categoría original.

\- \*\*Pago de Cuota / Corte:\*\* El gasto real del mes se reconoce \*\*únicamente cuando el dinero sale de una cuenta líquida para pagar la cuota\*\*. En ese momento:

&#x20; 1. Disminuye el saldo de la cuenta líquida origen (aplicando 4x1000 si no es exenta).

&#x20; 2. Disminuye la deuda de la tarjeta de crédito.

&#x20; 3. Se impacta el indicador de \*\*Gastos del Mes\*\* bajo la categoría original de la compra.



\### 2.3 Motor de Rendimientos (`E.A.`) y Conciliación

\- Conversión matemática de Tasa Efectiva Anual a tasa periódica:

&#x20; - Diaria: $r\_d = (1 + \\text{EA})^{1/365} - 1$

&#x20; - Mensual: $r\_m = (1 + \\text{EA})^{1/12} - 1$

\- Descuento automático de retención en la fuente sobre el rendimiento bruto generado.

\- Incluye operación de \*\*Reconciliación Manual\*\* para ajustar diferencias de redondeo bancario sin alterar el flujo de ingresos operativos.



\### 2.4 Multimoneda y TRM Histórica

\- El patrimonio general nunca mezcla divisas arbitrariamente: se presenta separado por moneda (\*\*Patrimonio Neto COP\*\* y \*\*Patrimonio Neto USD\*\*).

\- Toda transferencia entre cuentas de distinta moneda almacena la \*\*TRM exacta de ejecución\*\* ingresada por el usuario en esa fecha.



\---



\## 3. Stack Tecnológico



| Capa | Tecnología | Justificación Técnica |

| :--- | :--- | :--- |

| \*\*Lenguaje\*\* | Kotlin 2.1+ | Tipado fuerte, `value classes` para seguridad de divisas y corrutinas nativas. |

| \*\*UI\*\* | Jetpack Compose (M3) | UI 100% declarativa, Dark Mode nativo, soporte de números tabulares (`tnum`). |

| \*\*Persistencia\*\* | \*\*SQLDelight 2.x\*\* | SQL-first con validación en compilación, Vistas SQL reactivas y transacciones ACID. |

| \*\*Inyección (DI)\*\* | \*\*Koin 4.x\*\* | DSL puro en Kotlin sin penalización de compilación en KSP/KAPT. |

| \*\*Fechas y Tiempo\*\* | `kotlinx-datetime` | Manejo exacto de cortes y cierres mensuales en zona horaria local. |

| \*\*Navegación\*\* | Navigation Compose | Rutas fuertemente tipadas con `kotlinx-serialization`. |

| \*\*Testing\*\* | JUnit 5 + Turbine | Ejecución sub-segundo para motores financieros y flujos de estado MVI. |



\---



\## 4. Arquitectura del Sistema (CQRS-Lite + MVI)



El proyecto evita la sobreingeniería de Clean Architecture tradicional (sin interfaces redundantes ni UseCases pasamanos) utilizando una estructura pragmática en un solo módulo `:app`:



```text

app/src/main/

├── sqldelight/com/personal/ledger/db/

│   ├── Schema.sq                  # Tablas DDL, Índices y Triggers de integridad

│   ├── LedgerQueries.sq           # Consultas transaccionales y escrituras ACID

│   └── DashboardViews.sq          # Vistas SQL para balances y flujo mensual

└── kotlin/com/personal/ledger/

&#x20;   ├── core/

&#x20;   │   ├── model/                 # Value classes (MinorUnits, Currency, RateBp)

&#x20;   │   ├── engine/                # Motores puros: YieldEngine, GmfEngine, CreditCardEngine

&#x20;   │   ├── export/                # Generador determinista de archivos CSV/Excel

&#x20;   │   └── designsystem/          # Tokens visuales, tipografía tabular y teclado numérico

&#x20;   ├── data/

&#x20;   │   └── LedgerRepository.kt    # Orquestador único de transacciones SQLDelight

&#x20;   └── feature/

&#x20;       ├── dashboard/             # UI + ViewModel (MVI) de Patrimonio y Flujo Mensual

&#x20;       ├── accounts/              # Gestión de cuentas, cajitas y reconciliación

&#x20;       ├── creditcards/           # Control de cupos, planes de cuotas y pagos al corte

&#x20;       └── transaction/           # BottomSheet con teclado propio para registro rápido

