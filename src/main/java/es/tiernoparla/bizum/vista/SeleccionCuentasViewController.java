package es.tiernoparla.bizum.vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SeleccionCuentasViewController extends ViewController{

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnOk;

    @FXML
    private TableColumn<?,?> tblColNumCuenta;

    @FXML
    void cancelar(MouseEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void seleccionarCuenta(MouseEvent event) {
        mostrarMensaje("BIEN HECHO","BIEN HECHO");
    }

}
