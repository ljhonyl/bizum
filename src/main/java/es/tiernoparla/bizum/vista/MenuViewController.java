package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.App;
import es.tiernoparla.bizum.modelo.CuentaBancaria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MenuViewController extends ViewController{

    @FXML
    private Button btnBizum;

    @FXML
    private Button btnCuentas;

    @FXML
    private Button btnIngresar;

    @FXML
    private Button btnRetirar;

    @FXML
    private TextArea txaCantidad;

    @FXML
    void Ingresar(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(IView.VISTA_SELECCION_CUENTAS));
        Parent root = (Parent) fxmlLoader.load();
        SeleccionCuentasViewController seleccionCuentasViewController = (SeleccionCuentasViewController) fxmlLoader.<ViewController>getController();
        seleccionCuentasViewController.cargarCuentas(cargarCuentas());
        Stage ventanaSecundaria = new Stage();
        ventanaSecundaria.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        ventanaSecundaria.setScene(scene);
        ventanaSecundaria.showAndWait();
        if(seleccionCuentasViewController.getCuenta()!=null){
            CuentaBancaria cuenta= seleccionCuentasViewController.getCuenta();
            if(!txaCantidad.getText().toString().equals("")){
                try{
                    double dineroARetirar=Double.parseDouble(txaCantidad.getText().toString());
                    if(dineroARetirar>0){
                        bizumController.ingresar(cuenta.getNumCuenta(),dineroARetirar);
                        mostrarMensaje("INFO","Ha ingresado "+dineroARetirar+" euros");
                    }
                    else{
                        mostrarMensaje("ERROR","Error con la cantidad introducida");
                    }
                }
                catch (NumberFormatException e){
                    mostrarMensaje("ERROR","Error con la cantidad introducida");
                }

            }
            else{
                mostrarMensaje("ERROR","Error con la cantidad introducida");
            }
        }
        else{
            System.out.println("Cuenta sin seleccionar");
        }
    }

    @FXML
    void Retirar(MouseEvent event) throws  IOException{
        //SeleccionCuentasViewController seleccionCuentasViewController= (SeleccionCuentasViewController) bizumController.crearVentanaSecundaria();
        //seleccionCuentasViewController.cargarCuentas(cargarCuentas());
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(IView.VISTA_SELECCION_CUENTAS));
        Parent root = (Parent) fxmlLoader.load();
        SeleccionCuentasViewController seleccionCuentasViewController = (SeleccionCuentasViewController) fxmlLoader.<ViewController>getController();
        seleccionCuentasViewController.cargarCuentas(cargarCuentas());
        Stage ventanaSecundaria = new Stage();
        ventanaSecundaria.initModality(Modality.WINDOW_MODAL);
        //ventanaSecundaria.initOwner(btnRetirar.getParent());
        Scene scene = new Scene(root);
        ventanaSecundaria.setScene(scene);
        ventanaSecundaria.showAndWait();
        if(seleccionCuentasViewController.getCuenta()!=null){
            CuentaBancaria cuenta= seleccionCuentasViewController.getCuenta();
            if(!txaCantidad.getText().toString().equals("")){
                try{
                    double dineroARetirar=Double.parseDouble(txaCantidad.getText().toString());
                    if(dineroARetirar>0){
                        if(dineroARetirar<=cuenta.getSaldo()){
                            bizumController.retirar(cuenta.getNumCuenta(),dineroARetirar);
                            mostrarMensaje("INFO","Ha retirado "+dineroARetirar+" euros");
                        }
                        else{
                            mostrarMensaje("ERROR","No dispone del saldo suficiente");
                        }
                    }
                    else{
                        mostrarMensaje("ERROR","Error con la cantidad introducida");
                    }
                }
                catch (NumberFormatException e){
                    mostrarMensaje("ERROR","Error con la cantidad introducida");
                }

            }
            else{
                mostrarMensaje("ERROR","Error con la cantidad introducida");
            }
        }
        else{
            System.out.println("Cuenta sin seleccionar");
        }

    }

    @FXML
    void irBizum(MouseEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(IView.VISTA_BIZUM));
        Parent root = (Parent) fxmlLoader.load();
        BizumViewController bizumViewController = (BizumViewController) fxmlLoader.<ViewController>getController();
        Stage ventanaSecundaria = new Stage();
        ventanaSecundaria.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        ventanaSecundaria.setScene(scene);
        ventanaSecundaria.showAndWait();

        if(bizumViewController.getNumTel()>0 && bizumViewController.getCantidad()>0){
            int numero=bizumViewController.getNumTel();
            double cantidad=bizumViewController.getCantidad();
            String nombre=bizumController.getNombreBeneficiario(numero);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("INFO");
            alert.setHeaderText("Desea hacer un bizum de "+cantidad+" a "+nombre);

            // Configurar los botones
            ButtonType buttonTypeOK = new ButtonType("Aceptar");
            ButtonType buttonTypeCancel = new ButtonType("Cancelar");
            alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

            // Mostrar la alerta y esperar la respuesta del usuario
            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeOK) {
                    int exito=bizumController.hacerBizum(numero,cantidad);
                    if(exito==1){
                        bizumController.getNombreBeneficiario(numero);
                        mostrarMensaje("INFO","Bizum realizado correctamente");
                    }
                    else if(exito==0){
                        mostrarMensaje("INFO","No dispone del saldo necesario");
                    } else if (exito==-1) {
                        mostrarMensaje("ERROR","Error al relizar el bizum asegurese de que emisor y beneficiario disponen de una cuenta con bizum");
                    }
                } else if (response == buttonTypeCancel) {
                    mostrarMensaje("INFO","Operacion cancelada");
                }
            });

        }
    }

    @FXML
    void irCuentas(MouseEvent event) throws IOException{
        CuentasViewController cuentasViewController= (CuentasViewController) bizumController.cargarVista(IView.VISTA_CUENTAS);
        cuentasViewController.initialize(cargarCuentas());
    }

    @FXML
    void initialize(){
        limitarCaracteres(txaCantidad,4);
    }

    private ObservableList<CuentaBancaria> cuentasBancarias;

    public List<CuentaBancaria> cargarCuentas(){
        return bizumController.getCuentasBancarias();
    }
}