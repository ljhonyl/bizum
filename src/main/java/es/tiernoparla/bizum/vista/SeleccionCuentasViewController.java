package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;

public class SeleccionCuentasViewController extends ViewController{

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnOk;

    @FXML
    private TableColumn<?, ?> colNumCuenta;

    @FXML
    private TableColumn<?, ?> colSaldo;

    @FXML
    private TableView<?> tblCuentasBancarias;

    @FXML
    void cancelar(MouseEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void seleccionarCuenta(MouseEvent event) {

    }

    private ObservableList cuentasBancarias;

    @FXML
    void  initialize(){
        cuentasBancarias= FXCollections.observableArrayList();

        colNumCuenta.setCellValueFactory(new PropertyValueFactory<>("numCuenta"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        cargarCuentas(bizumController.getCuentasBancarias());
    }

    public void cargarCuentas(List<CuentaBancaria> cuentasBancarias){
        this.cuentasBancarias.addAll(cuentasBancarias);
        this.tblCuentasBancarias.setItems(this.cuentasBancarias);
    }
}
