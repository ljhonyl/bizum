package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.App;
import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.util.List;

public class CuentasViewController extends ViewController{
    @FXML
    private ListView<CuentaBancaria> cuentasBancarias;

    public void initialize(List<CuentaBancaria> cuentas) {
        cuentasBancarias.setCellFactory(new Callback<ListView<CuentaBancaria>, javafx.scene.control.ListCell<CuentaBancaria>>() {
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
        cuentasBancarias.getItems().addAll(cuentas);
    }
}