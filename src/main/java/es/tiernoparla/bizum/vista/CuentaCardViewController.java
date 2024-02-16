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
        setImagen(cuenta);
    }
    /*--------------------------------------------------------------------*/

    /**
     * Imagen a mostrar si la cuenta esta elegida como cuenta con bizum
     * @param cuenta
     */
    public void setImagen(CuentaBancaria cuenta) {
        System.out.println(cuenta.getEsBizum());
        if(cuenta.getEsBizum()){
            System.out.println(cuenta.getEsBizum());
            Image imagen=new Image(getClass().getResourceAsStream(IView.IMAGEN_BIZUM));
            ivBizum.setImage(imagen);
        }
    }
}
