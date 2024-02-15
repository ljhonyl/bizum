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

    /**
     * Se comprueba que el nombre de usuario este registrado, ademas
     * se comprueba si la contraseña guardada coincide con la contraseña introducida
     * si el dni devuelve una contraseña y esta coincide se cambia de ventana
     * @param event Click del raton
     * @throws IOException Excepcion lanzada que contrala el cargarVista
     */
    @FXML
    void irPantallaMenu(MouseEvent event) throws IOException {
        if (!(estarVacio(txfDni)|| estarVacio(txfPassword))) {
            List<String> datos=bizumController.comprobarContrasena(txfDni.getText().toString());
            if(datos.size()>0){
                int idUsuario=Integer.parseInt(datos.get(0));
                String contrasenaGuardada=datos.get(1);
                String contrasena=bizumController.cifrar(txfPassword.getText().toString());
                if(contrasena.equalsIgnoreCase(contrasenaGuardada)){
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

    /**
     * Carga la ventana de registro
     * @param event
     * @throws IOException Se lanza por si el fxml de la vista no existe en la ruta
     */
    @FXML
    void irPantallaRegistro(MouseEvent event) throws IOException {
        bizumController.cargarVista(IView.VISTA_REGISTRO);
    }

    @FXML
    void initialize(){
        limitarCaracteres(txfDni,9);
        limitarCaracteres(txfPassword,15);
    }
}
