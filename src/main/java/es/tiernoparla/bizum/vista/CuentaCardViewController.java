package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CuentaCardViewController extends ViewController{
    @FXML
    private Label lblNumCuenta;

    @FXML
    private Label lblSaldo;

    @FXML
    private ImageView ivBizum;

    @FXML
    void initialize(CuentaBancaria cuenta) {
        lblNumCuenta.setText(String.valueOf(cuenta.getNumCuenta()));
        lblSaldo.setText(String.valueOf(cuenta.getSaldo()));
    }
    /*--------------------------------------------------------------------*/
    public void setImagen(Image imagen) {
        ivBizum.setImage(imagen);
    }
}
