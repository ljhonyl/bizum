package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.modelo.CuentaUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RegistroViewController extends ViewController{

    @FXML
    private Button btnRegistrase;

    @FXML
    private TextField txfApellidos;

    @FXML
    private TextField txfContrasena;

    @FXML
    private TextField txfDni;

    @FXML
    private TextField txfNombre;

    @FXML
    private TextField txfSegundoNombre;

    @FXML
    private TextField txfTelefono;

    @FXML
    void registrarse(MouseEvent event) throws IOException{
        crearCuenta();
    }



    private void crearCuenta() throws IOException {
        boolean exito;
        if(!(estarVacio(txfDni) || estarVacio(txfNombre) || estarVacio(txfApellidos) || estarVacio(txfTelefono) || estarVacio(txfContrasena))){
            CuentaUsuario cuentaUsuario=new CuentaUsuario(txfDni.getText().toString(),txfNombre.getText().toString(),txfApellidos.getText().toString(),Integer.parseInt(txfTelefono.getText().toString()),txfContrasena.getText().toString());
            if (exito= bizumController.addCuentaUsuario(cuentaUsuario)){
                mostrarMensaje("AVISO","Cuenta Registrada");
                bizumController.cargarVista(IView.VISTA_LOGIN);
            }
            else{
                mostrarMensaje("ERROR", "Ocurrio un problema durante la operacion");
            }
        }
        else{
            mostrarMensaje("ERROR", "Faltan campos obligatorios");
        }
    }

}