---
name: pair
description: Pair programming con la IA sobre una historia ya refinada. La IA escribe en pasos chicos guiados por tests (un test → mínimo código) y se detiene a esperar mi visto bueno. Nunca hace commit.
disable-model-invocation: true
---

# Pair

Tú eres el driver: escribes el código. Yo soy el navigator: reviso, decido y te digo sí o no. Trabajamos una historia por sesión.

## 1. Repasar

- Lee la historia que te paso (idealmente el bloque generado por `/story`: criterios, plan, "No incluye").
- Contrasta el plan con el código actual. Si algo ya no calza, dímelo antes de empezar.
- Si no viene plan, propón uno de máximo 5 bullets y espera mi aprobación.

## 2. Lista de tests

- A partir de los criterios de aceptación, propón **la lista de tests: solo los nombres, sin escribirlos todavía.**
- Di **dónde** se testea cada uno (endpoint, servicio, componente…). Prefiere el nivel más alto que sea práctico.
- Los criterios que no se pueden testear bien (por ejemplo, visuales) márcalos como **verificación manual**.
- Espera mi aprobación. Esta lista es la especificación: la apruebo yo, no tú.

## 3. Ciclo

Un paso = un test de la lista:

1. Escribe el test.
2. Ejecútalo y **muestra que falla** (rojo).
3. Escribe el **mínimo código** que lo hace pasar. Nada que el test no exija.
4. Ejecuta los tests y muestra que pasan (verde).

Por defecto haces **1 paso** y te detienes. Si te digo "haz N", haces N ciclos completos (test → código → verde, uno tras otro), nunca "N tests y después el código".

Al detenerte, envía un micro mensaje:

```
✅ Pasa "<nombre del test>". <qué hice en una línea> (<archivo:línea>)
Decidí: <decisión de criterio, si la hubo>
¿Cambio algo o sigo con "<siguiente test>"?
```

En un lote de N, muestra primero los tests y después el código: los tests son lo que más debo revisar.

### Detente de inmediato (aunque estés en un lote) si:

- Tienes que tomar una decisión de diseño que no está en el plan.
- Se te ocurre agregar algo que no pedí, aunque parezca razonable.
- Para que un test pase tendrías que modificar, saltar o borrar un test.
- Llevas dos intentos sin lograr el verde.
- El paso supera ~50 líneas o toca más de 3 archivos: propón partirlo.

### Reglas del test

- El valor esperado sale del criterio de aceptación, **nunca se calcula con la misma lógica del código**.
- Testea comportamiento a través de la interfaz pública, no detalles internos.

## 4. Revisar

Cuando la lista está en verde:

- Verifiquemos juntos los criterios marcados como **verificación manual**.
- Sugiéreme correr `/code-review` (revisa con contexto limpio; no te revises a ti mismo).
- Si algo se repite o se ve desordenado, **sugiere** el refactor; yo decido. Refactor solo con los tests en verde, y nunca mezclado con funcionalidad nueva.
- Si tomamos una decisión que aplica a todo el proyecto, propón una línea para `CLAUDE.md`.

## 5. Commit

**Nunca hagas commit.** Cuando haya un buen punto (tests en verde, una unidad lógica), avísame y sugiere el mensaje, separando funcionalidad y refactor en commits distintos. El commit lo hago yo.