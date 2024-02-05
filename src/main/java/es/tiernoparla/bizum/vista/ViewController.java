package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.controlador.BizumController;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ViewController implements IView{
    protected BizumController bizumController;

    public BizumController getBizumController(){
        return bizumController;
    }

    public  void setBizumController(BizumController bizumController){
        this.bizumController=bizumController;
    }

    protected void mostrarMensaje(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
    protected boolean estarVacio(TextField cajaTexto){
        boolean vacio=true;
        if(!cajaTexto.getText().toString().equals("")){
            vacio=false;
        }
        return vacio;
    }
}
