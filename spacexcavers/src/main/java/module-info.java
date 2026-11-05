module com.juridico.spacexcavers {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.juridico.spacexcavers to javafx.fxml;
    exports com.juridico.spacexcavers;
}