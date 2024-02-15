package es.tiernoparla.bizum.vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

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
        if (!(estarVacio(txfDni)|| estarVacio(txfPassword))) {
            List<String> datos=bizumController.comprobarContrasena(txfDni.getText().toString());
            if(datos.size()>0){
                int idUsuario=Integer.parseInt(datos.get(0));
                String contrasena=datos.get(1);
                if(txfPassword.getText().toString().equals(contrasena)){
                    bizumController.setIdUsuario(idUsuario);
                    bizumController.cargarVista(IView.VISTA_MENU);
                }
                else{
                    mostrarMensaje("ERROR","Datos de acceso incorrectos");
                }
            }
            else{
                mostrarMensaje("ERROR","El DNI no esta registrado en la base de datos");
            }
        }
        //bizumController.cargarVista(IView.VISTA_MENU);
    }

    @FXML
    void irPantallaRegistro(MouseEvent event) throws IOException {
        bizumController.cargarVista(IView.VISTA_REGISTRO);
    }

    /*--------------------------------------------------------------------------*/
    private String cifrar(){

        return null;
    }
}
