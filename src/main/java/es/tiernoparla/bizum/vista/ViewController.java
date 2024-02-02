package es.tiernoparla.bizum.vista;

import es.tiernoparla.bizum.controlador.BizumController;

public class ViewController implements IView{
    protected BizumController bizumController;

    public BizumController getBizumController(){
        return bizumController;
    }

    public  void setBizumController(BizumController bizumController){
        this.bizumController=bizumController;
    }
}
