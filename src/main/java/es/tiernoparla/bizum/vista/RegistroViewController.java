package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.modelo.CuentaUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RegistroViewController extends ViewController{

    @FXML
    private Button btnRegistrase;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField txfApellidos;

    @FXML
    private PasswordField psfPassword;

    @FXML
    private TextField txfDni;

    @FXML
    private TextField txfNombre;

    @FXML
    private TextField txfTelefono;

    @FXML
    void registrarse(MouseEvent event) throws IOException{
        crearCuenta();
    }

    @FXML
    void volver(MouseEvent event) throws IOException{
        bizumController.cargarVista(IView.VISTA_LOGIN);
    }

    @FXML
    void initialize(){
        limitarCaracteres(txfDni, 9);
        limitarCaracteres(txfNombre, 25);
        limitarCaracteres(txfApellidos, 25);
        limitarCaracteres(txfTelefono, 9);
        limitarCaracteres(psfPassword, 15);
    }
    /*-----------------------------------------------------------------------------*/
    private void crearCuenta() throws IOException {
        boolean exito;
        if(!(estarVacio(txfDni) || estarVacio(txfNombre) || estarVacio(txfApellidos) || estarVacio(txfTelefono) || estarVacio(psfPassword))){
            CuentaUsuario cuentaUsuario=new CuentaUsuario(txfDni.getText().toString(),txfNombre.getText().toString(),txfApellidos.getText().toString(),Integer.parseInt(txfTelefono.getText().toString()),psfPassword.getText().toString());
            if (exito= bizumController.addCuentaUsuario(cuentaUsuario)){
                mostrarMensaje("AVISO","Cuenta Registrada");
                bizumController.cargarVista(IView.VISTA_LOGIN);
            }
            else{
                mostrarMensaje("ERROR", "Ocurrio un problema durante la operacion");
            }
        }
        else{
            mostrarMensaje("ERROR", "Faltan campos obligatorios");
        }
    }
}