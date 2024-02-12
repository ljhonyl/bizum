package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CuentaCardViewController {
    @FXML
    private Label field1Label;
    @FXML
    private Label field2Label;

    public void initialize(CuentaBancaria cuenta) {
        field1Label.setText("NumCuenta: " + cuenta.getNumCuenta());
        field2Label.setText("Saldo: " + cuenta.getSaldo());
    }
}