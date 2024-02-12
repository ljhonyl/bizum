package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.App;
import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

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
        cargarLista();
        bizumController.crearVentanaSecundaria();
    }

    @FXML
    void Retirar(MouseEvent event) {

    }

    @FXML
    void irBizum(MouseEvent event) {

    }

    @FXML
    void irCuentas(MouseEvent event) {

    }

    private void cargarLista() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(IView.VISTA_SELECCION_CUENTAS));
        Parent root = (Parent) fxmlLoader.load();
        SeleccionCuentasViewController secondaryController = (SeleccionCuentasViewController) fxmlLoader.<ViewController>getController();
// Suponiendo que cuentasBancarias es la lista que quieres pasar a la ventana secundaria
        List<CuentaBancaria> cuentasBancarias = bizumController.getCuentasBancarias();

// Llamada al método cargarCuentas del controlador de la ventana secundaria
        secondaryController.cargarCuentas(cuentasBancarias);


    }
}
