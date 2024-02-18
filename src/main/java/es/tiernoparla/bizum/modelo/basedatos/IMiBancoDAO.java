package es.tiernoparla.bizum.modelo.basedatos;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import es.tiernoparla.bizum.modelo.CuentaUsuario;

import java.util.List;

public interface IMiBancoDAO {
    public boolean agregarCuentaUsuario(CuentaUsuario usuario);
    public boolean agregarCuentaBancaria(int cuentaUsuario, double saldo);
    public void ingresar(int numCuenta,double cantidad);
    public void retirar(int numCuenta,double cantidad);
    public int hacerBizum(int idUsuario, int telefono, double cantidad);
    public List<CuentaBancaria> getCuentasBancarias(int idUsuario);
    List<String> comprobarContrasena(String dni);
    void seleccionarCuentaBizum(int numeroCuenta, int idUsuario);
    String getNombreBeneficiario(int numero);
}
