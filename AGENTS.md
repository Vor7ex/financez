# Guía para Agentes — FinanceZ

## 1. Contexto del Producto
FinanceZ es una aplicación móvil Android nativa diseñada para personas que desean trazabilidad financiera personal estricta (entradas reales, salidas efectivas y distribución patrimonial) sin presupuestos restrictivos.

### Pilares Fundamentales
- **Filosofía Local-First:** 100% manual y privada. Todo se almacena localmente en SQLite mediante Room. Sin nube, sin backend remoto, sin telemetría ni lectura de SMS/notificaciones.
- **Trazabilidad vs Presupuestos:** Registro exacto de flujos y distribución de activos/pasivos sin límites artificiales.
- **Estética:** Neobanco minimalista (Dark Coffee `#412722`, Muted Teal `#86ba90`, Scarlet Rush `#df2935`, Light Bronze `#dfa06e`, Lemon Chiffon `#f5f3bb`).

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
- Compilación verificable con `.\gradlew.bat assembleDebug`.
