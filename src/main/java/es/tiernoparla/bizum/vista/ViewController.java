package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.controlador.BizumController;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

/**
 * Clase de la que extenderan el resto de vistas
 */
public class ViewController implements IView{

    //Controlador que usaran las distintas vistas
    protected BizumController bizumController;

    public BizumController getBizumController(){
        return bizumController;
    }

    public  void setBizumController(BizumController bizumController){
        this.bizumController=bizumController;
    }

    /**
     * Muestra mensajes de aviso, error o info
     * @param titulo Tipo de aviso
     * @param contenido Texto a mostrar
     */
    protected void mostrarMensaje(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    /**
     * Comprueba si el text field esta vacio
     * @param cajaTexto text field a comprobar
     * @return vacio, si esta vacio o no
     */
    protected boolean estarVacio(TextField cajaTexto){
        boolean vacio=true;
        if(!cajaTexto.getText().toString().equals("")){
            vacio=false;
        }
        return vacio;
    }

    /**
     * Limita los caracteres que se pueden introducir
     * @param textField componente a limitar
     * @param maxLength maximo de caracteres
     */
    protected void limitarCaracteres(TextField textField, int maxLength) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= maxLength) {
                return change;
            } else {
                return null;
            }
        });
        textField.setTextFormatter(formatter);
    }

    protected void limitarCaracteres(TextArea textArea, int maxLength) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= maxLength) {
                return change;
            } else {
                return null;
            }
        });
        textArea.setTextFormatter(formatter);
    }
}
