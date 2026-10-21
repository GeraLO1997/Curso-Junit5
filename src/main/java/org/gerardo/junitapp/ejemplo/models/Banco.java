package org.gerardo.junitapp.ejemplo.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Banco {
    private String nombre;
    private List<Cuenta> cuentas;

    public Banco(){
        cuentas = new ArrayList<>();
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto){
        origen.debito(monto);
        destino.credito(monto);

    }

    public void addCuenta(Cuenta cuenta){
        cuentas.add(cuenta);
        cuenta.setBanco(this); // esto arregla la relacion inversa y le agrega el nombre del banco
    }
}
