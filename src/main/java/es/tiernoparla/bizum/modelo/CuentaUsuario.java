package es.tiernoparla.bizum.modelo;

public class CuentaUsuario {
    private String dni;
    private String nombre;
    private String segundoNombre;
    private String apellidos;
    private int telefono;
    private String contrasena;
    private String cuentaBizum;

    public CuentaUsuario(String dni, String nombre, String segundoNombre, String apellidos, int telefono, String contrasena){
        this.dni = dni;
        this.nombre = nombre;
        this.segundoNombre = segundoNombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.contrasena = contrasena;
    }

    public CuentaUsuario(String dni, String nombre, String apellidos, int telefono, String contrasena){
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.contrasena = contrasena;
    }

    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getSegundoNombre() {
        return segundoNombre;
    }
    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public int getTelefono() {
        return telefono;
    }
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getCuentaBizum() {
        return cuentaBizum;
    }
    public void setCuentaBizum(String cuentaBizum) {
        this.cuentaBizum = cuentaBizum;
    }
}
