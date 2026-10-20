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
}