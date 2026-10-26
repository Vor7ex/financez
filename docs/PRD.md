# PRD — FinanceZ (Trazabilidad Financiera Personal)

## 1. Visión y Principios
- **Propósito:** Trazabilidad contable real (entradas, salidas efectivas y distribución patrimonial) sin restricciones artificiales ni presupuestos.
- **Filosofía Local-First:** 100% manual, funcionamiento autónomo sin conexión, sin cuentas en la nube ni telemetría.
- **Estética:** Minimalista estilo neobanco, alta confianza y sobriedad. Paleta: Dark Coffee (`#412722`), Muted Teal (`#86ba90`), Scarlet Rush (`#df2935`), Light Bronze (`#dfa06e`), Lemon Chiffon (`#f5f3bb`).

## 2. Onboarding y Primer Uso
- **Paso 1: Moneda Principal:** Selección de la divisa base del sistema (ej. COP).
- **Paso 2: Creación de Cuentas:** Registro guiado de instrumentos iniciales (efectivo, bancos, tarjetas).
- **Paso 3: Selección de Categorías:** Catálogo predefinido con selección interactiva y adición de personalizadas.

## 3. Requisitos Funcionales
### 3.1 Dashboard Principal
- Balance neto del período (ingresos vs gastos efectivos).
- Selector temporal: Año, Mes, Día y Rango personalizado.
- Listado cronológico de movimientos recientes.
- Botón flotante central (+) prominente para captura inmediata.

### 3.2 Flujo de Captura de Movimientos
- Secuencia ágil: Tipo (Ingreso / Gasto / Transferencia) → Monto → Cuenta → Concepto → Categoría → Confirmación.
- Campos opcionales sin fricción: Fecha editable y notas descriptivas.

### 3.3 Instrumentos Financieros
- **Cuentas Líquidas:** Billetera física y cuentas bancarias para uso cotidiano.
- **Cajitas de Rendimiento:** Proyección de rendimientos por tasa E.A. (diaria/mensual) con retención en la fuente opcional, registradas como ingreso editable/conciliable.
- **Tarjetas de Crédito:** Las compras no imputan gasto del mes; consumen cupo y aumentan pasivo. El gasto se reconoce al liquidar la deuda desde liquidez.
- **Multimoneda:** Cuentas en divisas alternativas con tasa de cambio manual registrada y modificable por movimiento.

### 3.4 Portabilidad
- Exportación completa de movimientos a formatos estándar de hoja de cálculo (CSV / Excel).

## 4. Requisitos No Funcionales y Arquitectura
- **Persistencia:** Base de datos relacional local con mapeo de objetos (Room ORM), estructurada para máxima legibilidad y robustez.
- **Rendimiento:** Tiempos de apertura y respuesta instantáneos sin dependencia de red.
- **Exclusiones MVP:** Sin presupuestos, sin lectura de SMS/notificaciones, sin copias de seguridad complejas (reservadas para fases finales).