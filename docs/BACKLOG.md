# Backlog de Producto — FinanceZ

## Contexto
Aplicación móvil Android para personas que desean trazabilidad financiera personal estricta (entradas reales, salidas efectivas y distribución patrimonial) sin presupuestos restrictivos, 100% manual y privada.  
Stack: Kotlin 2.0+, Jetpack Compose (Material 3), ViewModel + StateFlow, Room (SQLite), DataStore Preferences, Inyección Manual (`AppContainer`).  
Decisiones caras: 100% Offline / Local-first estricto (sin sincronización en la nube ni auth); Multi-moneda (moneda base configurable + cuentas en divisas alternativas con TRM manual por movimiento); Monousuario; Monoidioma (español).

## Historias de Usuario
1. Como usuario nuevo, quiero elegir mi moneda base al iniciar la app, para definir la divisa principal de mis registros.
2. Como usuario, quiero registrar una cuenta de dinero con su saldo inicial, para ver mi patrimonio disponible en el panel principal. Requiere: #1
3. Como usuario, quiero registrar un gasto en una cuenta, para reflejar una salida de dinero de mi saldo disponible. Requiere: #2
4. Como usuario, quiero registrar un ingreso en una cuenta, para reflejar una entrada de dinero a mi saldo disponible. Requiere: #2
5. Como usuario, quiero filtrar mis movimientos por períodos de tiempo, para analizar mi historial en una fecha o rango específico. Requiere: #3
6. Como usuario, quiero transferir dinero entre mis cuentas de liquidez, para trasladar fondos propios sin generar ingresos ni gastos. Requiere: #2
7. Como usuario, quiero registrar cuentas en divisas extranjeras, para gestionar fondos fuera de mi moneda base. Requiere: #1
8. Como usuario, quiero registrar la tasa de cambio manual en transacciones multidivisa, para calcular su equivalencia en la moneda base. Requiere: #7
9. Como usuario, quiero registrar consumos con tarjeta de crédito, para controlar mi cupo disponible sin generar un gasto inmediato. Requiere: #1
10. Como usuario, quiero registrar el pago de mi tarjeta de crédito desde una cuenta líquida, para imputar el gasto efectivo liberando cupo. Requiere: #9
11. Como usuario, quiero crear cajitas de ahorro con su tasa de interés, para apartar fondos que generen rendimientos. Requiere: #1
12. Como usuario, quiero conciliar el abono periódico de rendimientos en una cajita, para sumar las ganancias netas a mi capital. Requiere: #11
13. Como usuario, quiero crear categorías personalizadas, para clasificar mis movimientos según mis propios hábitos. Requiere: #3
14. Como usuario, quiero exportar mis movimientos a un archivo CSV, para analizar mis finanzas en herramientas externas. Requiere: #3
