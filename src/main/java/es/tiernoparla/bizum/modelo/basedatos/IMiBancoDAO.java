package es.tiernoparla.bizum.modelo.basedatos;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import es.tiernoparla.bizum.modelo.CuentaUsuario;

import java.util.List;

public interface IMiBancoDAO {

    /**
     * Se agrega una nueva cuenta de usuario
     * @param usuario cuenta a agregar
     * @return exito si la operacion fue exitosa o no
     */
    boolean agregarCuentaUsuario(CuentaUsuario usuario);

    /**
     * Se aagrega una cuenta bancaria a la tabla
     * @param saldo dinero inicial en la cuenta
     * @param cuentaUsuario dueño de la cuenta
     */
    boolean agregarCuentaBancaria(int cuentaUsuario, double saldo);

    /**
     * Ingreso, se suma el saldo almacenado con la cantidad ingresada
     * @param numCuenta cuenta a la que se ingresa
     * @param cantidad cantidad que se ingresa
     */
    void ingresar(int numCuenta,double cantidad);

    /**
     * Retiro, se resta al saldo almacenado el retiro realizado
     * @param numCuenta cuenta a la que se retira
     * @param cantidad cantidad que se retira
     */
    void retirar(int numCuenta,double cantidad);

    /**
     * Se realiza el bizum comprobando que la cuentas tengan cuenta de bizum asociada
     * @param idUsuario se buscara la CuentaBizum asociada a este usuario
     * @param cantidad  monto
     * @param telefono  se buscara la CuentaBizum asociado a este telefono
     * @return exito, si la operacion fue exitosa o no
     */
    int hacerBizum(int idUsuario, int telefono, double cantidad);

    /**
     * Seleccion de las cuentas bancarias del usuario actual y comprobacion de si alguna cuenta es
     * una cuenta seleccionada como con bizum
     * @param idUsuario id del usuario que ha iniciado sesion
     * @return cuentas, cuentas con numero de cuenta, saldo y si es una cuenta con bizum
     */
    List<CuentaBancaria> getCuentasBancarias(int idUsuario);

    /**
     * Select de contraseña para el inicio de sesion pasandole el dni
     * @param dni identificador de usuario por el que se realiza la busqueda
     * @return datos, con el id y contraseña almacenada del usuario si los hubiera
     */
    List<String> comprobarContrasena(String dni);

    /**
     * Seteo del campo CuentaBizum
     * @param numeroCuenta Cuenta que pasara a funcionar con bizum
     * @param idUsuario Cuenta de usuario al que pertenece la cuenta
     */
    void seleccionarCuentaBizum(int numeroCuenta, int idUsuario);

    /**
     * Select de nombre y apellidos del dueño del numero receptor al realizar un bizum
     * @param telefono Campo por el que se busca
     * @return nombre, nombre del beneficiario o vacio si el numero no esta registrado
     */
    String getNombreBeneficiario(int telefono);
}
