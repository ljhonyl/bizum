package es.tiernoparla.bizum;

import es.tiernoparla.bizum.controlador.BizumController;
import javafx.application.Application;

public class App{
    public static void main(String[] args) {
        BizumController bizumController = new BizumController();
        Application.launch(BizumController.class,args);
    }
}