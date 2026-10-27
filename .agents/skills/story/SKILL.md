---
name: story
description: Toma UNA historia de usuario del backlog, le agrega 2-3 criterios de aceptación y propone un mini plan de implementación para aprobar. No escribe código; la implementación es trabajo de /pair.
disable-model-invocation: true
---

# Story

Refinamos una sola historia, justo antes de construirla. Tú propones y yo decido.

## Pasos

1. **Lee la historia** que te paso (texto pegado, issue de GitHub, ticket de Jira, etc.) y revisa el código actual y `CLAUDE.md` para entender el contexto.
2. **Criterios de aceptación.** Propón 2–3 en formato Dado/Cuando/Entonces. Solo describen *qué* ve u obtiene el usuario, nunca *cómo* se implementa.
3. **Si necesita más de 3 criterios, no la engordes: propón dividirla.** Entrega las nuevas historias en formato de backlog (`Como…, quiero…, para…`) para que yo las agregue al tracker, y seguimos con la primera.
4. **Mini plan.** Máximo 5 bullets, sin código ni nombres de archivo. Cómo atraviesa las capas, con lo mínimo que cumple los criterios.
5. **Espera mi aprobación.** Ajustamos hasta que diga que sí.

## Reglas

- **No inventes requerimientos.** Si falta información, pregúntame.
- **Lo más simple que cumple los criterios.** Nada de abstracciones, capas, configuraciones o patrones "para el futuro".
- **Sugiere, no decidas.** Si un patrón podría servir (Atomic Design, composición de middlewares, extraer un componente…), menciónalo bajo "Sugerencias" con su porqué. Solo sugiérelo si hay una señal concreta en el código (por ejemplo, algo que ya se repite), no por adelantado.
- **No escribas código.**

## Formato de salida

Un bloque listo para pegar en el issue. `/pair` lo usa como entrada.

```markdown
## Historia
Como <actor>, quiero <acción>, para <beneficio>.

## Criterios de aceptación
- [ ] Dado <contexto>, cuando <acción>, entonces <resultado>.
- [ ] ...

## Plan
- ...

## No incluye
- <lo que queda fuera de esta historia, para que no se cuele>

## Sugerencias (tú decides)
- <opcional>
```