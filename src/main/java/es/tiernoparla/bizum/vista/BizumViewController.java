package es.tiernoparla.bizum.vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BizumViewController extends ViewController{

    @FXML
    private TextField txfCantidad;

    @FXML
    private TextField txfNumTel;
    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnConfirmar;

    @FXML
    void cancelar(MouseEvent event) {
        numTel=-1;
        cantidad=-1;
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirmar(MouseEvent event) {
        if(!txfNumTel.getText().toString().equals("") && !txfCantidad.getText().toString().equals("")){
            try{
                numTel=Integer.parseInt(txfNumTel.getText().toString());
                cantidad=Double.parseDouble(txfCantidad.getText().toString());
                if(cantidad>0){
                    Stage stage = (Stage) btnConfirmar.getScene().getWindow();
                    stage.close();
                }
                else{
                    mostrarMensaje("ERROR","Compruebe la cantidad introducida");
                }
            }
            catch (NumberFormatException e){
                mostrarMensaje("ERROR","Revise los datos introducidos");
            }
        }
    }
    /*----------------------------------------------------*/

    private int numTel;
    private double cantidad;

    public int getNumTel(){
        return  numTel;
    }

    public double getCantidad(){
        return cantidad;
    }
}