package es.tiernoparla.bizum.controlador;

import es.tiernoparla.bizum.App;
import es.tiernoparla.bizum.vista.IView;
import es.tiernoparla.bizum.vista.LoginViewController;
import es.tiernoparla.bizum.vista.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BizumController extends Application {
    private Stage currentStage;

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
}
