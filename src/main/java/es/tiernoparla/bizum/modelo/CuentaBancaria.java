package es.tiernoparla.bizum.modelo;

public class CuentaBancaria {
    private int numCuenta;
    private double saldo;

    private boolean esBizum;

    public CuentaBancaria(int numCuenta, double saldo){
        this.numCuenta=numCuenta;
        this.saldo=saldo;
        esBizum=false;
    }

    public int getNumCuenta(){
        return numCuenta;
    }

    public void setNumCuenta(int numCuenta){
        this.numCuenta=numCuenta;
    }

    public double getSaldo(){
        return saldo;
    }

    public void setSaldo(double saldo){
        this.saldo=saldo;
    }

    public boolean getEsBizum() {
        return esBizum;
    }

    public void setEsBizum(boolean esBizum){
        this.esBizum=esBizum;
    }
}
