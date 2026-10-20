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
