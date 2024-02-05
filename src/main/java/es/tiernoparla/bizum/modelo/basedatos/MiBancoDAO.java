package es.tiernoparla.bizum.modelo.basedatos;

import es.tiernoparla.bizum.modelo.CuentaUsuario;

import java.sql.*;

/**
 * Clase que maneja las conexiones con la base de datos
 */
public class MiBancoDAO {
    private final String URL_GLOBAL = "jdbc:mysql://localhost:3306/";
    private final String USUARIO = "jhony";
    private final String PASSWORD = "password";
    private final String BASE_DATOS="MiBancoPruebas";
    private final String URL_BD=URL_GLOBAL+BASE_DATOS;

    public MiBancoDAO(){
        crearBD();
        crearTablaCuentasUsuarios();
        crearTablaCuentasUsuarios();
    }

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
         try(Connection conn=conectarBD(URL_GLOBAL);
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
                "Contrasena VARCHAR(12) NOT NULL,"+
                "CuentaBizum INT(16)"+
                ");";
        try(Connection conn=conectarBD(URL_BD);
            PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void crearTablaCuentasBancarias(){
        final String QUERY="CREATE TABLE IF NOT EXISTS Cuentas("+
                "NumCuenta INT(16) PRIMARY KEY AUTO_INCREMENT,"+
                "IdCuentaUsuario INT(9) NOT NULL,"+
                "Saldo DOUBLE,"+
                "FOREIGN KEY (IdCuentaUsuario) REFERENCES CuentasUsuarios(Id)"+
                ");";
        try(Connection conn=conectarBD(URL_BD);
            PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.executeUpdate();
            agregarForeignKeyCuentasUsuario();
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

    public boolean agregarCuentaUsuario(CuentaUsuario usuario){
        boolean exito=false;
        final String QUERY="INSERT INTO CuentasUsuarios(Dni, Nombre, SegundoNombre, Apellidos, Telefono,  Contrasena) VALUES (?,?,?,?,?,?)";
        try(Connection conn=conectarBD(URL_BD);
            PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.setString(1,usuario.getDni());
            ps.setString(2,usuario.getNombre());
            ps.setString(3,usuario.getSegundoNombre());
            ps.setString(4,usuario.getApellidos());
            ps.setInt(5,usuario.getTelefono());
            ps.setString(6,usuario.getContrasena());
            ps.executeUpdate();
            exito=true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            return exito;
        }
    }

    private void agregarCuentaBancaria(int id, double saldo){
        final String QUERY="INSERT INTO Cuentas(IdCuentaUsuario, Saldo) VALUES (?,?)";
        try(Connection conn=conectarBD(URL_BD);
            PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.setInt(1,id);
            ps.setDouble(2,saldo);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void ingresar(int numCuenta, int cantidad){
        final String QUERY="UPDATE Cuentas SET Saldo=Saldo+? WHERE NumCuenta=?";
        try(Connection conn=conectarBD(URL_BD);
        PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.setInt(1,cantidad);
            ps.setInt(2,numCuenta);
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void retirar(int numCuenta, int cantidad){
        final String QUERY="UPDATE Cuentas SET Saldo=Saldo-? WHERE NumCuenta=?";
        try(Connection conn=conectarBD(URL_BD);
            PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.setInt(1,cantidad);
            ps.setInt(2,numCuenta);
            ps.executeUpdate();
        }
        catch (SQLException e){

        }
    }

    private void hacerBizum(int numCuenta, int cantidad, int numCuentaReceptor){
        retirar(numCuenta,cantidad);
        ingresar(numCuentaReceptor,cantidad);
    }

    /**
     * Select de contraseña para el inicio de sesion pasandole el dni
     */
    public String comprobarContrasena(String dni){
        String password="";
        final String QUERY="SELECT Contrasena FROM CuentasUsuarios WHERE Dni=?";
        try(Connection conn=conectarBD(URL_BD);
        PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.setString(1,dni);
            ResultSet rs=ps.executeQuery();
            rs.next();
            password=rs.getString("Contrasena");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return password;
    }
}
