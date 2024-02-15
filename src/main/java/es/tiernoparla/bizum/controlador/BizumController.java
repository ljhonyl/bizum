package es.tiernoparla.bizum.controlador;

import es.tiernoparla.bizum.App;
import es.tiernoparla.bizum.modelo.CuentaBancaria;
import es.tiernoparla.bizum.modelo.CuentaUsuario;
import es.tiernoparla.bizum.modelo.basedatos.MiBancoDAO;
import es.tiernoparla.bizum.vista.IView;
import es.tiernoparla.bizum.vista.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class BizumController extends Application {
    private int idUsuario;
    private Stage currentStage;
    private MiBancoDAO miBancoDAO;

    public BizumController() {
        miBancoDAO=new MiBancoDAO();
    }

    /**
     * Se carga la ventana principal
     * @param ficheroView ruta del fxml
     * @throws IOException
     */
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

    /**
     * Se crea una ventana secundaria con padre currentStage
     * @throws IOException
     */
    public ViewController crearVentanaSecundaria() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(IView.VISTA_SELECCION_CUENTAS));
        Parent root = (Parent) fxmlLoader.load();
        ViewController viewController = fxmlLoader.<ViewController>getController();
        Stage ventanaSecundaria = new Stage();
        ventanaSecundaria.initModality(Modality.WINDOW_MODAL);
        ventanaSecundaria.initOwner(currentStage);
        Scene scene = new Scene(root);
        ventanaSecundaria.setScene(scene);
        ventanaSecundaria.show();
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

    /**
     * Llama al modelo y le pasa el dni. Retorna la contraseña asociada a ese dni
     * @param dni variable necesaria para ejecutar el metodo en el modelo
     * @return devuelve la contraseña asociada al dni
     */
    public List<String> comprobarContrasena(String dni) {
        return miBancoDAO.comprobarContrasena(dni);
    }

    public List<CuentaBancaria> getCuentasBancarias() {
        return miBancoDAO.getCuentasBancarias(idUsuario);
    }

    public int getIdUsuario(){
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario){
        this.idUsuario=idUsuario;
    }

    public void retirar(int numeroCuenta, double cantidad) {
        miBancoDAO.retirar(numeroCuenta,cantidad);
    }

    public int hacerBizum(int numero, double cantidad) {
        return miBancoDAO.hacerBizum(idUsuario,cantidad,numero);
    }

    public void ingresar(int numCuenta, double dineroARetirar) {
        miBancoDAO.ingresar(numCuenta,dineroARetirar);
    }

    public void seleccionarCuentaBizum(int numeroCuenta) {
        miBancoDAO.seleccionarCuentaBizum(numeroCuenta, idUsuario);
    }

    public String getNombreBeneficiario(int numero) {
        return  miBancoDAO.getNombreBeneficiario(numero);
    }

    public void addCuentaBancaria(Double saldo) {
        miBancoDAO.addCuentaBancaria(saldo, idUsuario);
    }
}