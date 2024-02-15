package es.tiernoparla.bizum.modelo.basedatos;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import es.tiernoparla.bizum.modelo.CuentaUsuario;
import es.tiernoparla.bizum.modelo.encriptador.HashManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que maneja las conexiones con la base de datos
 */
public class MiBancoDAO {
    private final String URL_GLOBAL = "jdbc:mysql://localhost:3306/";
    private final String USUARIO = "jhony";
    private final String PASSWORD = "password";
    private final String BASE_DATOS = "MiBancoPruebas";
    private final String URL_BD = URL_GLOBAL + BASE_DATOS;

    public MiBancoDAO() {
        crearBD();
        crearTablaCuentasUsuarios();
        crearTablaCuentasBancarias();
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
        final String QUERY = "CREATE DATABASE IF NOT EXISTS" + " " + BASE_DATOS;
        try (Connection conn = conectarBD(URL_GLOBAL);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos");
        }
    }

    public void crearTablaCuentasUsuarios() {
        final String QUERY = "CREATE TABLE IF NOT EXISTS CuentasUsuarios(" +
                "Id INT(9) PRIMARY KEY AUTO_INCREMENT," +
                "Dni VARCHAR(9) NOT NULL," +
                "Nombre VARCHAR(12) NOT NULL," +
                "SegundoNombre VARCHAR(12)," +
                "Apellidos VARCHAR(25) NOT NULL," +
                "Telefono INT(9) NOT NULL," +
                "Contrasena VARCHAR(255) NOT NULL," +
                "CuentaBizum INT(16)" +
                ");";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void crearTablaCuentasBancarias() {
        final String QUERY = "CREATE TABLE IF NOT EXISTS Cuentas(" +
                "NumCuenta INT(16) PRIMARY KEY AUTO_INCREMENT," +
                "IdCuentaUsuario INT(9) NOT NULL," +
                "Saldo DOUBLE," +
                "FOREIGN KEY (IdCuentaUsuario) REFERENCES CuentasUsuarios(Id)" +
                ");";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            if (!(tablaCuentasUsuariosExiste())) {
                ps.executeUpdate();
                agregarForeignKeyCuentasUsuario();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean tablaCuentasUsuariosExiste() {
        boolean existe = false;
        final String QUERY = "SHOW TABLES LIKE ?";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.setString(1, "Cuentas");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                existe = true;
            }
        } catch (SQLException e) {

        } finally {
            return existe;
        }
    }

    private void agregarForeignKeyCuentasUsuario() {
        final String QUERY = "ALTER TABLE CuentasUsuarios ADD CONSTRAINT fk_CuentaBizum FOREIGN KEY(CuentaBizum) REFERENCES Cuentas(NumCuenta);";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean agregarCuentaUsuario(CuentaUsuario usuario) {
        boolean exito = false;
        final String QUERY = "INSERT INTO CuentasUsuarios(Dni, Nombre, Apellidos, Telefono, Contrasena) VALUES (?,?,?,?,?)";
        HashManager encriptador=new HashManager();
        String contrasenaCifrada=encriptador.getDigest(usuario.getContrasena());
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.setString(1, usuario.getDni());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellidos());
            ps.setInt(4, usuario.getTelefono());
            ps.setString(5, contrasenaCifrada);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return exito;
        }
    }

    public void ingresar(int numCuenta, double cantidad) {
        final String QUERY = "UPDATE Cuentas SET Saldo=Saldo+? WHERE NumCuenta=?";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.setDouble(1, cantidad);
            ps.setInt(2, numCuenta);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retirar(int numCuenta, double cantidad) {
        final String QUERY = "UPDATE Cuentas SET Saldo=Saldo-? WHERE NumCuenta=?";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.setDouble(1, cantidad);
            ps.setInt(2, numCuenta);
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public int hacerBizum(int idUsaurio, double cantidad, int telefono) {
        int exito=-1;
        int numCuentaEmisor=buscarCuentaConBizum(idUsaurio);
        int numCuentaReceptor = buscarCuentaPorNumero(telefono);
        if(numCuentaEmisor!=-1 && numCuentaReceptor!=-1){
            double saldo=comprobarSaldoCuentaBizum(numCuentaEmisor);
            if(saldo>=cantidad){
                retirar(numCuentaEmisor, cantidad);
                ingresar(numCuentaReceptor, cantidad);
                exito=1;
            }
            else{
                exito=0;
            }
        }
        return exito;
    }

    private double comprobarSaldoCuentaBizum(int numCuentaEmisor) {
        final String QUERY = "SELECT Saldo FROM Cuentas WHERE NumCuenta=?";
        double saldo = -1;
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY);) {
            ps.setInt(1, numCuentaEmisor);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                saldo=rs.getDouble("Saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saldo;
    }

    private int buscarCuentaPorNumero(int telefono) {
        final String QUERY = "SELECT CuentaBizum FROM CuentasUsuarios WHERE Telefono=?";
        int cuentaBizum = -1;
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY);) {
            ps.setInt(1, telefono);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                String cuentaBizumAux=rs.getString("CuentaBizum");
                System.out.println(cuentaBizumAux);
                if(cuentaBizumAux!=null){
                    cuentaBizum = rs.getInt("CuentaBizum");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cuentaBizum;
    }

    private int buscarCuentaConBizum(int idUsuario){
        int cuentaBizum=-1;
        final String QUERY="SELECT CuentaBizum FROM CuentasUsuarios WHERE Id=?";
        try(Connection conn=conectarBD(URL_BD);
        PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.setInt(1,idUsuario);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                String cuentaBizumAux=rs.getString("CuentaBizum");
                System.out.println(cuentaBizumAux);
                if(cuentaBizumAux!=null){
                    cuentaBizum = rs.getInt("CuentaBizum");
                }
            }
        }
        catch (SQLException e){

        }
        return cuentaBizum;
    }

    /**
     * Select de contraseña para el inicio de sesion pasandole el dni
     */
    public List<String> comprobarContrasena(String dni) {
        List<String> datos = new ArrayList<String>();
        final String QUERY = "SELECT Id ,Contrasena FROM CuentasUsuarios WHERE Dni=?";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int idUsuario = rs.getInt("Id");
                String password = rs.getString("Contrasena");
                datos.add(String.valueOf(idUsuario));
                datos.add(password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }

    public List<CuentaBancaria> getCuentasBancarias(int idUsuario) {
        final String QUERY = "SELECT NumCuenta, Saldo FROM Cuentas WHERE IdCuentaUsuario=?";
        List<CuentaBancaria> cuentas = new ArrayList<CuentaBancaria>();
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY);) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CuentaBancaria cuenta = new CuentaBancaria(rs.getInt("NumCuenta"), rs.getDouble("Saldo"));
                cuentas.add(cuenta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cuentas;
    }

    public void seleccionarCuentaBizum(int numeroCuenta, int idUsuario) {
        final String QUERY = "UPDATE CuentasUsuarios SET CuentaBizum=? WHERE Id=?";
        try (Connection conn = conectarBD(URL_BD);
        PreparedStatement ps=conn.prepareStatement(QUERY)) {
            ps.setInt(1,numeroCuenta);
            ps.setInt(2,idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNombreBeneficiario(int telefono) {
        final String QUERY = "SELECT Nombre, Apellidos FROM CuentasUsuarios WHERE Telefono=?";
        String nombre="";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY);) {
            ps.setInt(1, telefono);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                nombre=rs.getString("Nombre")+" "+rs.getString("Apellidos");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombre;
    }

    public void addCuentaBancaria(Double saldo, int idUsuario) {
        final String QUERY="INSERT INTO Cuentas (IdCuentaUsuario,Saldo) values (?,?)";
        try(Connection conn=conectarBD(URL_BD);
        PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.setInt(1,idUsuario);
            ps.setDouble(2,saldo);
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}