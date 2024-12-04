module com.example.pbl3_test {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.pbl3_test to javafx.fxml;
    exports com.example.pbl3_test;
}