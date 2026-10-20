# Temario

## Nivel 1. Fundamentos del testing
Empezamos aquí
Comprender qué estamos haciendo y por qué.
1. ¿Qué es testing o prueba de software?
2. ¿Qué es JUnit 5 y para qué sirve?
3. ¿Qué es una prueba unitaria?
4. Diferencias entre pruebas unitarias, de integración y end-to-end.
5. Anatomía de una prueba: Arrange, Act, Assert (AAA).
6. Estructura de carpetas src/main/java y src/test/java.




## Nivel 2. Escribir las primeras pruebas
Aprender a escribir y ejecutar pruebas.

7. La anotación @Test.
8. Crear objetos y ejecutar métodos desde una prueba.
9. Assertions: assertEquals, assertTrue, assertFalse, assertNull y assertNotNull.
10. Interpretar resultados: pruebas aprobadas y fallidas.
11. Nombres descriptivos para los métodos de prueba.
12. Ejecutar pruebas desde el IDE y Maven.




## Nivel 3. Preparación y organización
Evitar duplicación y mantener pruebas claras.

13. @BeforeEach y @AfterEach.
14. @BeforeAll y @AfterAll.
15. @DisplayName.
16. @Nested para agrupar pruebas relacionadas.
17. @Disabled y cuándo evitar su uso.
18. Ciclo de vida de una prueba.




## Nivel 4. Validaciones y casos especiales
Comprobar más que el caso feliz.

19. assertThrows para excepciones.
20. assertAll para agrupar comprobaciones.
21. Probar valores límite y datos inválidos.
22. Pruebas parametrizadas con @ParameterizedTest.
23. Proveedores de datos: @ValueSource, @CsvSource y @MethodSource.
24. Pruebas de métodos que devuelven objetos, listas o valores opcionales.




## Nivel 5. Mockito y pruebas aisladas
Simular dependencias sin recurrir a servicios reales.

25. ¿Qué es un mock y para qué sirve?
26. Diferencias entre JUnit 5 y Mockito.
27. @Mock y @InjectMocks.
28. when(...).thenReturn(...).
29. when(...).thenThrow(...).
30. verify() y verificación de interacciones.
31. @ExtendWith(MockitoExtension.class).
32. Probar servicios que dependen de repositorios.




## Nivel 6. JUnit 5 con Spring Boot
Aplicar las pruebas a un backend real.

33. Dependencia spring-boot-starter-test.
34. Probar servicios con reglas de negocio.
35. Probar controladores REST con MockMvc.
36. Diferencia entre @WebMvcTest y @SpringBootTest.
37. Probar códigos HTTP y respuestas JSON.
38. Pruebas de integración con base de datos.




## Nivel 7. Buenas prácticas profesionales
Escribir pruebas útiles y mantenibles.

39. Qué probar y qué no probar.
40. Pruebas independientes y deterministas.
41. Cobertura de código y sus limitaciones.
42. Errores comunes al usar Mockito.
43. Ejecutar pruebas dentro de CI/CD.
44. Ejercicio final: probar un servicio de empleados o productos.

---

# Concepto basico
Es un framework para escribir pruebas unitarias de nuestro codigo y ejecutarlas en la JVM.

## Por que necesitamos probar nuestro codigo?
### 1. Primero, ¿por qué necesitamos probar nuestro código?
Imagina que estás desarrollando un sistema para una tienda. Necesitas calcular el precio total de los productos que compra un cliente.

Creas una clase Java:
```java
public class Calculadora {

    public int sumar(int a, int b) {
        return a + b;
    }
}
```
Esta clase tiene un método llamado `sumar()` que recibe dos números y devuelve su suma.

Por ejemplo:
- Recibe 10 y 5.
- Realiza la operación 10 + 5.
- Devuelve 15.

Parece sencillo, pero imagina que tu método tiene un error y devuelve un resultado incorrecto. ¿Cómo puedes comprobar que funciona?

Una opción sería ejecutar manualmente tu programa y revisar el resultado cada vez que modificas el código. Sin embargo, esto se vuelve tedioso cuando tienes cientos de métodos.

*Aquí es donde entra JUnit 5.*

### 2. ¿Cómo nos ayuda JUnit 5?
JUnit nos permite escribir código que comprueba automáticamente el comportamiento de nuestro programa.

Por ejemplo, queremos verificar que el método `sumar()` devuelva 15 cuando recibe 10 y 5.

Escribimos una prueba:

```java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculadoraTest {

    @Test
    void sumarDosNumeros() {
        Calculadora calculadora = new Calculadora();

        int resultado = calculadora.sumar(10, 5);

        assertEquals(15, resultado);
    }
}
```

Si el método devuelve 12 en lugar de 15, la prueba falla. De esta manera podemos detectar errores sin tener que comprobar manualmente el resultado.

### 3 ¿Qué significa cada parte del código?
Veamos los elementos más importantes del ejemplo.
#### A. @Test

```java
@Test
void sumarDosNumeros() {
    // Código de la prueba
}
```

`@Test` es una anotación de **JUnit 5** que indica que un método representa una prueba que el framework debe descubrir y ejecutar.

Piensa en ella como una etiqueta que dice: *«Este método contiene una prueba»*.

El nombre `sumarDosNumeros()` lo elegimos nosotros. Lo recomendable es que describa qué comportamiento estamos verificando.
#### B. Calculadora calculadora = new Calculadora();
```java
Calculadora calculadora = new Calculadora();
```
Aquí creamos un objeto de la clase que queremos probar.
- Calculadora: el tipo de dato, es decir, la clase.
- calculadora: el nombre de la variable que guardará la referencia al objeto.
- new Calculadora(): crea una instancia de la clase.

Es el objeto cuyo comportamiento queremos verificar.
#### C. int resultado = calculadora.sumar(10, 5);
```java
int resultado = calculadora.sumar(10, 5);
```

Llamamos al método sumar() con los valores 10 y 5, y guardamos el resultado en la variable resultado.

Como el método devuelve 15, la variable contiene ese valor.

#### D. assertEquals(15, resultado);

```java 
assertEquals(15, resultado);
```

Esta es una de las partes más importantes de la prueba.

`assertEquals()` comprueba que dos valores sean iguales:
- Primer argumento: el valor esperado, 15.
- Segundo argumento: el valor obtenido, resultado.

Si son iguales, esa comprobación pasa. Si son diferentes, falla y JUnit informa del error.

En este ejemplo utilizamos un método de la clase `Assertions` de JUnit Jupiter.

---

# 1- ¿Qué es testing o prueba de software?

