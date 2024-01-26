module es.tiernoparla.bizum {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens es.tiernoparla.bizum to javafx.fxml;
    exports es.tiernoparla.bizum;
}