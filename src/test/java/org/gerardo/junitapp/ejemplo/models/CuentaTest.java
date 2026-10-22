package org.gerardo.junitapp.ejemplo.models;

import org.gerardo.junitapp.ejemplo.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*; // importamos estaticamente para no usar el Assert.

class CuentaTest {

    Cuenta cuenta; // atributo independiente de cada metodo (lo declaramos en initMetodoTest())

    @BeforeEach
    void initMetodoTest(){
        System.out.println("Iniciando el metodo. ");

        cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
    }

    @AfterEach
    void tearDown(){
        System.out.println("Finalizando metodo");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }

    @Test
    @DisplayName("Probando el nombre de la cuenta corriente")
    void test() {

        String esperado = "Andres";
        String real = cuenta.getPersona();

        assertNotNull(real, () -> "La cuenta no puede ser nula");
        assertEquals(esperado, real,
                () ->  "El nombre de la cuenta no es el que se esperaba: se esperaba  " + esperado +  "sin embargo " +
                        "fue " + real);
        assertTrue(real.equals("Andres"), () -> "Nombre cuenta esperada debe ser igual a la real");
    }

    @Test
    @DisplayName("Proabndo el saldo de la cuenta, que no sea null, mayor que cero, valor esperado")
    void testSaldoCuenta() {
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue()); // convertimos el BigDecimal a double
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    // En este test aplicamos TDD para comprar 2 objetos
    @Test
    @DisplayName("Comparando referencia que sean iguales")
    void testReferenciaCuenta() {
        cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);
    }

    // Este test resta de la cuenta
    @Test
    @DisplayName("Test del metodo debito")
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100)); // esperado 900.12345

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue()); // lo verificamos como un entero
        assertEquals("900.12345", cuenta.getSaldo().toPlainString()); // lo verificamos como un string

    }

    // Este test le suma a la cuenta
    @Test
    @DisplayName("Test del metodo credito")
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100)); // esperado 900.12345

        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue()); // lo verificamos como un entero
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString()); // lo verificamos como un string

    }

    @Test
    void dineroInsuficienteException() {
        cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });

        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    @DisplayName("Test relacion bidireccional")
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre("Banco del estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Probando relaciones entre las cuentas y el banco con assertAll")
    @Disabled
    void testRelacionBancoCuentas() {
        fail();
        Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertAll(() -> {
            assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(),
                    () -> "El valor del saldo de la cuenta2 no es el esperado");
        }, () -> {
            assertEquals("3000", cuenta1.getSaldo().toPlainString(),
                    () -> "El valor del saldo de la cuenta1 no es el esperado");
        }, () -> {
            assertEquals(2, banco.getCuentas().size(), () -> "El banco no tiene las cuentas esperadas");
        }, () -> {
            assertEquals("Banco del estado", cuenta1.getBanco().getNombre());
        }, () -> {
            // vamos a buscar en la lista una cuenta que tenga el nombre de Andres
            assertEquals("Andres", banco.getCuentas().stream()
                    .filter(c -> c.getPersona().equals("Andres"))
                    .findFirst()
                    .get().getPersona());
        }, () -> {
            assertTrue(banco.getCuentas().stream()
                    .anyMatch(c -> c.getPersona().equals("Jhon Doe")));
        });
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testSoloWindows(){

    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void testSoloLinuxMac(){

    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testNoWindows(){

    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void solojdk8(){

    }

    @Test
    @EnabledOnJre(JRE.JAVA_15)
    void name(){

    }

    @Test
    void imprimirSystemProperties(){
        Properties properties = System.getProperties();
        properties.forEach((k,v) -> System.out.println(k + " : " + v));
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = "21.*")
    void testJavaVersion(){

    }

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void testDev(){

    }



}