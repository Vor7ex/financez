# Modelo Entidad-Relación — FinanceZ

## Diagrama Entidad-Relación (ERD)

```mermaid
erDiagram
    INSTITUCION ||--o{ CUENTA : "posee"
    INSTITUCION ||--o{ TARJETA_CREDITO : "emite"
    CUENTA ||--o{ CAJITA_AHORRO : "contiene"
    CATEGORIA ||--o{ MOVIMIENTO_CUENTA : "clasifica"
    CATEGORIA ||--o{ MOVIMIENTO_TARJETA : "clasifica"
    CUENTA ||--o{ MOVIMIENTO_CUENTA : "origen"
    CUENTA ||--o{ MOVIMIENTO_CUENTA : "destino"
    CAJITA_AHORRO ||--o{ MOVIMIENTO_CUENTA : "cajita_asociada"
    CUENTA ||--o{ MOVIMIENTO_TARJETA : "origen_pago"
    TARJETA_CREDITO ||--o{ MOVIMIENTO_TARJETA : "contiene"

    INSTITUCION {
        string id PK
        string nombre
        string logo_uri
    }

    CUENTA {
        string id PK
        string institucion_id FK "nullable"
        string nombre
        string tipo "CUENTA_BANCARIA|EFECTIVO"
        int saldo
        string divisa
        boolean exenta_gmf
    }

    CAJITA_AHORRO {
        string id PK
        string cuenta_id FK
        string nombre
        int saldo
        float tasa_ea
        string periodo_rendimiento "DIARIO|MENSUAL"
    }

    TARJETA_CREDITO {
        string id PK
        string institucion_id FK
        string nombre
        int cupo_total
        int deuda_total
        int dia_corte
        int dia_limite_pago
        string ultimos_cuatro "nullable"
    }

    CATEGORIA {
        string id PK
        string nombre
        string tipo "INGRESO|GASTO"
        string icono
    }

    MOVIMIENTO_CUENTA {
        string id PK
        string tipo "INGRESO|GASTO|TRANSFERENCIA"
        int monto
        string cuenta_origen_id FK
        string cuenta_destino_id FK "nullable"
        string cajita_id FK "nullable"
        string categoria_id FK "nullable"
        float tasa_cambio "nullable"
        date fecha
        string concepto
        string nota "nullable"
    }

    MOVIMIENTO_TARJETA {
        string id PK
        string tarjeta_id FK
        string tipo "CONSUMO|PAGO"
        int monto
        string categoria_id FK "nullable"
        int cuotas_totales "nullable"
        int cuotas_pendientes "nullable"
        float tasa_interes "nullable"
        string cuenta_pago_id FK "nullable"
        date fecha
        string concepto
        string nota "nullable"
    }
```

## Reglas de Integridad y Lógica Financiera
- **Cuentas y GMF:** `EFECTIVO` carece de `institucion_id` y siempre es exento de GMF. `CUENTA_BANCARIA` requiere institución y si `exenta_gmf = false`, sus débitos generan el 4x1000 correspondiente.
- **Cajitas de Ahorro:** Entidades hijas dependientes de una `CUENTA` bancaria. El traspaso cuenta <-> cajita y abonos de rendimiento usan `cajita_id` en `MOVIMIENTO_CUENTA`.
- **Categorías e Instituciones:** Soportan `icono` (SVG, imagen o emoji) y `logo_uri`.
- **Tarjetas:** Los consumos acumulan `deuda_total`; los pagos reducen deuda y debitan de `CUENTA` (`cuenta_pago_id`).