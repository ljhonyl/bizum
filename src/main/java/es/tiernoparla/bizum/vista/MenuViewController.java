
package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.App;
import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MenuViewController extends ViewController{

    @FXML
    private Button btnBizum;

    @FXML
    private Button btnCuentas;

    @FXML
    private Button btnIngresar;

    @FXML
    private Button btnRetirar;

    @FXML
    private TextArea txaCantidad;

    @FXML
    void Ingresar(MouseEvent event) throws IOException {
        bizumController.crearVentanaSecundaria();
    }

    @FXML
    void Retirar(MouseEvent event) {

    }

    @FXML
    void irBizum(MouseEvent event) {

    }

    @FXML
    void irCuentas(MouseEvent event) throws IOException{

    }

}
