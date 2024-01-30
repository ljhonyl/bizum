package es.tiernoparla.bizum.modelo.basedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MiBancoDAO {
    private final String URL_CUENTA = "jdbc:mysql://localhost:3306/";
    private final String USUARIO = "jhony";
    private final String PASSWORD = "password";
    private final String BASE_DATOS="MiBanco3";
    private final String URL_BD=URL_CUENTA+BASE_DATOS;

    public Connection conectarBD(String url) {
        // Configuración de la conexión

        // Establecer la conexión
        try {
            Connection conexion = DriverManager.getConnection(url, USUARIO, PASSWORD);

            // Verificar si la conexión fue exitosa
            if (conexion != null) {
                System.out.println("Conexión exitosa a la base de datos.");
                return conexion;
            } else {
                System.out.println("No se pudo establecer la conexión.");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            return null;
        }
    }

	public void crearBD() {
		final String QUERY="CREATE DATABASE IF NOT EXISTS"+" "+BASE_DATOS;
         try(Connection conn=conectarBD(URL_BD);
         PreparedStatement ps=conn.prepareStatement(QUERY)){
             ps.executeUpdate();
         }catch (SQLException e){
             System.out.println("Error al crear la base de datos");
         }
	}

    public void crearTablaCuentasUsuarios(){
        final String QUERY="CREATE TABLE IF NOT EXISTS CuentasUsuarios("+
                "Id INT(9) PRIMARY KEY AUTO_INCREMENT,"+
                "Dni VARCHAR(9) NOT NULL,"+
                "Nombre VARCHAR(12) NOT NULL,"+
                "SegundoNombre VARCHAR(12),"+
                "Apellidos VARCHAR(25) NOT NULL,"+
                "Telefono INT(9) NOT NULL,"+
                "Contraseña VARCHAR(12) NOT NULL,"+
                "CuentaBizum INT(16)"+
                ");";
        try(Connection conn=conectarBD(URL_BD);
            PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void crearTablaCuentas(){
        final String QUERY="CREATE TABLE IF NOT EXISTS Cuentas("+
                "NumCuenta INT(16) AUTO_INCREMENT,"+
                "IdCuentaUsuario INT(9) NOT NULL,"+
                "PRIMARY KEY (NumCuenta,IdCuentaUsuario),"+
                "FOREIGN KEY (IdCuentaUsuario) REFERENCES CuentasUsuarios(Id)"+
                ");";
        try(Connection conn=conectarBD(URL_BD);
            PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void agregarForeignKeyCuentasUsuario(){
        final String QUERY="ALTER TABLE CuentasUsuarios ADD CONSTRAINT fk_CuentaBizum FOREIGN KEY(CuentaBizum) REFERENCES Cuentas(NumCuenta);";
        try(Connection conn=conectarBD(URL_BD);
            PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void agregarCuentaUsuario(){
        final String QUERY="INSERT INTO CuentasUsuario(Dni, Nombre, SegundoNombre, Apellidos, Contraseña) VALUES (?,?,?,?,?)";
        try(Connection conn=conectarBD(URL_BD);
            PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.setString(1,"");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        MiBancoDAO mb=new MiBancoDAO();
        mb.crearBD();
        mb.crearTablaCuentasUsuarios();
        mb.crearTablaCuentas();
        mb.agregarForeignKeyCuentasUsuario();
    }
    /*Cuentas{
    * **NumCuenta : INT <<AUTO_INCREMENT>>**
    * **IdCuentaUsuario : INT <<FK Cuenta de Usuario>>**
    --
    * Saldo : Double
}*/
}
