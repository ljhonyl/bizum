package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.App;
import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class CuentasViewController extends ViewController{

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
