package es.tiernoparla.bizum.modelo;

public class CuentaBancaria {
    private String NumCuenta;
    private double Saldo;

    public CuentaBancaria(String numCuenta, double saldo){
        this.NumCuenta=numCuenta;
        this.Saldo=saldo;
    }
}
