module es.tiernoparla.bizum {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens es.tiernoparla.bizum to javafx.fxml;
    exports es.tiernoparla.bizum;
}