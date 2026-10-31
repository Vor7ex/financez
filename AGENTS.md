# Guía para Agentes — FinanceZ

## 1. Contexto del Producto
FinanceZ es una aplicación móvil Android nativa para trazabilidad financiera personal estricta sin presupuestos restrictivos.

### Pilares Fundamentales
- **Local-First:** 100% manual y privada en SQLite (Room). Sin red ni telemetría.
- **Trazabilidad vs Presupuestos:** Registro exacto de flujos y distribución de activos/pasivos.
- **Estética:** Neobanco minimalista (Dark Khaki, Hunter Green, Ocean Mist, Frosted Mint).

## 2. Decisiones Caras y Alcance
- **Multi-tenant / Roles:** Monousuario local, sin autenticación.
- **Offline:** 100% Offline estricto.
- **Multimoneda:** Moneda base en onboarding; secundarias con TRM manual.
- **Idioma:** Español.

## 3. Stack Tecnológico
- Kotlin 2.0.21, JDK 23 (Android SDK 35, minSdk 26).
- UI: Jetpack Compose + Material 3 (BOM 2024.10.01).
- Estado: `ViewModel` + `StateFlow<UiState>`.
- Persistencia: Room 2.6.1, DataStore Preferences 1.1.1.
- DI: Inyección manual (`AppContainer`).
- Build: Gradle 8.11.1 (`.\gradlew.bat`).

## 4. Convenciones de Desarrollo
- Persistencia consolidada en `data/local/` (`Entities.kt`, `Daos.kt`, `FinanceZDatabase.kt`).
- Dinero en enteros (`Long` en centavos).
- UI: Solo colores de `MaterialTheme.colorScheme` y tipografía de `MaterialTheme.typography` (Creato Display). Cero constantes directas.
- Modularidad: Pantallas descompuestas en `components/`.

## 5. Arquitectura de Features (OCP)
- **`FinanceZApplication` cerrado:** Solo inicializa `AppContainer`.
- **`AppContainer` inmutable:** Solo expone `context`, `database` y `preferencesRepository`. Las features construyen sus repositorios con los DAOs.
- **`FeatureNavGraph`:** Cada feature orquesta y registra sus rutas en `NavGraphBuilder`.
- **ViewModels:** DSL Compose `viewModel { ... }` (cero factories manuales con `as T`).
- **`MainActivity` pasivo:** Host de ~20 líneas que solo monta `AppNavHost(app.container)`.
- **`AppNavHost`:** Registro declarativo de features en una lista.
