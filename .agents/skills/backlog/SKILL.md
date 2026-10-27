---
name: backlog
description: Convierte una idea de software o una feature conversada en un backlog de historias de usuario cortas y verticales. Sin criterios de aceptación ni implementación; eso es trabajo de /story.
disable-model-invocation: true
---

# Backlog

Tu trabajo es ayudarme a dividir lo que te cuento en historias de usuario. Yo pongo los requerimientos; tú los ordenas y los cortas.

## Conversación

1. Escucha la idea o feature. Si algo no se entiende, pregunta. Una pregunta a la vez, con tu respuesta sugerida.
2. Si es un proyecto nuevo, conversemos el stack. Solo el stack, no la arquitectura ni la implementación.
3. Pregunta si aplica alguna de estas decisiones caras de cambiar. Solo pregunta, no las diseñes:
   - Multi-tenant o multi-organización
   - Modelo de permisos o roles
   - Offline o sincronización
   - Multi-idioma o multi-moneda
4. Propón el backlog. Yo lo ajusto.

## Reglas

- **No inventes requerimientos.** Solo divide lo que te conté. Si ves un hueco, pregúntame; no lo rellenes.
- **Cada historia entrega valor por sí sola** y atraviesa todas las capas (datos, backend, UI). Nunca dividas por capa.
- **Ninguna historia técnica.** El actor nunca es el desarrollador ni el sistema. "Configurar la base de datos" o "crear el modelo de usuario" no son historias; esa infraestructura se construye dentro de la primera historia que la necesita.
- **Pocas historias.** Máximo 10–15. Prefiero agregar a recortar.
- **Una línea por historia.** Sin criterios de aceptación, sin detalles técnicos, sin estimaciones ni prioridades.
- La primera historia es la más chica que se puede usar de punta a punta (walking skeleton).

## Formato de salida

```markdown
## Contexto
<una o dos líneas: qué es y para quién>
Stack: <lo acordado>
Decisiones caras: <las que aplican, o "ninguna">

## Backlog
1. Como <actor>, quiero <acción>, para <beneficio>.
2. Como <actor>, quiero <acción>, para <beneficio>. Requiere: #1
```

Agrega `Requiere: #N` solo cuando una historia no tenga sentido sin otra.

Al terminar, ofrece guardar el contexto y el stack en `CLAUDE.md` (o `AGENTS.md`). Las historias van a mi tracker; no las guardes en archivos salvo que te lo pida.