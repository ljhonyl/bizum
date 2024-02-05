package es.tiernoparla.bizum.vista;

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

    private void mostrarMensaje(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void crearCuenta() throws IOException {
        if(!(serNulo(txfDni) || serNulo(txfNombre) || serNulo(txfApellidos) || serNulo(txfTelefono) || serNulo(txfContrasena))){
            mostrarMensaje("AVISO","Cuenta Registrada");
            bizumController.cargarVista(IView.VISTA_LOGIN);
        }
        else{
            mostrarMensaje("ERROR", "Faltan campos obligatorios");
        }
    }

    private boolean serNulo(TextField cajaTexto){
        boolean nulo=true;
        if(!cajaTexto.getText().toString().equals("")){
            nulo=false;
        }
        return nulo;
    }
}