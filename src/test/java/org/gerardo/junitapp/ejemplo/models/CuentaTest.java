package org.gerardo.junitapp.ejemplo.models;

import org.gerardo.junitapp.ejemplo.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*; // importamos estaticamente para no usar el Assert.
import static org.junit.jupiter.api.Assumptions.*;

class CuentaTest {

    Cuenta cuenta; // atributo independiente de cada metodo (lo declaramos en initMetodoTest())

    private TestInfo testInfo;
    private TestReporter testReporter;

    @BeforeEach
    void initMetodoTest(TestInfo testInfo, TestReporter testReporter){
        System.out.println("Iniciando el metodo. ");

        this.testInfo = testInfo;
        this.testReporter = testReporter;

        cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));

        testReporter.publishEntry(" ejecutando: " + testInfo + testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElseThrow().getName()
                + " con las estiquetas " + testInfo.getTags());


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

    @Tag("cuenta")
    @Nested
    @DisplayName("Probando atributos de la cuenta")
    class CuentaTestNombreSaldo{
        @Test
        @DisplayName("Probando el nombre de la cuenta corriente")
        void cuentaTest() {

            String esperado = "Andres";
            String real = cuenta.getPersona();

            testReporter.publishEntry(testInfo.getTags().toString());
            if (testInfo.getTags().contains("cuenta")){
                testReporter.publishEntry("hacer algo con la etiqueta cuenta");
            }

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
    }


    @Nested
    @DisplayName("Probando operaciones")
    class CuentaOperacionesTest{
        // Este test resta de la cuenta
        @Test
        @DisplayName("Test del metodo debito")
        @Tag("cuenta")
        void testDebitoCuenta() {
            cuenta.debito(new BigDecimal(100)); // esperado 900.12345

            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue()); // lo verificamos como un entero
            assertEquals("900.12345", cuenta.getSaldo().toPlainString()); // lo verificamos como un string

        }

        // Este test le suma a la cuenta
        @Test
        @DisplayName("Test del metodo credito")
        @Tag("cuenta")
        void testCreditoCuenta() {
            cuenta.credito(new BigDecimal(100)); // esperado 900.12345

            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue()); // lo verificamos como un entero
            assertEquals("1100.12345", cuenta.getSaldo().toPlainString()); // lo verificamos como un string

        }

        @Test
        @DisplayName("Test relacion bidireccional")
        @Tag("cuenta")
        @Tag("banco")
        void testTransferirDineroCuentas() {
            Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

            Banco banco = new Banco();
            banco.setNombre("Banco del estado");
            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

            assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
            assertEquals("3000", cuenta1.getSaldo().toPlainString());
        }
    }



    @Test
    @Tag("cuenta")
    @Tag("error")
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
    @DisplayName("Probando relaciones entre las cuentas y el banco con assertAll")
    @Tag("banco")
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

    @Nested
    class SistemaOperativoTest{
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
    }

    @Nested
    class JavaVersionTest{
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void solojdk8(){

        }

        @Test
        @EnabledOnJre(JRE.JAVA_15)
        void name(){

        }
    }

    @Nested
    class SistemPropertiesTest{
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

    @Nested
    class variableAmbienteTest{
        @Test
        void imprimirVariablesAmbiente(){
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k,v) -> System.out.println(k + " = " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-21*")
        void testJavaHome(){

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "6")
        void testProcesadores(){

        }
    }




// Creamos una variable de ambiente llamada ENV=dev
    @Test
    @DisplayName("test saldo cuenta dev")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(esDev);
        assumingThat(esDev, () -> {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        });


    }

    @DisplayName("Prueba debito")
    @RepeatedTest(value=5, name = "{displayName} - Repeticion numero {currentRepetition} de {totalRepetitions}")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if (info.getCurrentRepetition() == 3){
            System.out.println("estamos en la repeticion " + info.getCurrentRepetition());
        }

        cuenta.debito(new BigDecimal(100)); // esperado 900.12345

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue()); // lo verificamos como un entero
        assertEquals("900.12345", cuenta.getSaldo().toPlainString()); // lo verificamos como un string

    }

    @Tag("param")
    @Nested
    class PruebasParametrizadas{
        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100","200","300","500","700","1000.1234"})
        void testDebitoCuentaValueSource(String monto) {
            cuenta.debito(new BigDecimal(monto));

            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); // lo verificamos como un entero

        }


        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"1,100","2,200","3,300","4,500","5,700","6,1000.1234"})
        void testDebitoCuentaCsvSource(String index, String monto) {
            System.out.println(index + " -> " + monto);
            cuenta.debito(new BigDecimal(monto));

            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); // lo verificamos como un entero

        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"200,100,John,Andres","250,200,Pepe,Pepe","299,300,maria,Maria","400,500,Lucas,Luca","750,700,Cata," +
                "Cata",
                "1000.1234,1000.1234,Cata,Cata"})
        void testDebitoCuentaCsvSource2(String saldo, String monto,String esperado, String actual) {
            System.out.println(saldo + " -> " + monto);
            cuenta.setPersona(actual);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));

            assertNotNull(cuenta.getPersona());
            assertEquals(esperado,actual);
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); // lo verificamos como un entero

        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void testDebitoCuentaCsvFileSource(String monto) {
            cuenta.debito(new BigDecimal(monto));

            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); // lo verificamos como un entero

        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data2.csv")
        void testDebitoCuentaCsvFileSource2(String saldo, String monto,String esperado, String actual) {
            cuenta.setPersona(actual);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));

            assertNotNull(cuenta.getPersona());
            assertEquals(esperado,actual);
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @MethodSource("montoList")
        void testDebitoCuentaMethodSource(String monto) {
            cuenta.debito(new BigDecimal(monto));

            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); // lo verificamos como un entero

        }

        private static List<String> montoList(){
            return Arrays.asList("100","200","300","500","700","1000.1234");
        }

    }







}