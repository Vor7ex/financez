# Guía para Agentes — FinanceZ

## 1. Contexto del Producto
FinanceZ es una aplicación móvil Android nativa diseñada para personas que desean trazabilidad financiera personal estricta (entradas reales, salidas efectivas y distribución patrimonial) sin presupuestos restrictivos.

### Pilares Fundamentales
- **Filosofía Local-First:** 100% manual y privada. Todo se almacena localmente en SQLite mediante Room. Sin nube, sin backend remoto, sin telemetría ni lectura de SMS/notificaciones.
- **Trazabilidad vs Presupuestos:** Registro exacto de flujos y distribución de activos/pasivos sin límites artificiales.
- **Estética:** Neobanco minimalista (Dark Khaki `#3D3522` / `#4A442D`, Hunter Green `#386150`, Ocean Mist `#58B09C`, Frosted Mint `#CAF7E2`).

## 2. Decisiones Caras y Alcance
- **Multi-tenant / Roles:** Monousuario local, sin autenticación ni perfiles.
- **Offline / Sincronización:** 100% Offline estricto. Cero sincronización en red.
- **Multimoneda:** Moneda base elegible en onboarding; cuentas secundarias con TRM manual registrada por movimiento.
- **Idioma:** Monoidioma (Español).

## 3. Stack Tecnológico
- **Lenguaje & JDK:** Kotlin 2.0.21 con JDK 23 (Android SDK 35, minSdk 26).
- **UI:** Jetpack Compose + Material 3 (Compose BOM 2024.10.01).
- **Gestión de Estado:** `ViewModel` + `StateFlow<UiState>`.
- **Persistencia:** Room 2.6.1 (SQLite local) con KSP 2.0.21-1.0.28, DataStore Preferences 1.1.1.
- **Inyección de Dependencias:** Inyección manual mediante contenedor de aplicación (`AppContainer`).
- **Construcción:** Gradle 8.11.1 mediante `.\gradlew.bat`.

## 4. Convenciones de Desarrollo
- Mantener la arquitectura pragmática: persistencia consolidada en `app/src/main/kotlin/com/personal/financez/data/local/` (`Entities.kt`, `Daos.kt`, `FinanceZDatabase.kt`).
- Valores monetarios almacenados en tipos enteros (`Long` en centavos) para evitar errores de precisión de punto flotante.
- Documentos de diseño en `docs/` mantenidos concisos (< 100 líneas).
- Sistema de diseño y UI: Las pantallas y componentes composables nunca deben importar constantes de color directas; consumen exclusivamente tokens semánticos vía `MaterialTheme.colorScheme`.
- Compilación verificable con `.\gradlew.bat assembleDebug`.
