package es.tiernoparla.bizum.vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.w3c.dom.events.DocumentEvent;

public class AltaCuentaBancariaViewController extends ViewController{

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnConfirmar;

    @FXML
    private TextField txfSaldo;

    @FXML
    void cancelar(MouseEvent event) {
        saldo=null;
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirmar(MouseEvent event) {
        if(!txfSaldo.getText().toString().equals("")){
            try{
                saldo=Double.parseDouble(txfSaldo.getText().toString());
                if(saldo>0){
                    Stage stage = (Stage) btnConfirmar.getScene().getWindow();
                    stage.close();
                }
                else {
                    mostrarMensaje("ERROR", "Compruebe el saldo introducido");
                }
            }
            catch (NumberFormatException e){
                saldo=null;
                mostrarMensaje("ERROR","Compruebe el saldo introducido");
            }
        }
    }

    @FXML
    void initialize(){
        limitarCaracteres(txfSaldo,7);
    }
    /*----------------------------------------------------------------------------*/

    private Double saldo;

    public Double getSaldo() {
        return saldo;
    }
}
