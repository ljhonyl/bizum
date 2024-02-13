package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CuentaCardViewController extends ViewController{
    @FXML
    private Label lblNumCuenta;

    @FXML
    private Label lblSaldo;

    public void initialize(CuentaBancaria cuenta) {
        lblNumCuenta.setText(String.valueOf(cuenta.getNumCuenta()));
        lblSaldo.setText(String.valueOf(cuenta.getSaldo()));
    }
}