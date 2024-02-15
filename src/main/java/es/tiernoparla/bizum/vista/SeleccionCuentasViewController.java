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
    private Button btnConfirmar;

    @FXML
    private TableColumn<?, ?> colNumCuenta;

    @FXML
    private TableColumn<?, ?> colSaldo;

    @FXML
    private TableView<?> tblCuentasBancarias;

    @FXML
    void cancelar(MouseEvent event) {
        cuenta=null;
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirmar(MouseEvent event) {
        if(cuenta!=null){
            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
        }
        else{
            mostrarMensaje("ERROR","No ha seleecionado ninguna cuenta");
        }
    }

    @FXML
    void seleccionarCuenta(MouseEvent event) {
        cuenta = (CuentaBancaria) tblCuentasBancarias.getSelectionModel().getSelectedItem();
    }

    @FXML
    void  initialize(){
        cuentasBancarias= FXCollections.observableArrayList();

        colNumCuenta.setCellValueFactory(new PropertyValueFactory<>("NumCuenta"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("Saldo"));
    }

    /*---------------------------*/
    private ObservableList cuentasBancarias;
    private CuentaBancaria cuenta;

    public void cargarCuentas(List<CuentaBancaria> cuentasBancarias){
        this.cuentasBancarias.addAll(cuentasBancarias);
        this.tblCuentasBancarias.setItems(this.cuentasBancarias);
    }

    public CuentaBancaria getCuenta() {
        return cuenta;
    }
}