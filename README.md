# Escribiendo y ejecutando primeras pruebas unitarias con Assertions

## 1. ¿Qué estamos probando?

Tenemos dos clases:

- `Cuenta.java`: clase que queremos probar.
- `CuentaTest.java`: clase que contiene las pruebas para verificar el comportamiento de `Cuenta`.

Una analogía: `Cuenta` es el producto que fabricamos y `CuentaTest` es el control de calidad que comprueba que funcione como esperamos.

## 2. La clase `Cuenta`

```java
package org.gerardo.junitapp.ejemplo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Cuenta {
    private String persona;
    private BigDecimal saldo;
}
```

### Conceptos

- `package`: organiza la clase dentro de un paquete lógico.
- `BigDecimal`: representa números decimales con precisión controlada. Es preferible a `double` para importes monetarios.
- `@Getter`: Lombok genera métodos para consultar los atributos, como `getPersona()`.
- `@Setter`: Lombok genera métodos para modificar los atributos, como `setPersona(...)`.
- `@AllArgsConstructor`: Lombok genera un constructor con todos los atributos como parámetros.

El constructor generado es conceptualmente equivalente a:

```java
public Cuenta(String persona, BigDecimal saldo) {
    this.persona = persona;
    this.saldo = saldo;
}
```

Por eso podemos escribir:

```java
Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
```

`BigDecimal` recibe aquí una cadena para evitar introducir errores de representación provenientes de un literal `double`.

## 3. La clase de prueba `CuentaTest`

```java
package org.gerardo.junitapp.ejemplo.models;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void debeObtenerElNombreDeLaPersona() {
        Cuenta cuenta = new Cuenta(
            "Andres",
            new BigDecimal("1000.12345")
        );

        String esperado = "Andres";
        String real = cuenta.getPersona();

        assertEquals(esperado, real);
        assertTrue(real.equals("Andres"));
    }
}
```

### Conceptos

- `CuentaTest`: convención común para nombrar la clase de pruebas de `Cuenta`.
- `@Test`: indica a JUnit Jupiter que el método es una prueba que debe descubrir y ejecutar.
- `void`: el método no devuelve un valor.
- `debeObtenerElNombreDeLaPersona()`: nombre descriptivo del método de prueba. En JUnit Jupiter, los métodos de prueba no necesitan ser `public`.
- `import org.junit.jupiter.api.Test`: importa la anotación `@Test`.
- `import static org.junit.jupiter.api.Assertions.*`: permite llamar directamente a las aserciones, sin escribir `Assertions.`.

En el código del curso aparecen `import org.junit.jupiter.api.*;` e `import org.junit.jupiter.api.Test;`. El segundo es redundante si ya se importa todo el paquete con `*`. Para esta prueba basta con importar `Test`.

## 4. Valor esperado y valor real

```java
String esperado = "Andres";
String real = cuenta.getPersona();
```

- **Esperado:** el resultado que creemos correcto.
- **Real:** el resultado que devuelve el código al ejecutarse.

La prueba compara ambos valores. Si el nombre obtenido cambia inesperadamente, la prueba puede detectar el problema.

Este patrón es una base de las pruebas unitarias: preparar los datos, ejecutar una acción y comprobar el resultado.

## 5. ¿Qué son las Assertions?

Las **Assertions** (aserciones) son métodos que comprueban condiciones dentro de una prueba.

Ejemplos de preguntas que pueden responder:

- ¿El resultado coincide con el esperado?
- ¿Una condición es verdadera?
- ¿Un objeto es nulo?
- ¿Se lanzó la excepción esperada?

Si una aserción falla, JUnit marca la prueba como fallida. Si todas pasan y no ocurre otro error, la prueba termina correctamente.

### Import estático

```java
import static org.junit.jupiter.api.Assertions.*;
```

Permite escribir:

```java
assertEquals(esperado, real);
```

en vez de:

```java
Assertions.assertEquals(esperado, real);
```

## 6. `assertEquals()`

```java
assertEquals(esperado, real);
```

Comprueba que el valor esperado sea igual al valor real.

Ejemplo que pasa:

```java
assertEquals("Andres", "Andres");
```

Ejemplo que falla:

```java
assertEquals("Andres", "Carlos");
```

El orden recomendado es:

```java
assertEquals(valorEsperado, valorReal);
```

Así el reporte de error permite identificar fácilmente qué esperábamos y qué obtuvimos.

## 7. `assertTrue()`

```java
assertTrue(real.equals("Andres"));
```

Comprueba que la expresión recibida sea verdadera.

- `equals("Andres")` compara el contenido de la cadena con `"Andres"`.
- Si coincide, `equals()` devuelve `true` y la aserción pasa.
- Si no coincide, devuelve `false` y la aserción falla.

También puede comprobar condiciones lógicas, por ejemplo:

```java
assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
```

Esta expresión verifica que el saldo sea mayor que cero. `compareTo()` devuelve un número negativo, cero o positivo según el saldo sea menor, igual o mayor que cero.

## 8. ¿Necesitamos las dos aserciones del ejemplo?

El código original contiene:

```java
assertEquals(esperado, real);
assertTrue(real.equals("Andres"));
```

Ambas comprueban prácticamente lo mismo: que el nombre sea `"Andres"`. Una sola aserción basta:

```java
assertEquals("Andres", real);
```

`assertEquals()` suele ser más claro al comparar valores. `assertTrue()` es útil para comprobar condiciones booleanas generales.

## 9. Resumen

| Elemento | Función |
|---|---|
| `@Test` | Marca un método como prueba de JUnit. |
| `Assertions` | Proporciona métodos para verificar resultados y condiciones. |
| `assertEquals(expected, actual)` | Comprueba que el valor esperado y el real sean iguales. |
| `assertTrue(condition)` | Comprueba que una condición sea verdadera. |
| `@Getter` | Lombok genera métodos getter. |
| `@Setter` | Lombok genera métodos setter. |
| `@AllArgsConstructor` | Lombok genera un constructor con todos los atributos. |
| `BigDecimal` | Representa valores decimales con precisión controlada. |

## 10. Idea principal

Una prueba unitaria comprueba una parte pequeña del programa de forma aislada. En este ejemplo, creamos una cuenta, obtenemos el nombre y verificamos con una aserción que el resultado coincida con el esperado.

Las pruebas ayudan a detectar regresiones: errores que aparecen cuando modificamos código que antes funcionaba.

# Escribiendo test para el saldo

## 1. Objetivo de la prueba

La prueba verifica dos cosas sobre el saldo de una cuenta:

1. Que el saldo tenga el valor esperado: `1000.12345`.
2. Que el saldo no sea negativo.

Código del curso:

```java
@Test
void testSaldoCuenta() {
    Cuenta cuenta = new Cuenta(
        "Andres",
        new BigDecimal("1000.12345")
    );

    assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
    assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
}
```

## 2. `getSaldo()`

`cuenta.getSaldo()` obtiene el saldo de la cuenta. En la clase `Cuenta`, el atributo `saldo` es de tipo `BigDecimal`, y Lombok genera el getter gracias a `@Getter`.

## 3. `doubleValue()`

`cuenta.getSaldo().doubleValue()` convierte el valor `BigDecimal` a un tipo primitivo `double`. El curso lo hace para compararlo con `1000.12345`, que es un literal `double`.

**Precaución:** convertir `BigDecimal` a `double` puede perder precisión. Para importes monetarios suele ser preferible mantener la comparación en `BigDecimal`.

Una alternativa para este ejemplo:

```java
BigDecimal esperado = new BigDecimal("1000.12345");
assertEquals(esperado, cuenta.getSaldo());
```

Esto funciona porque ambos valores tienen la misma escala. `BigDecimal.equals()` distingue la escala: `10.0` y `10.00` no son iguales mediante `equals()`, aunque tengan el mismo valor numérico.

Para comparar el valor numérico sin importar la escala:

```java
assertEquals(
    0,
    cuenta.getSaldo().compareTo(new BigDecimal("1000.12345"))
);
```

## 4. `BigDecimal.ZERO`

`BigDecimal.ZERO` es una constante de `BigDecimal` que representa el número cero. Se utiliza para comparar el saldo con cero.

## 5. `compareTo()`

```java
cuenta.getSaldo().compareTo(BigDecimal.ZERO)
```

Compara el saldo con cero:

- Resultado negativo: el saldo es menor que cero.
- Resultado `0`: el saldo es igual a cero.
- Resultado positivo: el saldo es mayor que cero.

## 6. El operador `< 0`

```java
cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0
```

Pregunta: “¿El saldo es menor que cero?”. El resultado es un `boolean`:

- `true` si el saldo es negativo.
- `false` si el saldo es cero o positivo.

## 7. `assertFalse()`

```java
assertFalse(
    cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0
);
```

`assertFalse()` verifica que la condición recibida sea falsa. Queremos comprobar que la condición “el saldo es menor que cero” no se cumpla.

| Saldo | ¿Saldo < 0? | Resultado de `assertFalse()` |
|---:|---|---|
| `-50` | `true` | Falla |
| `0` | `false` | Pasa |
| `1000` | `false` | Pasa |

## 8. Resumen de los conceptos

| Concepto | Función |
|---|---|
| `getSaldo()` | Obtiene el saldo de la cuenta. |
| `doubleValue()` | Convierte `BigDecimal` a `double`, con posible pérdida de precisión. |
| `BigDecimal.ZERO` | Constante que representa cero. |
| `compareTo()` | Compara numéricamente dos valores `BigDecimal`. |
| `< 0` | Comprueba si el resultado de la comparación es negativo. |
| `assertFalse()` | Hace que la prueba falle si la condición es verdadera. |
| `assertEquals()` | Comprueba que el valor esperado coincida con el valor real. |

## 9. Idea principal

La prueba utiliza una aserción para comprobar el valor del saldo y otra para comprobar una regla: que el saldo no sea negativo.

Recuerda: `assertFalse(condicion)` pasa cuando la condición es `false`. Si la condición es `true`, la aserción falla.

# JUnit 5: TDD con JUnit

## 1. ¿Qué es TDD?

TDD significa *Test-Driven Development* o desarrollo guiado por pruebas. Es una forma de desarrollar software escribiendo primero una prueba que defina el comportamiento deseado.

El ciclo es **Red → Green → Refactor**:

1. **Red:** escribir una prueba que falle porque la funcionalidad todavía no está implementada.
2. **Green:** escribir el código mínimo para que la prueba pase.
3. **Refactor:** mejorar el código sin cambiar el comportamiento y volver a ejecutar las pruebas.

## 2. ¿Qué comprueba el ejemplo?

```java
@Test
void testReferenciaCuenta() {
    Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
    Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

    // assertNotEquals(cuenta2, cuenta);
    assertEquals(cuenta2, cuenta);
}
```

Se crean dos objetos distintos con los mismos datos. La prueba espera que sean iguales según la definición de igualdad de `Cuenta`.

## 3. El método `equals()`

```java
@Override
public boolean equals(Object o) {
    if (!(o instanceof Cuenta cuenta)) return false;

    return Objects.equals(persona, cuenta.persona)
        && Objects.equals(saldo, cuenta.saldo);
}
```

- `@Override`: indica que se sobrescribe un método heredado.
- `equals(Object o)`: recibe un objeto y devuelve `true` o `false` según el criterio de igualdad.
- `instanceof`: comprueba si el objeto recibido es una instancia de `Cuenta`.
- `!`: niega la condición.
- `return false`: termina el método y devuelve `false`.
- `o instanceof Cuenta cuenta`: comprueba el tipo y declara la variable `cuenta` si la comprobación tiene éxito.
- `Objects.equals(a, b)`: compara valores de forma segura incluso si alguno es `null`.
- `&&`: operador lógico Y; ambas comparaciones deben ser verdaderas.

Aquí, dos cuentas son iguales si coinciden tanto la persona como el saldo.

**Detalle:** `BigDecimal.equals()` distingue la escala. Por ejemplo, `8900.0` y `8900.00` no son iguales mediante `equals()`, aunque representen el mismo valor numérico.

## 4. El método `hashCode()`

```java
@Override
public int hashCode() {
    return Objects.hash(persona, saldo);
}
```

- `hashCode()` devuelve un entero utilizado, entre otros lugares, por estructuras como `HashMap` y `HashSet`.
- `Objects.hash(persona, saldo)` calcula un código hash a partir de esos atributos.
- Si dos objetos son iguales según `equals()`, deben tener el mismo código hash. Dos objetos diferentes pueden compartir un código hash.

Por eso, al sobrescribir `equals()`, normalmente también se sobrescribe `hashCode()` usando los mismos atributos relevantes.

## 5. `assertEquals()` y `assertNotEquals()`

`assertEquals(cuenta2, cuenta)` verifica que los objetos sean iguales según su método `equals()`.

`assertNotEquals(cuenta2, cuenta)` verifica que no sean iguales según `equals()`.

Como ambos objetos tienen la misma persona y el mismo saldo, `assertEquals(cuenta2, cuenta)` pasa con la implementación mostrada. `assertNotEquals(cuenta2, cuenta)` fallaría.

## 6. ¿Cómo se aplica TDD a este ejemplo?

1. Escribir primero una prueba que establezca que dos cuentas con los mismos datos deben ser iguales.
2. Ejecutarla y observar que falla con la igualdad predeterminada de `Object`, que compara identidad.
3. Implementar `equals()` para comparar `persona` y `saldo`.
4. Ejecutar la prueba otra vez y comprobar que pasa.
5. Implementar o ajustar `hashCode()` para respetar el contrato con `equals()`.
6. Refactorizar si hace falta y volver a ejecutar las pruebas.

Si se escribe `equals()` antes de la prueba, sigue existiendo una prueba unitaria útil, pero no se sigue estrictamente el orden de TDD para esa funcionalidad.

## 7. Resumen

| Concepto | Función |
|---|---|
| TDD | Desarrollar guiándose por pruebas escritas primero. |
| Red → Green → Refactor | Fallar, lograr que pase y mejorar el código. |
| `equals()` | Define cuándo dos objetos se consideran iguales. |
| `instanceof` | Comprueba el tipo del objeto. |
| `Objects.equals()` | Compara objetos de forma segura frente a `null`. |
| `&&` | Exige que ambas condiciones sean verdaderas. |
| `hashCode()` | Proporciona un código hash coherente con `equals()`. |
| `assertEquals()` | Verifica que dos valores u objetos sean iguales. |
| `assertNotEquals()` | Verifica que dos valores u objetos no sean iguales. |

## 8. Idea principal

Dos objetos pueden ser instancias distintas creadas con `new` y, aun así, considerarse iguales por sus datos si la clase define ese criterio mediante `equals()`. JUnit permite comprobarlo con `assertEquals()`. TDD propone escribir esa expectativa primero y luego implementar el código que la haga pasar.

# JUnit 5: TDD para débito y crédito

## Objetivo

Probamos dos operaciones que modifican el saldo:

- Débito: `1000.12345 - 100 = 900.12345`.
- Crédito: `1000.12345 + 100 = 1100.12345`.

## Métodos de `Cuenta`

```java
public void debito(BigDecimal monto) {
    this.saldo = this.saldo.subtract(monto);
}

public void credito(BigDecimal monto) {
    this.saldo = this.saldo.add(monto);
}
```

`public` permite llamar al método desde otras clases. `void` indica que no devuelve un valor. `monto` es el importe que se resta o suma. `this.saldo` es el atributo del objeto actual. `subtract()` resta y `add()` suma.

`BigDecimal` es inmutable: `add()` y `subtract()` devuelven nuevos objetos, por eso el resultado se asigna a `this.saldo`.

## Prueba de débito

```java
@Test
void testDebitoCuenta() {
    Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
    cuenta.debito(new BigDecimal("100"));

    assertNotNull(cuenta.getSaldo());
    assertEquals(900, cuenta.getSaldo().intValue());
    assertEquals("900.12345", cuenta.getSaldo().toPlainString());
}
```

- `assertNotNull()` comprueba que el saldo no sea `null`, pero no verifica por sí sola que la resta sea correcta.
- `intValue()` convierte el saldo a `int`. `900.12345` se convierte en `900`, descartando la parte decimal, sin redondear. Esta aserción solo comprueba la parte entera.
- `toPlainString()` devuelve el `BigDecimal` como texto sin notación científica. La comparación verifica el saldo completo, incluidos los decimales.

## Prueba de crédito

```java
@Test
void testCreditoCuenta() {
    Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
    cuenta.credito(new BigDecimal("100"));

    assertNotNull(cuenta.getSaldo());
    assertEquals(1100, cuenta.getSaldo().intValue());
    assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
}
```

El resultado correcto es `1100.12345`. Si el comentario del código indica `900.12345`, es una errata: ese es el resultado del débito.

## Comparar `BigDecimal` directamente

Es preferible evitar convertir importes a `int` o `double`, porque se puede perder información.

Una opción es comparar numéricamente con `compareTo()`:

```java
assertEquals(
    0,
    cuenta.getSaldo().compareTo(new BigDecimal("900.12345"))
);
```

`compareTo()` devuelve `0` cuando los valores son numéricamente iguales, incluso si tienen distinta escala.

También se puede usar `assertEquals()` directamente si valor y escala deben coincidir:

```java
assertEquals(new BigDecimal("900.12345"), cuenta.getSaldo());
```

`BigDecimal.equals()` distingue la escala: `10.0` y `10.00` no son iguales con `equals()`.

## Relación con TDD

1. Escribir primero una prueba que defina el saldo esperado después del débito.
2. Ejecutarla y observar que falle si la funcionalidad no existe o está mal implementada.
3. Implementar `debito()` hasta que pase.
4. Repetir el proceso con `credito()`.
5. Refactorizar y ejecutar las pruebas de nuevo.

Si se implementan los métodos antes de escribir los tests, las pruebas siguen siendo útiles, pero no se sigue estrictamente el orden TDD para esas funcionalidades.

## Resumen

| Concepto | Función |
|---|---|
| `debito(monto)` | Resta el importe al saldo. |
| `credito(monto)` | Suma el importe al saldo. |
| `subtract()` | Resta valores `BigDecimal`. |
| `add()` | Suma valores `BigDecimal`. |
| `assertNotNull()` | Comprueba que el resultado no sea `null`. |
| `intValue()` | Convierte a entero y descarta la parte decimal. |
| `toPlainString()` | Convierte el valor a texto sin notación científica. |
| `compareTo()` | Compara numéricamente valores `BigDecimal`. |

## Idea principal

Las pruebas verifican que el saldo cambie correctamente después de un movimiento. Para validar importes decimales, comprueba el valor completo y no dependas únicamente de `intValue()`.

# Probando y afirmando excepciones con `assertThrows` en JUnit 5

## 1. ¿Qué es una excepción?

Una excepción es un evento que ocurre durante la ejecución de un programa e indica que una operación no puede continuar normalmente.

Ejemplo: una cuenta tiene $1,000 y alguien intenta retirar $1,500. La aplicación debe impedir el retiro y comunicar que no hay dinero suficiente.

## 2. Crear una excepción personalizada

```java
package org.gerardo.junitapp.ejemplo.exceptions;

public class DineroInsuficienteException extends RuntimeException {
    public DineroInsuficienteException(String message) {
        super(message);
    }
}
```

- `public`: permite acceder a la clase desde otras clases.
- `class DineroInsuficienteException`: declara una excepción con un nombre específico del negocio.
- `extends RuntimeException`: hereda de una excepción no comprobada (*unchecked*). Java no obliga a capturarla ni declararla con `throws`.
- `String message`: recibe el mensaje que describe el problema.
- `super(message)`: pasa el mensaje al constructor de `RuntimeException`.
- `throw new DineroInsuficienteException(...)`: crea y lanza la excepción.

## 3. Validar el saldo antes de hacer el débito

```java
public void debito(BigDecimal monto) {
    BigDecimal nuevoSaldo = this.saldo.subtract(monto);

    if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
        throw new DineroInsuficienteException("Dinero Insuficiente");
    }

    this.saldo = nuevoSaldo;
}
```

- `public`: el método puede ser llamado desde otras clases.
- `void`: el método no devuelve un valor.
- `debito(BigDecimal monto)`: recibe el importe que se quiere retirar.
- `subtract(monto)`: calcula la resta y devuelve un nuevo `BigDecimal`; no modifica el objeto original.
- `BigDecimal.ZERO`: representa el valor cero.
- `compareTo(BigDecimal.ZERO) < 0`: comprueba si el nuevo saldo es menor que cero.
- `throw`: lanza la excepción cuando el saldo sería negativo.
- `this.saldo = nuevoSaldo`: actualiza el saldo solo si la validación pasa.

**Importante:** calcular `nuevoSaldo` antes de asignarlo a `this.saldo` permite rechazar el retiro sin modificar el saldo original.

## 4. ¿Qué hace `assertThrows()`?

`assertThrows()` es una aserción de JUnit 5 que verifica que un bloque de código lance una excepción del tipo esperado.

Forma general:

```java
TipoExcepcion excepcion = assertThrows(
    TipoExcepcion.class,
    () -> {
        // Código que debería lanzar la excepción
    }
);
```

- `TipoExcepcion.class`: indica la clase de excepción esperada.
- `() -> { ... }`: es una expresión lambda que representa el código que JUnit debe ejecutar.
- `assertThrows(...)`: hace fallar la prueba si no se lanza la excepción esperada. También acepta una excepción de un subtipo del esperado.
- El valor devuelto es la excepción capturada, que se puede utilizar para revisar su mensaje.

Si el bloque no lanza ninguna excepción, la prueba falla. Si lanza una excepción de un tipo incompatible, también falla.

## 5. Probar la excepción y su mensaje

```java
@Test
void dineroInsuficienteException() {
    Cuenta cuenta = new Cuenta(
        "Andres",
        new BigDecimal("1000.12345")
    );

    DineroInsuficienteException exception = assertThrows(
        DineroInsuficienteException.class,
        () -> cuenta.debito(new BigDecimal("1500"))
    );

    String actual = exception.getMessage();
    String esperado = "Dinero Insuficiente";

    assertEquals(esperado, actual);
}
```

Paso a paso:

1. Se crea una cuenta con un saldo de `1000.12345`.
2. Se intenta retirar `1500`, por lo que el nuevo saldo sería negativo.
3. `assertThrows()` comprueba que se lance `DineroInsuficienteException`.
4. La excepción capturada se guarda en `exception`.
5. `getMessage()` recupera el mensaje de la excepción.
6. `assertEquals(esperado, actual)` comprueba que el mensaje sea exactamente `"Dinero Insuficiente"`.

Es preferible declarar la variable como `DineroInsuficienteException` en lugar de `Exception`, porque así queda claro el tipo concreto que esperamos y podemos acceder a sus métodos específicos si los tuviera.

## 6. ¿Qué pasa con el saldo cuando se lanza la excepción?

En el método `debito`, primero se calcula `nuevoSaldo`. Si resulta negativo, se lanza la excepción antes de ejecutar `this.saldo = nuevoSaldo`.

Por eso, el saldo original debe permanecer sin cambios. Podemos comprobarlo con una prueba más completa:

```java
@Test
void noDebeModificarSaldoCuandoNoHayDineroSuficiente() {
    Cuenta cuenta = new Cuenta(
        "Andres",
        new BigDecimal("1000.12345")
    );

    assertThrows(
        DineroInsuficienteException.class,
        () -> cuenta.debito(new BigDecimal("1500"))
    );

    assertEquals(
        new BigDecimal("1000.12345"),
        cuenta.getSaldo()
    );
}
```

Esta prueba verifica dos comportamientos importantes:
- Se lanza la excepción cuando el retiro supera el saldo.
- El saldo no cambia si el retiro es rechazado.

`BigDecimal.equals()` considera tanto el valor como la escala decimal. En este ejemplo ambos valores tienen la misma escala, así que la comparación es apropiada. Si quisieras comparar solo el valor numérico sin importar la escala, podrías usar `compareTo()`.

## 7. Imports necesarios

En la clase de pruebas puedes usar:

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.gerardo.junitapp.ejemplo.exceptions.DineroInsuficienteException;
```

- `@Test`: marca un método como prueba de JUnit.
- `Assertions.*`: importa estáticamente las aserciones para escribir `assertThrows`, `assertEquals` y otras sin anteponer `Assertions.`.
- `DineroInsuficienteException`: permite usar la excepción personalizada en la prueba.

El import `org.junit.jupiter.api.*` ya incluye `Test`, por lo que no es necesario importar también `org.junit.jupiter.api.Test` por separado. Basta con usar uno de los dos estilos.

## 8. Resumen

| Elemento | Función |
|---|---|
| `RuntimeException` | Clase base para excepciones no comprobadas. |
| `throw` | Lanza una excepción. |
| `super(message)` | Envía el mensaje al constructor de la clase padre. |
| `assertThrows()` | Comprueba que se lance la excepción esperada. |
| `TipoExcepcion.class` | Especifica el tipo de excepción que debe ocurrir. |
| `() -> ...` | Expresión lambda que contiene la operación a probar. |
| `getMessage()` | Recupera el mensaje de la excepción. |
| `assertEquals()` | Compara el mensaje esperado con el real. |

La idea principal es que una prueba unitaria no solo comprueba que el código funcione en situaciones normales; también verifica que rechace correctamente las operaciones inválidas y que lo haga de la forma esperada.

# Agregando la clase `Banco` y la relación con las cuentas

## 1. Relación entre `Banco` y `Cuenta`

`Banco` administra una lista de objetos `Cuenta`, y cada `Cuenta` guarda una referencia al banco asociado. Es una **asociación bidireccional**: puedes navegar del banco a sus cuentas y de una cuenta a su banco.

## 2. Cambios en `Cuenta`

```java
@Getter
@Setter
@AllArgsConstructor
public class Cuenta {
    private String persona;
    private BigDecimal saldo;
    private Banco banco;

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }
}
```

- `private Banco banco`: referencia a un objeto `Banco`.
- `@Getter` y `@Setter`: Lombok genera métodos como `getBanco()` y `setBanco(...)`.
- `@AllArgsConstructor`: genera un constructor con todos los atributos, ahora tres: persona, saldo y banco.
- El constructor manual de dos argumentos permite seguir creando cuentas sin indicar el banco. En ese caso, `banco` queda `null` hasta asignarlo.

## 3. La clase `Banco`

```java
@Getter
@Setter
public class Banco {
    private String nombre;
    private List<Cuenta> cuentas;

    public Banco() {
        cuentas = new ArrayList<>();
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto) {
        origen.debito(monto);
        destino.credito(monto);
    }

    public void addCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
        cuenta.setBanco(this);
    }
}
```

- `List<Cuenta>`: `List` es una interfaz de Java para una colección ordenada; aquí contiene objetos `Cuenta`.
- `ArrayList<>`: implementación de `List` cuyo tamaño puede crecer. `new ArrayList<>()` crea una lista vacía.
- El constructor inicializa `cuentas`, evitando intentar agregar elementos a una lista `null`.
- `@Getter` y `@Setter`: Lombok genera métodos para consultar y modificar los atributos.

## 4. El método `addCuenta()`

```java
public void addCuenta(Cuenta cuenta) {
    cuentas.add(cuenta);
    cuenta.setBanco(this);
}
```

- `cuentas.add(cuenta)`: registra la cuenta en la lista del banco.
- `cuenta.setBanco(this)`: establece la referencia inversa desde la cuenta al banco.
- `this`: representa el objeto actual. Si ejecutas `banco.addCuenta(cuenta1)`, `this` representa `banco`.

Las dos líneas mantienen la relación en ambas direcciones. El código actual no impide cuentas duplicadas ni evita que una cuenta se asocie con distintos bancos; eso requeriría validaciones adicionales.

## 5. El método `transferir()`

```java
public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto) {
    origen.debito(monto);
    destino.credito(monto);
}
```

- `origen`: cuenta de la que sale el dinero.
- `destino`: cuenta que recibe el dinero.
- `monto`: cantidad que se transfiere.
- `debito(monto)`: resta el importe de la cuenta de origen.
- `credito(monto)`: suma el importe a la cuenta de destino.

Si el débito lanza una excepción, el crédito no se ejecuta. En un sistema bancario real, las transferencias necesitan controles adicionales para garantizar que ambas operaciones se completen correctamente o que ninguna quede aplicada.

## 6. Probar una transferencia

```java
@Test
void testTransferirDineroCuentas() {
    Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
    Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

    Banco banco = new Banco();
    banco.setNombre("Banco del estado");

    banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));

    assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
    assertEquals("3000", cuenta1.getSaldo().toPlainString());
}
```

La cuenta de Andres es el origen y la de Jhon Doe es el destino:
- Andres: `1500.8989 - 500 = 1000.8989`.
- Jhon Doe: `2500 + 500 = 3000`.

`toPlainString()` convierte `BigDecimal` a texto sin notación científica. Esta prueba compara las cadenas exactamente. También puedes comparar numéricamente con `compareTo()`:

```java
assertEquals(0, cuenta2.getSaldo().compareTo(new BigDecimal("1000.8989")));
```

`compareTo()` devuelve `0` si los valores numéricos son iguales, incluso si su escala decimal es distinta.

Esta prueba no registra las cuentas en la lista porque el método `transferir()` actual no exige que estén registradas.

## 7. Probar la relación entre banco y cuentas

```java
@Test
void testRelacionBancoCuentas() {
    Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
    Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

    Banco banco = new Banco();
    banco.addCuenta(cuenta1);
    banco.addCuenta(cuenta2);
    banco.setNombre("Banco del estado");

    banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));

    assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
    assertEquals("3000", cuenta1.getSaldo().toPlainString());
    assertEquals(2, banco.getCuentas().size());
    assertEquals("Banco del estado", cuenta1.getBanco().getNombre());
}
```

- `addCuenta(...)`: registra la cuenta y establece su banco.
- `getCuentas().size()`: obtiene cuántas cuentas hay en la lista; esperamos `2`.
- `cuenta1.getBanco().getNombre()`: navega de la cuenta al banco y después obtiene su nombre.

## 8. Buscar cuentas con Stream API

### `filter()` y `findFirst()`

```java
assertEquals(
    "Andres",
    banco.getCuentas().stream()
        .filter(c -> c.getPersona().equals("Andres"))
        .findFirst()
        .get()
        .getPersona()
);
```

1. `getCuentas()` obtiene la lista.
2. `stream()` permite procesar sus elementos con Stream API.
3. `filter(...)` conserva los elementos que cumplen la condición.
4. `c -> ...` es una expresión lambda; `c` representa cada cuenta examinada.
5. `findFirst()` devuelve un `Optional<Cuenta>` con la primera coincidencia, si existe.
6. `get()` obtiene la cuenta del `Optional`, pero lanza `NoSuchElementException` si no hay coincidencia.
7. `getPersona()` obtiene el nombre y `assertEquals()` lo compara con `"Andres"`.

Para una comprobación más directa y segura cuando solo interesa saber si existe una coincidencia, puedes usar:

```java
assertTrue(
    banco.getCuentas().stream()
        .anyMatch(c -> c.getPersona().equals("Andres"))
);
```

`anyMatch()` devuelve `true` si al menos una cuenta cumple la condición. `assertTrue()` hace fallar la prueba si el resultado es `false`.

## 9. Consideración sobre `equals()` y `hashCode()`

La versión previa de `Cuenta` compara `persona` y `saldo`, pero no `banco`. Eso puede ser intencional: el banco no siempre forma parte de la identidad lógica de la cuenta. Incluir ambos lados de una relación bidireccional en `equals()` puede provocar comparaciones recursivas.

## 10. Resumen

| Elemento | Función |
|---|---|
| `List<Cuenta>` | Lista de objetos `Cuenta`. |
| `ArrayList<>` | Implementación de lista que puede cambiar de tamaño. |
| `this` | Referencia al objeto actual. |
| `addCuenta()` | Registra una cuenta y establece el banco asociado. |
| `transferir()` | Debita una cuenta y acredita otra. |
| `size()` | Devuelve el número de elementos. |
| `stream()` | Permite procesar una colección con Stream API. |
| `filter()` | Filtra elementos según una condición. |
| `findFirst()` | Devuelve un `Optional` con la primera coincidencia. |
| `get()` | Obtiene el valor del `Optional`; falla si está vacío. |
| `anyMatch()` | Comprueba si al menos un elemento cumple una condición. |
| `assertEquals()` | Compara el resultado real con el esperado. |
| `assertTrue()` | Comprueba que una condición sea verdadera. |

La idea central es que `Banco` administra varias cuentas y que `addCuenta()` mantiene sincronizada la relación entre ambos objetos. Los tests verifican tanto los saldos de la transferencia como la relación entre el banco y sus cuentas.

# Usando el método `assertAll()` en JUnit 5

## 1. ¿Qué hace `assertAll()`?

`assertAll()` agrupa varias aserciones y ejecuta todas las comprobaciones del grupo. Si algunas fallan, JUnit informa los fallos al terminar el grupo, en lugar de detenerse en la primera aserción fallida.

**Analogía:** es como revisar todas las preguntas de un examen y después informar cuáles están mal, en vez de detenerse en la primera respuesta incorrecta.

## 2. Estructura

```java
assertAll(
    () -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString()),
    () -> assertEquals("3000", cuenta1.getSaldo().toPlainString())
);
```

- `assertAll(...)`: agrupa las comprobaciones.
- `() ->`: expresión lambda sin parámetros. `->` separa los parámetros del código que se ejecuta.
- `assertEquals(...)`: compara el resultado esperado con el real.
- Cada lambda representa una comprobación independiente.

También puedes usar llaves cuando necesitas varias instrucciones dentro de una lambda.

## 3. Preparar la prueba

```java
Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

Banco banco = new Banco();
banco.addCuenta(cuenta1);
banco.addCuenta(cuenta2);
banco.setNombre("Banco del estado");

banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));
```

La cuenta de Andres (`cuenta2`) es el origen y la de Jhon Doe (`cuenta1`) es el destino. Los saldos esperados son:
- Andres: `1500.8989 - 500 = 1000.8989`.
- Jhon Doe: `2500 + 500 = 3000`.

## 4. Prueba completa

```java
@Test
void testRelacionBancoCuentas() {
    Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
    Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

    Banco banco = new Banco();
    banco.addCuenta(cuenta1);
    banco.addCuenta(cuenta2);
    banco.setNombre("Banco del estado");
    banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));

    assertAll(
        () -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString()),
        () -> assertEquals("3000", cuenta1.getSaldo().toPlainString()),
        () -> assertEquals(2, banco.getCuentas().size()),
        () -> assertEquals("Banco del estado", cuenta1.getBanco().getNombre()),
        () -> assertEquals(
            "Andres",
            banco.getCuentas().stream()
                .filter(c -> c.getPersona().equals("Andres"))
                .findFirst()
                .get()
                .getPersona()
        ),
        () -> assertTrue(
            banco.getCuentas().stream()
                .anyMatch(c -> c.getPersona().equals("Jhon Doe"))
        )
    );
}
```

## 5. ¿Qué comprueba cada aserción?

1. El saldo de Andres quedó en `1000.8989`.
2. El saldo de Jhon Doe quedó en `3000`.
3. `banco.getCuentas().size()` devuelve la cantidad de cuentas registradas; se esperan `2`.
4. `cuenta1.getBanco().getNombre()` navega desde la cuenta al banco y obtiene su nombre.
5. `stream()` permite procesar la lista; `filter()` conserva las cuentas de Andres; `findFirst()` devuelve un `Optional` con la primera coincidencia; `get()` obtiene la cuenta, pero lanza `NoSuchElementException` si no hay coincidencia.
6. `anyMatch()` devuelve `true` si existe una cuenta de Jhon Doe; `assertTrue()` comprueba ese resultado.

En la quinta aserción, `findFirst().get()` presupone que la cuenta existe. Si solo quieres comprobar su existencia, puedes usar `anyMatch()` directamente.

## 6. ¿Qué pasa si varias aserciones fallan?

Sin `assertAll()`, una aserción fallida normalmente detiene el método de prueba en ese punto y las posteriores no se ejecutan.

Con `assertAll()`, JUnit ejecuta las comprobaciones del grupo y reúne los fallos de las aserciones para reportarlos juntos. Esto facilita diagnosticar varios problemas en una sola ejecución.

No obstante, `assertAll()` no revierte cambios que el código ya haya realizado, ni garantiza que una lambda termine si ocurre un error inesperado dentro de ella.

## 7. Import necesario

```java
import static org.junit.jupiter.api.Assertions.*;
```

El import estático permite llamar directamente a `assertAll()`, `assertEquals()` y `assertTrue()` sin escribir `Assertions.` delante.

## 8. Resumen

| Elemento | Función |
|---|---|
| `assertAll()` | Ejecuta un grupo de aserciones y reporta sus fallos juntos. |
| `assertEquals()` | Compara el resultado esperado con el real. |
| `assertTrue()` | Comprueba que una condición sea verdadera. |
| `() -> ...` | Lambda que representa una comprobación. |
| `stream()` | Permite procesar los elementos de una colección. |
| `filter()` | Filtra elementos según una condición. |
| `findFirst()` | Devuelve un `Optional` con la primera coincidencia. |
| `anyMatch()` | Comprueba si al menos un elemento cumple una condición. |

La idea principal: usa `assertAll()` cuando quieras validar varios resultados relacionados en una prueba y conocer todos los fallos del grupo en una sola ejecución.

# Agregando mensajes de falla en los métodos Assertions de JUnit 5

En JUnit 5 podemos agregar mensajes personalizados a las aserciones para entender mejor por qué una prueba falló. Esto es útil cuando tenemos muchas pruebas y necesitamos identificar rápidamente el problema.

Una analogía: una aserción es como un inspector de calidad. No solo queremos que indique que algo salió mal; también queremos que explique qué esperaba encontrar y qué encontró realmente.

## 1. Código de ejemplo

```java
@Test
void test() {
    Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));

    String esperado = "Andres";
    String real = cuenta.getPersona();

    assertNotNull(real, () -> "La cuenta no puede ser nula");

    assertEquals(esperado, real,
            () -> "El nombre de la cuenta no es el que se esperaba: se esperaba "
                    + esperado + ", sin embargo fue " + real);

    assertTrue(real.equals("Andres"),
            () -> "Nombre cuenta esperada debe ser igual a la real");
}
```

Este test comprueba que el nombre obtenido de la cuenta no sea nulo y que sea igual a `"Andres"`.

## 2. ¿Qué es un mensaje de falla?

Es un texto que JUnit muestra cuando una aserción no se cumple. Sirve para describir el problema con palabras claras, en lugar de obligarnos a deducirlo únicamente a partir de los valores.

| Aserción | ¿Qué comprueba? | Ejemplo de mensaje |
|---|---|---|
| `assertNotNull(real, mensaje)` | Que `real` no sea `null`. | `La cuenta no puede ser nula` |
| `assertEquals(esperado, real, mensaje)` | Que ambos valores sean iguales. | Indica el valor esperado y el obtenido. |
| `assertTrue(condicion, mensaje)` | Que la condición sea verdadera. | `Nombre cuenta esperada debe ser igual a la real` |

Si `esperado` vale `"Andres"` y `real` vale `"Pedro"`, la segunda aserción puede mostrar un mensaje similar a:

```text
El nombre de la cuenta no es el que se esperaba: se esperaba Andres, sin embargo fue Pedro
```

## 3. ¿Por qué se utiliza `() ->` antes del mensaje?

En el ejemplo se usa:

```java
assertNotNull(real, () -> "La cuenta no puede ser nula");
```

La expresión `() -> "La cuenta no puede ser nula"` es una **expresión lambda**. En este caso, proporciona el mensaje mediante un proveedor de texto (`Supplier<String>`).

- `()` significa que la lambda no recibe parámetros.
- `->` separa los parámetros del contenido que se devuelve.
- `"La cuenta no puede ser nula"` es el texto que se proporciona si la aserción falla.

JUnit también ofrece sobrecargas que reciben el mensaje directamente como `String`, por ejemplo:

```java
assertNotNull(real, "El nombre no debe ser nulo");
```

Usar una lambda para el mensaje permite que JUnit lo calcule solo si hace falta mostrarlo. Esto puede ser útil si construir el mensaje requiere trabajo adicional. Para un texto sencillo, ambas formas son válidas.

## 4. Explicación de cada aserción

### `assertNotNull()`

```java
assertNotNull(real, () -> "La cuenta no puede ser nula");
```

Comprueba que `real` no sea `null`.

**Detalle importante:** la variable `real` contiene el nombre de la persona, no el objeto `Cuenta`. Por eso, un mensaje más preciso sería:

```java
assertNotNull(real, () -> "El nombre de la cuenta no puede ser nulo");
```

Si quisieras comprobar que el objeto completo no sea nulo, escribirías `assertNotNull(cuenta, ...)`.

### `assertEquals()`

```java
assertEquals(esperado, real,
        () -> "El nombre esperado era " + esperado
                + ", pero se obtuvo " + real);
```

Compara el valor esperado con el valor real. Si no coinciden, JUnit informa el fallo y muestra el mensaje personalizado.

En este caso:
- `esperado`: lo que debería devolver el programa.
- `real`: lo que devolvió el programa.
- La lambda: la explicación adicional del fallo.

Conviene incluir separadores y puntuación en el texto para que el mensaje sea fácil de leer.

### `assertTrue()`

```java
assertTrue(real.equals("Andres"),
        () -> "El nombre de la cuenta debe ser Andres");
```

Comprueba que la expresión dentro de `assertTrue()` sea verdadera. Si devuelve `false`, se muestra el mensaje.

Sin embargo, esta comprobación es redundante en este ejemplo: `assertEquals(esperado, real, ...)` ya verifica que `real` sea igual a `"Andres"`, porque `esperado` contiene ese valor. En una prueba real normalmente basta con una de las dos comprobaciones.

## 5. Versión recomendada del test

Podemos dejar la prueba más clara usando una comprobación para el valor nulo y otra para la igualdad:

```java
@Test
void test() {
    Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));

    String esperado = "Andres";
    String real = cuenta.getPersona();

    assertNotNull(real, () -> "El nombre de la cuenta no puede ser nulo");
    assertEquals(esperado, real,
            () -> "El nombre esperado era " + esperado
                    + ", pero se obtuvo " + real);
}
```

Esta versión evita comprobar dos veces la misma igualdad y utiliza mensajes más específicos.

## 6. Importaciones necesarias

Si usas las aserciones sin escribir `Assertions.` delante de cada método, normalmente tendrás una importación estática como esta:

```java
import static org.junit.jupiter.api.Assertions.*;
```

También necesitas las clases usadas en el ejemplo, por ejemplo:

```java
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
```

Asegúrate de tener disponible la clase `Cuenta` en el paquete correspondiente.

## Resumen

- Un mensaje personalizado explica por qué falló una aserción.
- `assertNotNull()` verifica que un valor no sea `null`.
- `assertEquals()` compara el valor esperado con el real.
- `assertTrue()` verifica que una condición sea verdadera.
- `() -> "mensaje"` es una lambda que proporciona el texto de forma diferida.
- Usa mensajes concretos y evita aserciones duplicadas cuando comprueban exactamente lo mismo.

# Usando las anotaciones `@DisplayName` y `@Disabled` en JUnit 5

Estas anotaciones ayudan a organizar las pruebas y controlar cuáles se ejecutan.

Imagina que tienes una lista de pruebas para tu aplicación bancaria. En vez de ver solamente nombres técnicos, puedes mostrar una descripción clara. También puedes deshabilitar temporalmente una prueba mientras trabajas en ella.

## 1. Código de ejemplo

```java
@Test
@DisplayName("Probando relaciones entre las cuentas y el banco con assertAll")
@Disabled
void testRelacionBancoCuentas() {
    fail();
}
```

## 2. ¿Qué hace cada anotación o método?

| Elemento | Función |
|---|---|
| `@Test` | Indica que el método es una prueba de JUnit 5. |
| `@DisplayName` | Define un nombre descriptivo que se muestra en los resultados de las pruebas. |
| `@Disabled` | Deshabilita la prueba para que JUnit no la ejecute normalmente. |
| `fail()` | Hace que la prueba falle intencionalmente si llega a ejecutarse. |

## 3. `@DisplayName`

Permite asignar un nombre legible a una prueba, en lugar de depender únicamente del nombre del método.

```java
@Test
@DisplayName("Debe relacionar correctamente las cuentas con el banco")
void testRelacionBancoCuentas() {
    // Aquí van las comprobaciones de la prueba.
}
```

En el reporte de JUnit o del IDE se mostrará la descripción configurada en `@DisplayName`. El nombre del método sigue siendo `testRelacionBancoCuentas` en el código Java.

**¿Para qué sirve?**

- Hace que los reportes sean más fáciles de entender.
- Describe el comportamiento que se está probando.
- Ayuda a localizar rápidamente una prueba cuando falla.

Puedes escribir espacios, acentos y frases completas dentro de la descripción.

## 4. `@Disabled`

Esta anotación indica que una prueba debe omitirse durante la ejecución normal de JUnit.

```java
@Test
@DisplayName("Debe relacionar correctamente las cuentas con el banco")
@Disabled
void testRelacionBancoCuentas() {
    // JUnit omitirá esta prueba.
}
```

Es útil cuando una prueba está temporalmente en desarrollo o depende de una funcionalidad que todavía no está lista.

También puedes indicar una razón:

```java
@Test
@DisplayName("Debe relacionar correctamente las cuentas con el banco")
@Disabled("Pendiente de completar las comprobaciones")
void testRelacionBancoCuentas() {
    // Prueba pendiente.
}
```

La razón es opcional, pero ayuda a que el equipo entienda por qué la prueba está deshabilitada.

**Importante:** no conviene dejar pruebas deshabilitadas permanentemente sin una razón clara. Cuando la funcionalidad esté lista, revisa la prueba y quita `@Disabled` para que vuelva a ejecutarse.

## 5. ¿Qué hace `fail()`?

`fail()` es una aserción de JUnit que provoca un fallo inmediato si el programa llega a esa línea.

```java
@Test
void pruebaQueFallaIntencionalmente() {
    fail("Esta prueba falla intencionalmente");
}
```

Esta prueba falla y muestra el mensaje indicado. Puede servir para comprobar cómo se reportan los fallos o para marcar temporalmente una prueba que todavía no está implementada.

En el ejemplo original, sin embargo, la prueba tiene `@Disabled`. Por eso, JUnit normalmente la omitirá y no llegará a ejecutar `fail()`.

## 6. ¿Qué sucede al ejecutar la prueba original?

```java
@Test
@DisplayName("Probando relaciones entre las cuentas y el banco con assertAll")
@Disabled
void testRelacionBancoCuentas() {
    fail();
}
```

El resultado esperado es:

1. JUnit reconoce el método como una prueba gracias a `@Test`.
2. Usa el texto de `@DisplayName` como nombre descriptivo.
3. Debido a `@Disabled`, omite la prueba.
4. Como la prueba se omite, no llega a ejecutarse `fail()`.

Por tanto, **la prueba aparece como deshabilitada u omitida, no como una prueba fallida**.

## 7. Importaciones

Con JUnit 5, las importaciones pueden ser:

```java
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;
```

Si ya utilizas `import static org.junit.jupiter.api.Assertions.*;`, no necesitas agregar por separado la importación estática de `fail()`.

## Resumen

- `@Test`: identifica un método de prueba.
- `@DisplayName("...")`: le asigna una descripción legible.
- `@Disabled`: evita que la prueba se ejecute normalmente.
- `fail()`: fuerza un fallo si se ejecuta.
- En el ejemplo original, `@Disabled` impide que se alcance `fail()`.

# Ciclo de vida en JUnit 5: `@BeforeEach`, `@AfterEach`, `@BeforeAll` y `@AfterAll`

En JUnit 5, las anotaciones del ciclo de vida permiten ejecutar código automáticamente antes y después de las pruebas. Sirven para preparar objetos, inicializar recursos y limpiar lo que se utilizó.

Imagina que vas a probar una cuenta bancaria varias veces:
- Antes de cada prueba, creas una cuenta con un saldo inicial.
- Ejecutas la prueba.
- Después de cada prueba, limpias los recursos utilizados si hace falta.
- Cuando termina todo el grupo, cierras los recursos compartidos.

## 1. ¿Qué hace cada anotación?

| Anotación | ¿Cuándo se ejecuta? | Uso habitual |
|---|---|---|
| `@BeforeEach` | Antes de cada método de prueba. | Crear objetos y preparar datos. |
| `@AfterEach` | Después de cada método de prueba. | Limpiar o restablecer recursos. |
| `@BeforeAll` | Una vez antes de las pruebas de la clase. | Preparar recursos compartidos. |
| `@AfterAll` | Una vez después de las pruebas de la clase. | Liberar recursos compartidos. |

`Each` significa “cada una”; `All` significa “todas”.

## 2. Ejemplo completo

```java
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CuentaTest {
    private Cuenta cuenta;

    @BeforeAll
    static void iniciarPruebas() {
        System.out.println("Preparación general");
    }

    @BeforeEach
    void prepararCuenta() {
        cuenta = new Cuenta("Andres", new BigDecimal("1000.00"));
        System.out.println("Se creó una cuenta para esta prueba");
    }

    @Test
    void comprobarNombre() {
        assertEquals("Andres", cuenta.getPersona());
    }

    @Test
    void comprobarSaldoInicial() {
        assertEquals(0, cuenta.getSaldo().compareTo(new BigDecimal("1000.00")));
    }

    @AfterEach
    void limpiarPrueba() {
        System.out.println("Terminó esta prueba");
    }

    @AfterAll
    static void finalizarPruebas() {
        System.out.println("Finalizaron todas las pruebas");
    }
}
```

El ejemplo utiliza la clase `Cuenta` vista en los apuntes anteriores. Agrega su importación si se encuentra en otro paquete.

## 3. `@BeforeEach`: preparar cada prueba

```java
@BeforeEach
void prepararCuenta() {
    cuenta = new Cuenta("Andres", new BigDecimal("1000.00"));
}
```

Se ejecuta antes de cada método `@Test`. Si tienes tres pruebas, se ejecutará tres veces y creará una cuenta nueva para cada una.

**Analogía:** es como entregar una hoja nueva a cada estudiante antes de un examen. Cada prueba empieza con sus propios datos y no depende de lo que haya cambiado otra.

## 4. `@AfterEach`: limpiar después de cada prueba

```java
@AfterEach
void limpiarPrueba() {
    System.out.println("Terminó esta prueba");
}
```

Se ejecuta después de cada prueba, incluso si una aserción falla. Puede servir para cerrar recursos, borrar archivos temporales o restablecer datos.

No es obligatorio tener este método si no hay nada que limpiar.

## 5. `@BeforeAll`: preparar una sola vez

```java
@BeforeAll
static void iniciarPruebas() {
    System.out.println("Preparación general");
}
```

Se ejecuta una vez antes de las pruebas de la clase. Puede preparar un recurso costoso que se compartirá, como una infraestructura de prueba.

Por defecto, el método debe ser `static`. JUnit ejecuta esta preparación antes de usar la instancia individual de prueba. Puede evitarse la restricción de `static` configurando `@TestInstance(TestInstance.Lifecycle.PER_CLASS)`.

No lo uses para datos que deban reiniciarse en cada prueba; para eso suele ser mejor `@BeforeEach`.

## 6. `@AfterAll`: finalizar una sola vez

```java
@AfterAll
static void finalizarPruebas() {
    System.out.println("Finalizaron todas las pruebas");
}
```

Se ejecuta una vez al terminar las pruebas de la clase. Se utiliza para cerrar o liberar recursos compartidos preparados con `@BeforeAll`.

Por defecto también debe ser `static`, salvo que se configure el ciclo de vida `PER_CLASS`.

## 7. Orden de ejecución

Con dos pruebas, el ciclo habitual es:

```text
@BeforeAll
    |
@BeforeEach
    |
@Test prueba 1
    |
@AfterEach
    |
@BeforeEach
    |
@Test prueba 2
    |
@AfterEach
    |
@AfterAll
```

JUnit no garantiza un orden fijo entre los métodos `@Test` por defecto. Lo importante es que la preparación y la limpieza rodeen cada prueba.

## 8. ¿Por qué se usa `compareTo()` para `BigDecimal`?

```java
assertEquals(0, cuenta.getSaldo().compareTo(new BigDecimal("1000.00")));
```

`compareTo()` devuelve un número negativo si el valor es menor, `0` si es numéricamente igual y uno positivo si es mayor. Por eso permite comparar `1000.0` con `1000.00` sin que la escala decimal cause una diferencia. En cambio, `BigDecimal.equals()` distingue esas escalas.

## 9. Buenas prácticas

- Usa `@BeforeEach` para preparar datos independientes en cada prueba.
- Usa `@BeforeAll` y `@AfterAll` para recursos compartidos.
- Recuerda que `@BeforeAll` y `@AfterAll` normalmente deben ser `static`.
- Evita que una prueba dependa de que otra se ejecute primero.
- No agregues métodos de limpieza si no hay nada que limpiar.

## Resumen

- `@BeforeEach`: prepara cada prueba.
- `@AfterEach`: limpia después de cada prueba.
- `@BeforeAll`: prepara una vez antes de todas las pruebas de la clase.
- `@AfterAll`: limpia una vez después de todas las pruebas de la clase.

# Tests condicionales en JUnit 5

JUnit 5 permite ejecutar u omitir pruebas según el sistema operativo, la versión de Java o las propiedades del entorno.

## 1. Anotaciones principales

| Anotación | Función |
|---|---|
| `@EnabledOnOs` | Ejecuta solo en los sistemas operativos indicados. |
| `@DisabledOnOs` | Omite la prueba en los sistemas operativos indicados. |
| `@EnabledOnJre` | Ejecuta solo en las versiones de Java indicadas. |
| `@EnabledIfSystemProperty` | Ejecuta si una propiedad de la JVM coincide con una condición. |
| `@EnabledIfEnvironmentVariable` | Ejecuta si una variable de entorno coincide con una condición. |

Si la condición no se cumple, JUnit omite la prueba; no significa que haya fallado.

## 2. Condiciones por sistema operativo

```java
@Test
@EnabledOnOs(OS.WINDOWS)
void testSoloWindows() {
    // Solo se ejecuta en Windows.
}

@Test
@EnabledOnOs({OS.LINUX, OS.MAC})
void testSoloLinuxMac() {
    // Se ejecuta en Linux o macOS.
}

@Test
@DisabledOnOs(OS.WINDOWS)
void testNoWindows() {
    // Se omite en Windows; se habilita en otros sistemas.
}
```

`OS` es un enum de JUnit con constantes que representan sistemas operativos. Las llaves `{ ... }` permiten indicar varios sistemas.

## 3. Condiciones por versión de Java

```java
@Test
@EnabledOnJre(JRE.JAVA_8)
void soloJdk8() {
    // Solo se ejecuta con Java 8.
}

@Test
@EnabledOnJre(JRE.JAVA_15)
void soloJava15() {
    // Solo se ejecuta con Java 15.
}
```

`@EnabledOnJre` habilita la prueba solo en las versiones especificadas. Comprueba qué versiones reconoce la versión de JUnit que utiliza tu proyecto.

Nota: JDK incluye herramientas para desarrollar; JRE es el nombre que JUnit usa en esta anotación para representar versiones del entorno Java.

## 4. Imprimir las propiedades del sistema

```java
@Test
void imprimirSystemProperties() {
    Properties properties = System.getProperties();
    properties.forEach((k, v) -> System.out.println(k + " : " + v));
}
```

- `Properties`: clase que almacena pares de clave y valor.
- `System.getProperties()`: obtiene propiedades de la JVM, como `java.version`, `os.name` y `user.dir`.
- `forEach(...)`: recorre las entradas.
- `(k, v) -> ...`: lambda; `k` es la clave y `v` el valor.
- `System.out.println(...)`: imprime cada par.

Importación necesaria:

```java
import java.util.Properties;
```

## 5. Condición según `java.version`

```java
@Test
@EnabledIfSystemProperty(named = "java.version", matches = "21\\..*")
void testJavaVersion() {
    // Se ejecuta si la propiedad coincide con la expresión regular.
}
```

- `named = "java.version"` indica la propiedad que se consulta.
- `matches` recibe una expresión regular.
- `"21\\..*"` en el archivo Java representa una expresión regular que busca un texto que empiece con `21.`; `\\.` en esta documentación corresponde al escape de la barra invertida dentro de la cadena Java.

El punto `.` tiene significado especial en expresiones regulares; para buscar un punto literal se escapa. Ajusta la expresión al formato real que devuelva `java.version`.

## 6. Condición según la propiedad `ENV`

```java
@Test
@EnabledIfSystemProperty(named = "ENV", matches = "dev")
void testDev() {
    // Se ejecuta si la propiedad de la JVM ENV vale dev.
}
```

`@EnabledIfSystemProperty` evalúa una propiedad de Java, no una variable de entorno. Puedes definirla al iniciar la JVM con `-DENV=dev`; por ejemplo, con Maven:

```bash
mvn test -DENV=dev
```

La propiedad debe llegar al proceso de Java que ejecuta las pruebas, según la configuración del proyecto.

### Si `ENV` es una variable de entorno

Usa esta anotación:

```java
@Test
@EnabledIfEnvironmentVariable(named = "ENV", matches = "dev")
void testDevPorVariableDeEntorno() {
    // Se ejecuta si la variable de entorno ENV coincide con dev.
}
```

Diferencia:
- `System.getProperty("ENV")`: propiedad de la JVM, comúnmente configurada con `-DENV=dev`.
- `System.getenv("ENV")`: variable de entorno del sistema operativo.

## 7. Importaciones

```java
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.condition.JRE;
```

Importa solo las anotaciones que realmente uses.

## Resumen

- `@EnabledOnOs`: habilita pruebas para ciertos sistemas operativos.
- `@DisabledOnOs`: deshabilita pruebas para ciertos sistemas operativos.
- `@EnabledOnJre`: habilita pruebas para versiones concretas de Java.
- `@EnabledIfSystemProperty`: evalúa propiedades de la JVM.
- `@EnabledIfEnvironmentVariable`: evalúa variables de entorno.
- Una condición falsa hace que la prueba se omita, no que falle.

# Tests condicionales con `@EnabledIfEnvironmentVariable` y `Assumptions` en JUnit 5

JUnit 5 permite habilitar pruebas según variables de entorno y también abortarlas o ejecutar bloques opcionales con `Assumptions`.

## 1. Consultar variables de entorno

```java
@Test
void imprimirVariablesAmbiente() {
    Map<String, String> getenv = System.getenv();
    getenv.forEach((k, v) -> System.out.println(k + " = " + v));
}
```

- `Map<String, String>` es una interfaz que representa pares de clave y valor.
- `System.getenv()` devuelve las variables de entorno visibles para el proceso Java.
- `forEach()` recorre el mapa.
- `(k, v) -> ...` es una lambda: `k` representa la clave y `v` el valor.
- `System.out.println()` imprime el par en consola.

**Precaución:** las variables pueden contener contraseñas o tokens. Evita imprimirlas todas en logs compartidos.

## 2. `@EnabledIfEnvironmentVariable`

```java
@Test
@EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-21.*")
void testJavaHome() {
    // Solo se ejecuta si JAVA_HOME contiene jdk-21.
}

@Test
@EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "6")
void testProcesadores() {
    // Solo se ejecuta si la variable vale exactamente 6.
}
```

- `named` indica el nombre de la variable.
- `matches` contiene una expresión regular.
- `.*` significa cualquier secuencia de caracteres.
- Si la variable no existe o no coincide, JUnit omite la prueba.

En el ejemplo original aparecía `".*jdk-21*"`: `1*` significa cero o más caracteres `1`, **no** cualquier texto después de `21`. Para buscar la subcadena `jdk-21`, usa `".*jdk-21.*"`.

`NUMBER_OF_PROCESSORS` es una variable habitual de Windows; la anotación no detecta directamente los procesadores. `JAVA_HOME` tampoco garantiza que esa sea la versión de Java con la que se ejecuta JUnit.

## 3. Diferencia entre variable de entorno y propiedad de Java

```java
System.getenv("ENV");       // Variable de entorno del sistema operativo.
System.getProperty("ENV"); // Propiedad de la JVM.
```

- `@EnabledIfEnvironmentVariable` consulta variables de entorno.
- `@EnabledIfSystemProperty` consulta propiedades de la JVM.
- La opción `-DENV=dev` configura una **propiedad de la JVM**, no una variable de entorno.

En el código original se dice que `ENV=dev` es una variable de ambiente, pero se consulta con `System.getProperty("ENV")`. Debes elegir una de las dos opciones y usarla de forma consistente.

## 4. `assumeTrue()`

```java
@Test
void testSaldoCuentaDev() {
    boolean esDev = "dev".equals(System.getenv("ENV"));
    assumeTrue(esDev);

    assertNotNull(cuenta.getSaldo());
    assertEquals(0, cuenta.getSaldo().compareTo(new BigDecimal("1000.12345")));
}
```

- `boolean esDev` guarda si el entorno se llama `dev`.
- `"dev".equals(...)` compara textos sin lanzar una excepción si el valor obtenido es `null`.
- `assumeTrue(esDev)` permite continuar cuando la condición es verdadera. Si es falsa, **aborta** la prueba y JUnit la registra como abortada, no como fallida.
- `assertNotNull()` verifica que el saldo exista.
- `assertEquals()` verifica que `compareTo()` devuelva cero, es decir, igualdad numérica.

Para que funcione, `cuenta` debe estar inicializada, por ejemplo en un método `@BeforeEach`.

## 5. `assumingThat()`

```java
@Test
void testSaldoConBloqueOpcional() {
    boolean esDev = "dev".equals(System.getenv("ENV"));

    assumingThat(esDev, () -> {
        assertNotNull(cuenta.getSaldo());
        assertEquals(0, cuenta.getSaldo().compareTo(new BigDecimal("1000.12345")));
    });

    assertNotNull(cuenta.getPersona()); // Se ejecuta también si esDev es false.
}
```

`assumingThat(condicion, () -> { ... })` ejecuta **solo el bloque** cuando la condición es verdadera. Si es falsa, omite ese bloque, pero **la prueba continúa** con las instrucciones posteriores.

Diferencia esencial:

| Método | Si la condición es falsa |
|---|---|
| `assumeTrue(esDev)` | Aborta el resto de la prueba. |
| `assumingThat(esDev, lambda)` | Omite la lambda y continúa la prueba. |

## 6. ¿Qué pasa al combinarlos como en el código original?

```java
boolean esDev = "dev".equals(System.getProperty("ENV"));
assumeTrue(esDev);
assumingThat(esDev, () -> {
    assertNotNull(cuenta.getSaldo());
});
```

Si `esDev` es falso, `assumeTrue()` aborta la prueba y nunca se llega a `assumingThat()`. Si es verdadero, el bloque se ejecuta. Por tanto, **usar ambos con la misma condición es redundante**: elige `assumeTrue` para abortar toda la prueba o `assumingThat` para ejecutar un bloque opcional.

## 7. Ejemplo completo recomendado

```java
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class CuentaTest {
    private Cuenta cuenta;

    @BeforeEach
    void prepararCuenta() {
        cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
    }

    @Test
    @DisplayName("El saldo de la cuenta es correcto en desarrollo")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getenv("ENV"));
        assumeTrue(esDev, "Esta prueba requiere ENV=dev");

        assertNotNull(cuenta.getSaldo());
        assertEquals(0, cuenta.getSaldo().compareTo(new BigDecimal("1000.12345")));
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
    }
}
```

Aquí `Cuenta` es la clase del proyecto bancario. Si está en otro paquete, importa la clase correspondiente. `BigDecimal.compareTo()` evita convertir el saldo a `double`, lo cual es recomendable al trabajar con dinero.

## 8. Configurar `ENV` para probar

**PowerShell (sesión actual):**

```powershell
$env:ENV = "dev"
mvn test
```

**CMD (sesión actual):**

```cmd
set ENV=dev
mvn test
```

**Linux/macOS (terminal):**

```bash
ENV=dev mvn test
```

Si en lugar de una variable de entorno quieres utilizar una propiedad de la JVM, consulta `System.getProperty("ENV")` y configura la propiedad con `-DENV=dev` en el proceso Java que ejecuta las pruebas.

## Resumen

- `@EnabledIfEnvironmentVariable`: habilita una prueba según una variable de entorno.
- `System.getenv()`: consulta variables de entorno.
- `System.getProperty()`: consulta propiedades de la JVM.
- `assumeTrue()`: aborta una prueba cuando la condición es falsa.
- `assumingThat()`: ejecuta un bloque solo cuando la condición es verdadera, sin abortar el resto.
- No es necesario combinar `assumeTrue()` y `assumingThat()` con la misma condición.

# Clases test anidadas usando `@Nested` en JUnit 5

`@Nested` permite agrupar métodos de prueba dentro de clases internas para organizar los tests por contexto. Es como crear carpetas dentro de una carpeta principal: cada grupo contiene pruebas relacionadas.

## 1. Estructura básica

```java
class CuentaTest {
    @Nested
    class SistemaOperativoTest {
        // Pruebas relacionadas con sistemas operativos
    }
}
```

- `CuentaTest` es la clase de pruebas principal.
- `SistemaOperativoTest` es una clase interna **no estática**.
- `@Nested` indica a JUnit Jupiter que descubra y ejecute las pruebas contenidas en esa clase interna.

## 2. Ejemplo completo

```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

class CuentaTest {

    @Nested
    @DisplayName("Pruebas según el sistema operativo")
    class SistemaOperativoTest {

        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
            // Comprobaciones exclusivas de Windows
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {
            // Comprobaciones para Linux o macOS
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
            // Comprobaciones para sistemas distintos de Windows
        }
    }
}
```

### ¿Qué hace cada elemento?

- `@Test`: identifica un método de prueba.
- `@Nested`: agrupa pruebas dentro de una clase interna.
- `@DisplayName`: asigna un nombre legible al grupo en el reporte.
- `@EnabledOnOs`: habilita la prueba solo en los sistemas indicados.
- `@DisabledOnOs`: deshabilita la prueba en los sistemas indicados.
- `OS`: enum con constantes como `WINDOWS`, `LINUX` y `MAC`.

Los cuerpos de los métodos están vacíos solo para ilustrar la organización. En pruebas reales deben incluir acciones y aserciones.

## 3. ¿Cómo se muestran los resultados?

Si ejecutas el ejemplo en Windows, un reporte puede mostrar:

```text
CuentaTest
└── Pruebas según el sistema operativo
    ├── testSoloWindows()   [ejecutado]
    ├── testSoloLinuxMac()  [omitido]
    └── testNoWindows()     [omitido]
```

`@Nested` no cambia las condiciones de ejecución; solamente organiza las pruebas.

## 4. Agrupar pruebas por diferentes contextos

También puedes organizar pruebas bancarias por funcionalidad:

```java
class CuentaTest {

    @Nested
    @DisplayName("Pruebas del saldo")
    class SaldoTest {
        @Test
        void saldoInicial() {
            // Verificar saldo inicial
        }
    }

    @Nested
    @DisplayName("Pruebas de transferencias")
    class TransferenciasTest {
        @Test
        void transferirDinero() {
            // Verificar transferencia
        }
    }
}
```

Así separas los tests por comportamiento sin necesidad de crear una clase de nivel superior para cada grupo.

## 5. Relación con el ciclo de vida

Las clases anidadas pueden utilizar `@BeforeEach` y `@AfterEach`. En una prueba anidada, JUnit también ejecuta los métodos de preparación y limpieza aplicables de las clases contenedoras. Esto permite compartir una configuración general y agregar preparación específica en cada grupo.

Ejemplo:

```java
class CuentaTest {
    @BeforeEach
    void prepararGeneral() {
        System.out.println("Preparación general");
    }

    @Nested
    class SaldoTest {
        @BeforeEach
        void prepararSaldo() {
            System.out.println("Preparación del saldo");
        }

        @Test
        void probarSaldo() {
            System.out.println("Prueba del saldo");
        }
    }
}
```

En ese ejemplo, antes de `probarSaldo()` se ejecuta primero `prepararGeneral()` y luego `prepararSaldo()`.

## 6. Buenas prácticas

- Agrupa pruebas que compartan un mismo contexto o comportamiento.
- Usa `@DisplayName` para nombrar claramente cada grupo.
- Evita crear niveles de anidamiento innecesarios.
- Recuerda que `@Nested` se utiliza en clases internas no estáticas.
- Las pruebas deben tener aserciones útiles; un método `@Test` vacío puede pasar sin verificar nada.

## Resumen

`@Nested` organiza los tests por grupos dentro de una clase principal. `@DisplayName` ayuda a identificar esos grupos en el reporte, y las anotaciones condicionales siguen funcionando dentro de las clases anidadas.

# Repitiendo pruebas con `@RepeatedTest` en JUnit 5

`@RepeatedTest` permite ejecutar el mismo método de prueba varias veces. Es útil para comprobar que un comportamiento sigue funcionando en ejecuciones repetidas.

## 1. Ejemplo

```java
@DisplayName("Prueba debito")
@RepeatedTest(value = 5,
    name = "{displayName} - Repeticion numero {currentRepetition} de {totalRepetitions}")
void testDebitoCuentaRepetir(RepetitionInfo info) {
    if (info.getCurrentRepetition() == 3) {
        System.out.println("estamos en la repeticion " + info.getCurrentRepetition());
    }

    cuenta.debito(new BigDecimal(100));
    assertNotNull(cuenta.getSaldo());
    assertEquals(900, cuenta.getSaldo().intValue());
    assertEquals("900.12345", cuenta.getSaldo().toPlainString());
}
```

## 2. Explicación de cada elemento

- `@RepeatedTest`: marca un método como prueba repetida; no necesita `@Test` adicional.
- `value = 5`: ejecuta cinco repeticiones.
- `name`: configura el nombre que aparece en los resultados.
- `{displayName}`: inserta el texto de `@DisplayName`.
- `{currentRepetition}`: inserta el número de repetición actual, de 1 a 5.
- `{totalRepetitions}`: inserta el número total de repeticiones.
- `RepetitionInfo`: interfaz de JUnit que proporciona información sobre la repetición.
- `info.getCurrentRepetition()`: obtiene el número de repetición actual.
- `cuenta.debito(...)`: método de la clase `Cuenta` que resta dinero del saldo.
- `assertNotNull(...)`: comprueba que el saldo no sea nulo.
- `assertEquals(...)`: compara el valor esperado con el real.
- `intValue()`: convierte el saldo a entero, descartando la parte decimal; no sirve para verificar centavos.
- `toPlainString()`: convierte el `BigDecimal` a texto sin notación científica y conserva sus decimales.

## 3. ¿Qué ocurre en la repetición 3?

```java
if (info.getCurrentRepetition() == 3) {
    System.out.println("estamos en la repeticion 3");
}
```

El mensaje se imprime solo durante la tercera repetición. La operación de débito y las aserciones se ejecutan en las cinco.

## 4. ¿Se descuenta dinero cinco veces de la misma cuenta?

**No necesariamente.** Con el ciclo de vida predeterminado de JUnit 5 (`PER_METHOD`), se crea una nueva instancia de la clase de prueba para cada repetición. Además, `@BeforeEach` se ejecuta antes de cada repetición. Si ahí inicializas `cuenta` con `1000.12345`, cada repetición parte de ese saldo y termina en `900.12345`.

```java
private Cuenta cuenta;

@BeforeEach
void prepararCuenta() {
    cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
}
```

Si compartes la misma cuenta entre repeticiones mediante un campo `static`, un recurso compartido o una configuración de ciclo de vida distinta, el saldo puede acumular los débitos y las pruebas dejarán de comportarse como en el ejemplo. Evita compartir estado mutable cuando las pruebas deban ser independientes.

## 5. Versión recomendada

Para comprobar dinero, evita convertir `BigDecimal` a `int` porque se pierden decimales. Puedes comparar el valor numérico con `compareTo()`:

```java
@DisplayName("Prueba débito")
@RepeatedTest(value = 5,
    name = "{displayName} - Repetición {currentRepetition} de {totalRepetitions}")
void testDebitoCuentaRepetir(RepetitionInfo info) {
    cuenta.debito(new BigDecimal("100"));

    assertNotNull(cuenta.getSaldo());
    assertEquals(0,
        cuenta.getSaldo().compareTo(new BigDecimal("900.12345")));
}
```

`BigDecimal.compareTo()` devuelve `0` cuando los valores son numéricamente iguales, aunque tengan distinta escala decimal. Si necesitas comprobar también la representación decimal exacta, `toPlainString()` puede ser apropiado.

## 6. Importaciones

```java
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import static org.junit.jupiter.api.Assertions.*;
```

## Resumen

`@RepeatedTest(5)` repite la prueba cinco veces. `RepetitionInfo` informa en cuál repetición estamos. `@DisplayName` y los marcadores del atributo `name` permiten personalizar los nombres. Usa `@BeforeEach` para preparar una cuenta limpia en cada repetición y evitar dependencias entre pruebas.

# Pruebas parametrizadas con `@ParameterizedTest` y `@ValueSource` en JUnit 5

Las pruebas parametrizadas permiten ejecutar un mismo método de prueba con diferentes datos. Imagina un cajero automático: queremos verificar retiros de $100, $200, $300 y otros montos sin copiar el test varias veces.

## 1. Código original

```java
@ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
@ValueSource(strings = {"100", "200", "300", "500", "700", "1000.1234"})
void testDebitoCuenta(String monto) {
    cuenta.debito(new BigDecimal(monto));
    assertNotNull(cuenta.getSaldo());
    assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
}
```

## 2. ¿Qué hace cada elemento?

- `@ParameterizedTest`: identifica una prueba que recibe argumentos y se ejecuta una vez por cada conjunto de datos.
- `@ValueSource`: proporciona valores individuales del mismo tipo. En este caso `strings` entrega seis cadenas.
- `String monto`: parámetro que recibe uno de los valores en cada ejecución.
- `new BigDecimal(monto)`: convierte el texto a un número decimal exacto, útil para dinero.
- `cuenta.debito(...)`: descuenta el monto del saldo de la cuenta.
- `assertNotNull(...)`: comprueba que el saldo no sea `null`.
- `BigDecimal.ZERO`: representa el cero decimal.
- `compareTo(BigDecimal.ZERO)`: devuelve positivo si el saldo es mayor que cero, cero si es igual y negativo si es menor.
- `assertTrue(... > 0)`: verifica que el saldo resultante sea estrictamente positivo.

## 3. ¿Qué significa `name`?

```java
@ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
```

- `{index}`: número de invocación, comenzando en 1.
- `{0}`: valor del primer argumento de esa invocación.
- `{argumentsWithNames}`: representación de los argumentos con sus nombres cuando están disponibles.

Así es más fácil identificar qué dato provocó un fallo.

## 4. Resultado de los seis casos

Si `@BeforeEach` crea una cuenta nueva con saldo `1000.12345` antes de cada invocación, los resultados serían:

| Monto | Saldo final | ¿Es mayor que cero? |
|---:|---:|---|
| 100 | 900.12345 | Sí |
| 200 | 800.12345 | Sí |
| 300 | 700.12345 | Sí |
| 500 | 500.12345 | Sí |
| 700 | 300.12345 | Sí |
| 1000.1234 | 0.00005 | Sí |

**Importante:** cada invocación debe comenzar con el saldo inicial esperado. Por ejemplo:

```java
private Cuenta cuenta;

@BeforeEach
void prepararCuenta() {
    cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
}
```

JUnit ejecuta `@BeforeEach` antes de cada invocación de la prueba parametrizada.

## 5. ¿Qué verifica realmente la prueba?

La prueba comprueba que, después del débito, el saldo no sea nulo y sea **mayor que cero**. No comprueba directamente que el monto descontado sea exactamente el correcto.

Una versión más completa calcula el saldo esperado:

```java
@ParameterizedTest(name = "Débito {index}: monto {0}")
@ValueSource(strings = {"100", "200", "300", "500", "700", "1000.1234"})
void testDebitoCuenta(String monto) {
    BigDecimal saldoInicial = new BigDecimal("1000.12345");
    BigDecimal debito = new BigDecimal(monto);
    BigDecimal saldoEsperado = saldoInicial.subtract(debito);

    cuenta.debito(debito);

    assertNotNull(cuenta.getSaldo());
    assertEquals(0, saldoEsperado.compareTo(cuenta.getSaldo()));
}
```

`subtract()` es un método de `BigDecimal` que devuelve la resta sin modificar el objeto original. `assertEquals(0, ...compareTo(...))` verifica la igualdad numérica, incluso si cambia la escala decimal.

## 6. Diferencia con `@RepeatedTest`

- `@RepeatedTest(6)` repite el test seis veces; normalmente se utilizan los mismos datos salvo que el propio test los cambie.
- `@ParameterizedTest` junto con `@ValueSource` ejecuta el test una vez por cada dato proporcionado.

Para probar montos diferentes, las pruebas parametrizadas son más adecuadas.

## 7. Importaciones

```java
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
```

Además, necesitas la clase `Cuenta` en el paquete correspondiente. En Maven, verifica que tu proyecto tenga soporte para `junit-jupiter-params` (normalmente incluido si usas `spring-boot-starter-test`).

## Resumen

`@ParameterizedTest` define una prueba que recibe datos; `@ValueSource` proporciona esos datos; el método se ejecuta una vez por cada valor. Es útil para probar múltiples entradas sin duplicar código. Para comprobar un débito, comparar el saldo final con el saldo esperado ofrece una verificación más fuerte que comprobar únicamente que el saldo sea positivo.

# Pruebas parametrizadas con `@CsvSource`, `@CsvFileSource` y `@MethodSource`

Estas anotaciones proporcionan diferentes conjuntos de datos a una misma prueba `@ParameterizedTest`.

## 1. `@CsvSource`: datos escritos dentro del código

```java
@ParameterizedTest(name = "Caso {index}: monto {1}")
@CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000.1234"})
void testDebitoCuentaCsvSource(String index, String monto) {
    System.out.println(index + " -> " + monto);
    cuenta.debito(new BigDecimal(monto));
    assertNotNull(cuenta.getSaldo());
    assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
}
```

Cada cadena representa una fila CSV con **dos columnas**. JUnit asigna la primera a `index` y la segunda a `monto`. `{index}` en el nombre del test es el número automático de invocación (empieza en 1); `{0}` y `{1}` representan el primer y segundo argumentos. La variable `index` de tu método es distinta del marcador `{index}` de JUnit.

## 2. `@CsvFileSource`: datos desde un archivo CSV

```java
@ParameterizedTest
@CsvFileSource(resources = "/data.csv")
void testDebitoCuentaCsvFileSource(String monto) {
    cuenta.debito(new BigDecimal(monto));
    assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
}
```

El archivo debe estar en `src/test/resources/data.csv` (en un proyecto Maven convencional). Para este método con **un parámetro**, el archivo debe tener una columna por fila:

```csv
100
200
300
500
700
1000.1234
```

Si reutilizas las filas de `@CsvSource` (`1,100`, `2,200`, etc.), cada fila tendrá dos columnas, pero el método solo recibe un argumento. **Ese desajuste es una causa probable de que falle o se comporte distinto el test de archivo.** Puedes adaptar el método a `void testDebitoCuentaCsvFileSource(String index, String monto)` o modificar el CSV para dejar una sola columna. Si el archivo tiene encabezado, usa `numLinesToSkip = 1`.

## 3. `@MethodSource`: datos desde un método Java

```java
@ParameterizedTest
@MethodSource("montoList")
void testDebitoCuentaMethodSource(String monto) {
    cuenta.debito(new BigDecimal(monto));
    assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
}

private static List<String> montoList() {
    return Arrays.asList("100", "200", "300", "500", "700", "1000.1234");
}
```

`@MethodSource("montoList")` busca el método que genera los argumentos. `List<String>` es una interfaz que representa una lista de cadenas; `Arrays.asList(...)` construye una lista a partir de los valores. El método proveedor normalmente debe ser `static`, salvo cuando se utiliza el ciclo de vida `@TestInstance(PER_CLASS)`.

## 4. ¿Por qué puede fallar el último monto?

Si el saldo inicial es `1000.12345`, retirar `1000.1234` deja exactamente `0.00005`, que es **positivo**:

```text
1000.12345 - 1000.1234 = 0.00005
```

En ese caso `assertTrue(saldo.compareTo(BigDecimal.ZERO) > 0)` debe pasar. Revisa:

1. **Saldo inicial:** si es `1000`, el débito de `1000.1234` supera el saldo y tu implementación puede lanzar `DineroInsuficienteException`.
2. **Reinicio de cuenta:** crea una cuenta nueva en `@BeforeEach` para cada invocación parametrizada.
3. **Formato del CSV:** verifica que `data.csv` tenga el mismo número de columnas que los parámetros del método.
4. **Datos exactos:** imprime monto y saldo para detectar diferencias.

```java
@BeforeEach
void prepararCuenta() {
    cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
}
```

## 5. Prueba más precisa

Comprobar solamente que el saldo sea positivo no garantiza que se haya descontado el monto correcto. Puedes calcular el saldo esperado:

```java
@ParameterizedTest(name = "Débito de {0}")
@ValueSource(strings = {"100", "200", "300", "500", "700", "1000.1234"})
void testDebitoExacto(String monto) {
    BigDecimal saldoInicial = cuenta.getSaldo();
    BigDecimal cantidad = new BigDecimal(monto);
    BigDecimal saldoEsperado = saldoInicial.subtract(cantidad);

    cuenta.debito(cantidad);

    assertNotNull(cuenta.getSaldo());
    assertEquals(0, saldoEsperado.compareTo(cuenta.getSaldo()));
}
```

`BigDecimal` es inmutable: `subtract()` devuelve un nuevo valor y `compareTo()` devuelve cero cuando los valores son numéricamente iguales, incluso si difiere su escala decimal.

## 6. Importaciones

```java
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
```

## Resumen

- `@CsvSource`: datos separados por comas definidos en la anotación.
- `@CsvFileSource`: datos leídos de un archivo CSV en recursos de prueba.
- `@MethodSource`: datos devueltos por un método Java.
- Cada invocación parametrizada ejecuta también `@BeforeEach`.
- El último monto `1000.1234` debe dejar `0.00005` si el saldo inicial es `1000.12345`.
- Para identificar un fallo concreto, revisa el saldo inicial, la excepción y el contenido de `data.csv`.


# Pruebas parametrizadas: agregando más atributos con `@CsvSource` y `@CsvFileSource`

JUnit 5 permite suministrar varios argumentos en cada ejecución de una prueba parametrizada. Cada fila CSV representa un escenario y cada columna se asigna a un parámetro del método.

## 1. Ejemplo con cuatro argumentos

```java
@ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
@CsvSource({
    "200,100,John,Andres",
    "250,200,Pepe,Pepe",
    "299,300,maria,Maria",
    "400,500,Lucas,Luca",
    "750,700,Cata,Cata",
    "1000.1234,1000.1234,Cata,Cata"
})
void testDebitoCuentaCsvSource2(String saldo, String monto,
                               String esperado, String actual) {
    cuenta.setPersona(actual);
    cuenta.setSaldo(new BigDecimal(saldo));
    cuenta.debito(new BigDecimal(monto));

    assertNotNull(cuenta.getPersona());
    assertEquals(esperado, actual);
    assertNotNull(cuenta.getSaldo());
    assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
}
```

Las columnas corresponden, en orden, a `saldo`, `monto`, `esperado` y `actual`. La prueba se ejecuta seis veces.

## 2. Diagnóstico de las filas

| Datos | Resultado esperado |
|---|---|
| `200,100,John,Andres` | El saldo queda en 100, pero falla la comparación de nombres. |
| `250,200,Pepe,Pepe` | El saldo queda en 50 y coinciden los nombres. |
| `299,300,maria,Maria` | Con la validación anterior de `debito`, se lanza `DineroInsuficienteException` antes de las aserciones. Además, los nombres no coinciden. |
| `400,500,Lucas,Luca` | Se lanza la excepción por saldo insuficiente; también difieren los nombres. |
| `750,700,Cata,Cata` | El saldo queda en 50 y coinciden los nombres. |
| `1000.1234,1000.1234,Cata,Cata` | El saldo queda en cero; falla la condición `> 0`. |

`BigDecimal.compareTo(BigDecimal.ZERO)` devuelve `0` cuando el saldo es cero. Si la regla permite saldos de cero, utiliza `>= 0`.

## 3. Comparar el nombre de la cuenta

En tu prueba aparece:

```java
assertEquals(esperado, actual);
```

Eso compara dos parámetros que llegan del CSV, pero no lee directamente el estado de la cuenta. Una comprobación más útil es:

```java
assertEquals(esperado, cuenta.getPersona());
```

Así compruebas el valor que realmente quedó guardado en el objeto `Cuenta`.

## 4. Comprobar el saldo exacto

Si se permite dejar la cuenta en cero, puedes calcular el resultado esperado:

```java
BigDecimal saldoInicial = new BigDecimal(saldo);
BigDecimal cantidad = new BigDecimal(monto);
BigDecimal saldoEsperado = saldoInicial.subtract(cantidad);

cuenta.setSaldo(saldoInicial);
cuenta.debito(cantidad);

assertEquals(0, saldoEsperado.compareTo(cuenta.getSaldo()));
```

Para escenarios con fondos insuficientes, escribe otra prueba que compruebe `assertThrows(DineroInsuficienteException.class, ...)`. No mezcles en una misma prueba parametrizada escenarios que deberían terminar con éxito y otros que deberían lanzar una excepción sin distinguirlos.

## 5. Ejemplo con `@CsvFileSource`

```java
@ParameterizedTest
@CsvFileSource(resources = "/data2.csv")
void testDebitoCuentaCsvFileSource2(String saldo, String monto,
                                   String esperado, String actual) {
    cuenta.setPersona(actual);
    cuenta.setSaldo(new BigDecimal(saldo));
    cuenta.debito(new BigDecimal(monto));
    assertEquals(esperado, cuenta.getPersona());
    assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
}
```

Ubica `data2.csv` en `src/test/resources/data2.csv` para que `/data2.csv` se encuentre en la raíz del classpath de pruebas. El archivo puede contener:

```csv
200,100,John,Andres
250,200,Pepe,Pepe
299,300,maria,Maria
400,500,Lucas,Luca
750,700,Cata,Cata
1000.1234,1000.1234,Cata,Cata
```

El archivo reproduce intencionalmente escenarios que fallan; para una prueba de éxito usa solamente filas válidas. Si agregas una cabecera, configura `numLinesToSkip = 1`.

**Importante:** `@CsvFileSource` asigna columnas a los parámetros según su orden. Si `data.csv` tiene dos columnas pero el método recibe un único parámetro `monto`, puede terminar recibiendo la primera columna en lugar del importe deseado. Haz coincidir la estructura del archivo con la firma del método.

## 6. El atributo `name`

```java
@ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
```

- `{index}`: número de ejecución, empezando en 1.
- `{0}`: primer argumento de la fila; en la segunda prueba es el saldo, no el monto.
- `{argumentsWithNames}`: representación de los argumentos con nombres cuando están disponibles.

Si quieres mostrar el monto de la prueba con cuatro parámetros, utiliza `{1}`.

## 7. Importaciones

```java
import java.math.BigDecimal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.*;
```

Además, importa la clase `Cuenta` y la excepción personalizada si tu prueba las utiliza.

## Resumen

- `@CsvSource` proporciona varias filas y columnas directamente en el código.
- `@CsvFileSource` carga los argumentos desde un archivo CSV.
- Cada columna se asigna a un parámetro del método.
- Comprueba el estado del objeto, no solo los argumentos de entrada.
- Distingue entre débitos válidos, saldo cero y fondos insuficientes.
- Usa `assertThrows` cuando el resultado correcto sea una excepción.

# Agrupando pruebas con `@Tag` en JUnit 5

`@Tag` permite **clasificar pruebas mediante etiquetas** y seleccionar grupos al ejecutarlas. Es como poner etiquetas a carpetas: `cuentas`, `debito`, `integracion` o `rapidas`.

## 1. Ejemplo básico

```java
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CuentaTest {
    @Test
    @Tag("cuentas")
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        assertEquals(0, cuenta.getSaldo().compareTo(new BigDecimal("1000")));
    }
}
```

- `@Test`: identifica el método como prueba.
- `@Tag("cuentas")`: le asigna la etiqueta `cuentas`.
- `Cuenta`: clase del proyecto que representa una cuenta bancaria.
- `BigDecimal`: clase de Java para trabajar con números decimales precisos.
- `assertEquals`: comprueba que el resultado de `compareTo()` sea cero (valores numéricamente iguales).

**Importante:** agregar `@Tag` no evita que la prueba se ejecute; debes configurar un filtro para seleccionar etiquetas.

## 2. Diferentes etiquetas

```java
class CuentaTest {
    @Test
    @Tag("debito")
    void testDebito() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        cuenta.debito(new BigDecimal("200"));
        assertEquals(0, cuenta.getSaldo().compareTo(new BigDecimal("800")));
    }

    @Test
    @Tag("credito")
    void testCredito() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        cuenta.credito(new BigDecimal("500"));
        assertEquals(0, cuenta.getSaldo().compareTo(new BigDecimal("1500")));
    }
}
```

Las etiquetas describen el contexto de cada prueba. Puedes repetir `@Tag` para asignar varias etiquetas:

```java
@Test
@Tag("cuentas")
@Tag("debito")
void testDebitoCuenta() {
    // ...
}
```

## 3. Etiquetas en clases completas

```java
@Tag("cuentas")
class CuentaTest {
    @Test
    void testSaldo() {
        // Etiqueta: cuentas
    }

    @Test
    @Tag("debito")
    void testDebito() {
        // Etiquetas: cuentas y debito
    }
}
```

Las etiquetas declaradas a nivel de clase se aplican también a sus métodos de prueba.

## 4. Ejecutar pruebas por etiqueta con Maven

Si el proyecto utiliza Maven Surefire y JUnit 5, puedes filtrar las pruebas mediante:

```bash
mvn test -Dgroups=debito
```

Para excluir una etiqueta:

```bash
mvn test -DexcludedGroups=integracion
```

Para seleccionar varias etiquetas con una expresión:

```bash
mvn test -Dgroups="debito | credito"
```

Las opciones dependen de que la configuración de Maven Surefire y el motor de JUnit Jupiter del proyecto sean compatibles. En el IDE también puedes crear configuraciones de ejecución filtradas por etiquetas.

## 5. Reglas y buenas prácticas

- Usa nombres claros y consistentes, como `unitarias`, `integracion`, `cuentas` y `debito`.
- Los nombres de etiquetas no pueden estar vacíos ni contener espacios en blanco o caracteres reservados como `,`, `(`, `)`, `&`, `|` y `!`.
- Una etiqueta clasifica pruebas, pero no cambia su comportamiento.
- `@Tag` y `@Disabled` no son equivalentes: `@Disabled` omite una prueba; `@Tag` permite filtrarla desde la configuración de ejecución.
- Puedes combinar `@Tag` con `@ParameterizedTest`, `@RepeatedTest` y `@Nested`.

## Resumen

`@Tag` sirve para organizar pruebas por categorías. Puedes aplicarla a métodos o clases, asignar varias etiquetas y ejecutar grupos específicos desde Maven o tu IDE. Es especialmente útil cuando un proyecto crece y no quieres ejecutar todas las pruebas en cada ocasión.


