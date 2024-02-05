package es.tiernoparla.bizum.controlador;

import es.tiernoparla.bizum.App;
import es.tiernoparla.bizum.modelo.CuentaUsuario;
import es.tiernoparla.bizum.modelo.basedatos.MiBancoDAO;
import es.tiernoparla.bizum.vista.IView;
import es.tiernoparla.bizum.vista.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class BizumController extends Application {
    private Stage currentStage;
    private MiBancoDAO miBancoDAO;

    public BizumController() {
        miBancoDAO=new MiBancoDAO();
    }

    public ViewController cargarVista(String ficheroView) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(ficheroView));
        Parent root = (Parent) fxmlLoader.load();
        ViewController viewController = fxmlLoader.<ViewController>getController();
        viewController.setBizumController(this);
        Scene scene = new Scene(root);
        currentStage.close();
        currentStage.setScene(scene);
        currentStage.show();
        return viewController;
    }

    @Override
    public void start(Stage arg0) throws Exception {
        currentStage=arg0;
        cargarVista(IView.VISTA_LOGIN);
    }

    /**
     * Llama al metodo del modelo que permite que se agregue una cuenta de usuario
     * en la base de datos
     * @param cuentaUsuario datos recogidos desde la vista
     */
    public boolean addCuentaUsuario(CuentaUsuario cuentaUsuario) {
        boolean exito= miBancoDAO.agregarCuentaUsuario(cuentaUsuario);
        return exito;
    }


    public String comprobarContrasena(String contrasena) {
        return miBancoDAO.comprobarContrasena(contrasena);
    }
}
