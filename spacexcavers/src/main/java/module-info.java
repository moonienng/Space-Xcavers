module com.juridico {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.juridico to javafx.fxml;
    exports com.juridico;
}
