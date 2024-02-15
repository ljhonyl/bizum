package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.App;
import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class CuentasViewController extends ViewController{

    @FXML
    private Button btnAddCuenta;

    @FXML
    private Button btnSeleccionarCuentaBizum;

    @FXML
    private Button btnVolver;

    @FXML
    private ListView<CuentaBancaria> listViewCuentas;

    @FXML
    void seleccionarCuentaBizum(MouseEvent event) throws IOException{
        if(numCuenta!=-1){
            bizumController.seleccionarCuentaBizum(numCuenta);
            bizumController.cargarVista(IView.VISTA_MENU);
        }
        else{
            mostrarMensaje("ERROR","Seleccione una cuenta");
        }
    }

    @FXML
    void volver(MouseEvent event) throws IOException {
        numCuenta=-1;
        bizumController.cargarVista(IView.VISTA_MENU);
    }

    @FXML
    void addCuenta(MouseEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(IView.VISTA_ADD_CUENTA_BANCARIA));
        Parent root = (Parent) fxmlLoader.load();
        AltaCuentaBancariaViewController altaCuentaBancariaViewController = (AltaCuentaBancariaViewController) fxmlLoader.<ViewController>getController();
        Stage ventanaSecundaria = new Stage();
        ventanaSecundaria.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        ventanaSecundaria.setScene(scene);
        ventanaSecundaria.showAndWait();
        if(altaCuentaBancariaViewController.getSaldo()!=null){
            bizumController.addCuentaBancaria(altaCuentaBancariaViewController.getSaldo());
            mostrarMensaje("INFO","Cuenta dada de alta");
            List<CuentaBancaria> cuentas= bizumController.getCuentasBancarias();
            listViewCuentas.getItems().add(cuentas.getLast());
        }
    }

    @FXML
    void initialize(List<CuentaBancaria> cuentas) {
        listViewCuentas.setCellFactory(new Callback<ListView<CuentaBancaria>, javafx.scene.control.ListCell<CuentaBancaria>>() {
            @Override
            public javafx.scene.control.ListCell<CuentaBancaria> call(ListView<CuentaBancaria> param) {
                return new javafx.scene.control.ListCell<CuentaBancaria>() {
                    @Override
                    protected void updateItem(CuentaBancaria cuenta, boolean empty) {
                        super.updateItem(cuenta, empty);
                        if (empty || cuenta == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            try {
                                FXMLLoader loader = new FXMLLoader(App.class.getResource("vista/CuentaCardView.fxml"));
                                AnchorPane cardPane = loader.load();
                                CuentaCardViewController controller = loader.getController();
                                controller.initialize(cuenta); // Asigna título y descripción
                                setGraphic(cardPane);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        });

        // Agrega un listener para la selección de la lista
        listViewCuentas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            CuentaBancaria cuenta = newValue;
            numCuenta=cuenta.getNumCuenta();
        });

        listViewCuentas.getItems().addAll(cuentas);
    }

    /*------------------------------------------------------------------------*/

    private int numCuenta;

}
