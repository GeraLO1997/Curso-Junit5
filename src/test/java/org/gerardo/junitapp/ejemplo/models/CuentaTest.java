package org.gerardo.junitapp.ejemplo.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*; // importamos estaticamente para no usar el Assert.

class CuentaTest {

    @Test
    void test(){
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));

        String esperado = "Andres";
        String real = cuenta.getPersona();

        assertEquals(esperado,real);
        assertTrue(real.equals("Andres"));
    }

    @Test
    void testSaldoCuenta(){
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        assertEquals(1000.12345,cuenta.getSaldo().doubleValue()); // convertimos el BigDecimal a double
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    // En este test aplicamos TDD para comprar 2 objetos
    @Test
    void testReferenciaCuenta(){
        Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);
    }
}