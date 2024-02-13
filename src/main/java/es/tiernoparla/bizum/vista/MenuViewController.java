package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    }

    @FXML
    void Retirar(MouseEvent event) throws  IOException{
        SeleccionCuentasViewController seleccionCuentasViewController= (SeleccionCuentasViewController) bizumController.crearVentanaSecundaria();
        cargarCuentas();
        seleccionCuentasViewController.cargarCuentas(cuentasBancarias);
        CuentaBancaria cuenta= seleccionCuentasViewController.getCuenta();
        double dineroARetirar=Integer.parseInt(txaCantidad.getText().toString());
        if(cuenta.getSaldo()>=dineroARetirar){
            mostrarMensaje("ERROR","No dispone del saldo suficiente");
        }
        else{
            bizumController.retirar(cuenta.getNumCuenta(),dineroARetirar);
        }
    }

    @FXML
    void irBizum(MouseEvent event) {

    }

    @FXML
    void irCuentas(MouseEvent event) throws IOException{
        CuentasViewController cuentasViewController= (CuentasViewController) bizumController.cargarVista(IView.VISTA_CUENTAS);
        cargarCuentas();
        cuentasViewController.initialize(cuentasBancarias);
    }

    private ObservableList<CuentaBancaria> cuentasBancarias;

    public void cargarCuentas(){
        cuentasBancarias= FXCollections.observableArrayList();
        this.cuentasBancarias.addAll(bizumController.getCuentasBancarias());
    }
}