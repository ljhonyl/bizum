package es.tiernoparla.bizum.modelo;

import jakarta.persistence.*;


@Entity
@Table(name="CuentasBancarias")
public class CuentaBancaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NumCuenta")
    private int numCuenta;

    @ManyToOne
    @JoinColumn(name = "IdCuentaUsuario", referencedColumnName ="Id", nullable = false)
    private CuentaUsuario cuentaUsuario;

    @Column(name = "Saldo")
    private double saldo;
    @Transient
    private boolean esBizum;

    public CuentaBancaria(){}

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
