package es.tiernoparla.bizum.controlador;

import es.tiernoparla.bizum.App;
import es.tiernoparla.bizum.modelo.CuentaBancaria;
import es.tiernoparla.bizum.modelo.CuentaUsuario;
import es.tiernoparla.bizum.modelo.basedatos.IMiBancoDAO;
import es.tiernoparla.bizum.modelo.basedatos.MiBancoDAO;
import es.tiernoparla.bizum.modelo.basedatos.hibernate.MiBancoDAOHibernate;
import es.tiernoparla.bizum.modelo.encriptador.HashManager;
import es.tiernoparla.bizum.vista.IView;
import es.tiernoparla.bizum.vista.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class BizumController extends Application {
    private int idUsuario;
    private Stage currentStage;
    private IMiBancoDAO miBancoDAO;
    private HashManager encriptador;

    public BizumController() {
        miBancoDAO=new MiBancoDAO();
        encriptador=new HashManager();
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
    public ViewController mostrarVentanaSecundaria(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        Parent root = (Parent) fxmlLoader.load();
        ViewController viewController =fxmlLoader.<ViewController>getController();
        Stage ventanaSecundaria = new Stage();
        ventanaSecundaria.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        ventanaSecundaria.setScene(scene);
        ventanaSecundaria.showAndWait();
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
        String contrsenaCifrada=encriptador.getDigest(cuentaUsuario.getContrasena());
        cuentaUsuario.setContrasena(contrsenaCifrada);
        return miBancoDAO.agregarCuentaUsuario(cuentaUsuario);
    }

    /**
     * Recoge la contraseña almacenada si se ha introducido un dni registrado
     * y se la pasa al encriptador juntos con lacontraseña introducida para
     * verificar si coinciden
     * @param dni necesario para buscar en la bbdd
     * @param contrasena contraseña introducida
     * @return acceder, 1 si se accede, 0 si las contraseñas no coinciden
     * -1 si el dni no esta almacenado
     */
    public int comprobarContrasena(String dni, String contrasena) {
        int acceder=-1;
        List<String> datos=miBancoDAO.comprobarContrasena(dni);
        if(!datos.isEmpty()){
            String contrasenaGuardada=datos.get(1);
            if(encriptador.compararContrasena(encriptador.getDigest(contrasena),contrasenaGuardada)){
                idUsuario=Integer.parseInt(datos.get(0));
                acceder=1;
            }
            else{
                acceder=0;
            }
        }
        return acceder;
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
        return miBancoDAO.hacerBizum(idUsuario,numero,cantidad);
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

    public boolean addCuentaBancaria(Double saldo) {
        return miBancoDAO.agregarCuentaBancaria(idUsuario, saldo);
    }
}