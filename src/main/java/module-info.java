module com.example.pbl3_test {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.prefs;

    opens com.example.pbl3_test to javafx.fxml, com.google.gson;
    exports com.example.pbl3_test;
    exports com.example.pbl3_test.views;
}