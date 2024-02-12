package es.tiernoparla.bizum.vista;
import es.tiernoparla.bizum.App;
import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.util.List;

public class CuentasViewController extends ViewController{
    @FXML
    private ListView<CuentaBancaria> listView;

    public void initialize() {
        // Obtener las cuentas bancarias asociadas al usuario actual
        List<CuentaBancaria> cuentas = bizumController.getCuentasBancarias();

        // Configura el ListView con el diseño de tarjeta personalizado
        listView.setCellFactory(new Callback<ListView<CuentaBancaria>, ListCell<CuentaBancaria>>() {
            @Override
            public ListCell<CuentaBancaria> call(ListView<CuentaBancaria> param) {
                return new ListCell<CuentaBancaria>() {
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
                                controller.initialize(cuenta); // Configura los datos de la cuenta en la tarjeta
                                setGraphic(cardPane);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        });

        // Configura los elementos de la lista con las cuentas bancarias
        listView.getItems().addAll(cuentas);
    }
}