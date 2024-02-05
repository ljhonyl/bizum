package es.tiernoparla.bizum.vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginViewController extends ViewController{

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnRegistrase;

    @FXML
    void irPantallaMenu(MouseEvent event) throws IOException{
        bizumController.cargarVista(IView.VISTA_MENU);
    }

    @FXML
    void irPantallaRegistro(MouseEvent event) throws IOException {
        bizumController.cargarVista(IView.VISTA_REGISTRO);
    }
}
