module es.tiernoparla.bizum {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens es.tiernoparla.bizum to javafx.fxml;
    exports es.tiernoparla.bizum;
    exports es.tiernoparla.bizum.vista;
    opens es.tiernoparla.bizum.vista to javafx.fxml;
    exports es.tiernoparla.bizum.controlador;
    opens es.tiernoparla.bizum.controlador to javafx.fxml;
    opens es.tiernoparla.bizum.modelo to javafx.base;
}