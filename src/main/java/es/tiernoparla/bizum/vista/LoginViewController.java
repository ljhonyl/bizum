package es.tiernoparla.bizum.vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginViewController extends ViewController{

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnRegistrase;

    @FXML
    private TextField txfDni;

    @FXML
    private TextField txfPassword;
    @FXML
    void irPantallaMenu(MouseEvent event) throws IOException {
        /*if (!(estarVacio(txfDni)|| estarVacio(txfPassword))) {
            String contrasena=bizumController.comprobarContrasena(txfDni.getText().toString());
            if(txfPassword.getText().toString().equals(contrasena)){

            }
            else{
                mostrarMensaje("ERROR","Datos de acceso incorrectos");
            }
        }*/
        bizumController.cargarVista(IView.VISTA_MENU);
    }

    @FXML
    void irPantallaRegistro(MouseEvent event) throws IOException {
        bizumController.cargarVista(IView.VISTA_REGISTRO);
    }
}
