package es.tiernoparla.bizum.modelo;

import jakarta.persistence.*;

@Entity
@Table(name="CuentasUsuarios")
public class CuentaUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Id")
    private int id;
    @Column(name="Dni", nullable = false)
    private String dni;
    @Column(name="Nombre", nullable = false)
    private String nombre;
    @Column(name = "Apellidos", nullable = false)
    private String apellidos;
    @Column(name="Telefono", nullable = false)
    private int telefono;
    @Column(name="Contrasena", nullable = false)
    private String contrasena;
    @ManyToOne
    @JoinColumn(name = "CuentaBizum", referencedColumnName ="NumCuenta")
    private CuentaBancaria cuentaBizum;


    public CuentaUsuario(){}

    public CuentaUsuario(String dni, String nombre, String apellidos, int telefono, String contrasena){
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.contrasena = contrasena;
    }

    public CuentaUsuario(int id){
        this.id=id;
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
    public CuentaBancaria getCuentaBizum() {
        return cuentaBizum;
    }
    public void setCuentaBizum(CuentaBancaria cuentaBizum) {
        this.cuentaBizum = cuentaBizum;
    }
}
