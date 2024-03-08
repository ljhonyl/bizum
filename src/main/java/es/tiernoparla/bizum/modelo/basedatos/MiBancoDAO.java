package es.tiernoparla.bizum.modelo.basedatos;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import es.tiernoparla.bizum.modelo.CuentaUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que maneja las conexiones con la base de datos
 */
public class MiBancoDAO implements IMiBancoDAO{

    /*------------ Retocar estos valores ------------------------------------*/
    private final String URL_GLOBAL = "jdbc:mysql://localhost:3306/";
    private final String USUARIO = "jhony";
    private final String PASSWORD = "password";
    private final String BASE_DATOS = "MiBancoPruebas";
    /*-----------------------------------------------------------------------*/
    private final String URL_BD = URL_GLOBAL + BASE_DATOS;

    public MiBancoDAO() {
        crearBD();
        crearTablaCuentasUsuarios();
        crearTablaCuentasBancarias();
    }

    /**
     * Establece la conexion
     * @param url se le pasa el parametro porque este sera dinamico, no es el mismo al
     *            crear la base de datos que al trabajar con la base de datos creada
     * @return conexion si fue exitosa o null (por legilidad del codigo) si algo fallo
     */
    public Connection conectarBD(String url) {
        try {
            Connection conexion = DriverManager.getConnection(url, USUARIO, PASSWORD);

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

    /**
     * Crea la base de datos, usa la url del servidor (URL_GLOBAL)
     */
    public void crearBD() {
        final String QUERY = "CREATE DATABASE IF NOT EXISTS" + " " + BASE_DATOS;
        try (Connection conn = conectarBD(URL_GLOBAL);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos");
        }
    }

    /**
     * Crea la tabla de CuentasUsuarios, usa la url de la base de datos (URL_BD)
     */
    public void crearTablaCuentasUsuarios() {
        final String QUERY = "CREATE TABLE IF NOT EXISTS CuentasUsuarios(" +
                "Id INT(9) PRIMARY KEY AUTO_INCREMENT," +
                "Dni VARCHAR(9) NOT NULL," +
                "Nombre VARCHAR(25) NOT NULL," +
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

    /**
     * Crea la tabla de CuentasBancarias, usa la url de la base de datos (URL_BD).
     */
    private void crearTablaCuentasBancarias() {
        final String QUERY = "CREATE TABLE IF NOT EXISTS CuentasBancarias(" +
                "NumCuenta INT(16) PRIMARY KEY AUTO_INCREMENT," +
                "IdCuentaUsuario INT(9) NOT NULL," +
                "Saldo DOUBLE," +
                "FOREIGN KEY (IdCuentaUsuario) REFERENCES CuentasUsuarios(Id)" +
                ");";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            if (!(tablaCuentasBancariasExiste())) {
                ps.executeUpdate();
                agregarForeignKeyCuentasUsuario();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Comprueba si la tabla CuentasBancarias existe
     * @return existe, true si existe false si no
     */
    private boolean tablaCuentasBancariasExiste() {
        boolean existe = false;
        final String QUERY = "SHOW TABLES LIKE ?";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.setString(1, "CuentasBancarias");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                existe = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

    /**
     * Establece la clave foranea que relaciona CuentaBizum con el NumCuenta
     */
    private void agregarForeignKeyCuentasUsuario() {
        final String QUERY = "ALTER TABLE CuentasUsuarios ADD CONSTRAINT fk_CuentaBizum FOREIGN KEY(CuentaBizum) REFERENCES CuentasBancarias(NumCuenta);";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     @Override
    public boolean agregarCuentaUsuario(CuentaUsuario usuario) {
        boolean exito = false;
        final String QUERY = "INSERT INTO CuentasUsuarios(Dni, Nombre, Apellidos, Telefono, Contrasena) VALUES (?,?,?,?,?)";
        try {
            Connection conn = conectarBD(URL_BD);
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(QUERY)) {
                ps.setString(1, usuario.getDni());
                ps.setString(2, usuario.getNombre());
                ps.setString(3, usuario.getApellidos());
                ps.setInt(4, usuario.getTelefono());
                ps.setString(5, usuario.getContrasena());
                ps.executeUpdate();
                exito = true;
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }

    @Override
    public void ingresar(int numCuenta, double cantidad) {
        final String QUERY = "UPDATE CuentasBancarias SET Saldo=Saldo+? WHERE NumCuenta=?";
        realizarOperacion(numCuenta, cantidad, QUERY);
    }


    @Override
    public void retirar(int numCuenta, double cantidad) {
        final String QUERY = "UPDATE CuentasBancarias SET Saldo=Saldo-? WHERE NumCuenta=?";
        realizarOperacion(numCuenta, cantidad, QUERY);
    }

    private void realizarOperacion(int numCuenta, double cantidad, String QUERY) {
        try (Connection conn = conectarBD(URL_BD)){
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(QUERY)) {
                ps.setDouble(1, cantidad);
                ps.setInt(2, numCuenta);
                ps.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int hacerBizum(int idUsuario, int telefono, double cantidad) {
        int exito=-1;
        int numCuentaEmisor=buscarCuentaConBizum(idUsuario);
        int numCuentaReceptor = buscarCuentaBizumPorNumero(telefono);
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

    /**
     * Se selecciona el saldo de la cuenta buscada
     * @param numCuentaEmisor valor por el que se filtra
     * @return saldo, el saldo disponible o -1 si no hay
     */
    private double comprobarSaldoCuentaBizum(int numCuentaEmisor) {
        final String QUERY = "SELECT Saldo FROM CuentasBancarias WHERE NumCuenta=?";
        double saldo = -1;
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
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

    /**
     * Se busca la cuenta bizum asociada a este telefono
     * @param telefono numero por el que se busca
     * @return cuentaBizum, numero de la cuenta con bizum o -1 si no tiene cuenta bizum
     * seleccionada
     */
    private int buscarCuentaBizumPorNumero(int telefono) {
        final String QUERY = "SELECT CuentaBizum FROM CuentasUsuarios WHERE Telefono=?";
        int cuentaBizum = -1;
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.setInt(1, telefono);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    String cuentaBizumAux=rs.getString("CuentaBizum");
                    System.out.println(cuentaBizumAux);
                    if(cuentaBizumAux!=null){
                        cuentaBizum = rs.getInt("CuentaBizum");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cuentaBizum;
    }

    /**
     * Se busca la cuenta bizum asociada a este telefono
     * @param idUsuario numero por el que se busca
     * @return cuentaBizum, numero de la cuenta con bizum o -1 si no tiene cuenta bizum
     * seleccionada
     */
    private int buscarCuentaConBizum(int idUsuario){
        int cuentaBizum=-1;
        final String QUERY="SELECT CuentaBizum FROM CuentasUsuarios WHERE Id=?";
        try(Connection conn=conectarBD(URL_BD);
            PreparedStatement ps=conn.prepareStatement(QUERY)){
            ps.setInt(1,idUsuario);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    String aux=rs.getString("CuentaBizum");
                    if(aux!=null)
                        cuentaBizum = Integer.parseInt(aux);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return cuentaBizum;
    }

    @Override
    public List<String> comprobarContrasena(String dni) {
        List<String> datos = new ArrayList<>();
        final String QUERY = "SELECT Id ,Contrasena FROM CuentasUsuarios WHERE Dni=?";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.setString(1, dni);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int idUsuario = rs.getInt("Id");
                    String password = rs.getString("Contrasena");
                    datos.add(String.valueOf(idUsuario));
                    datos.add(password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }

    @Override
    public List<CuentaBancaria> getCuentasBancarias(int idUsuario) {
        final String QUERY = "SELECT NumCuenta, Saldo FROM CuentasBancarias WHERE IdCuentaUsuario=?";
        List<CuentaBancaria> cuentas = new ArrayList<>();
        //Cuenta que tiene bizum
        int cuentaBizum = buscarCuentaConBizum(idUsuario);
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.setInt(1, idUsuario);
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    CuentaBancaria cuenta = new CuentaBancaria(rs.getInt("NumCuenta"), rs.getDouble("Saldo"));
                    cuentas.add(cuenta);
                }
                if (!cuentas.isEmpty()) {
                    for(CuentaBancaria cuenta:cuentas){
                        if(cuenta.getNumCuenta()==cuentaBizum){
                            //Si coincide el numero de cuenta
                            cuenta.setEsBizum(true);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cuentas;
    }

    @Override
    public void seleccionarCuentaBizum(int numeroCuenta, int idUsuario) {
        final String QUERY = "UPDATE CuentasUsuarios SET CuentaBizum=? WHERE Id=?";
        try (Connection conn = conectarBD(URL_BD)) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(QUERY)) {
                ps.setInt(1, numeroCuenta);
                ps.setInt(2, idUsuario);
                ps.executeUpdate();
                conn.commit();
            }
            catch (SQLException e) {
                e.printStackTrace();
                conn.rollback();
            }
            finally {
                conn.setAutoCommit(true);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getNombreBeneficiario(int telefono) {
        final String QUERY = "SELECT Nombre, Apellidos FROM CuentasUsuarios WHERE Telefono=?";
        String nombre="";
        try (Connection conn = conectarBD(URL_BD);
             PreparedStatement ps = conn.prepareStatement(QUERY)) {
            ps.setInt(1, telefono);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    nombre=rs.getString("Nombre")+" "+rs.getString("Apellidos");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombre;
    }

    @Override
    public boolean agregarCuentaBancaria(int cuentaUsuario, double saldo) {
        final String QUERY = "INSERT INTO CuentasBancarias (IdCuentaUsuario, Saldo) VALUES (?, ?)";
        boolean exito=false;
        try (Connection conn = conectarBD(URL_BD)){
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(QUERY)) {
                ps.setInt(1, cuentaUsuario);
                ps.setDouble(2, saldo);
                ps.executeUpdate();
                conn.commit();
                exito=true;
            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }
}