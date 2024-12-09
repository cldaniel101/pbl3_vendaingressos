package com.example.pbl3_test.views;

import com.example.pbl3_test.Armazenamento;
import com.example.pbl3_test.Controller;
import com.example.pbl3_test.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;

public class ConfirmarLoginView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    public ConfirmarLoginView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
    }

    public void show() {
        // Criação de um layout principal
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-width: 2px;");

        // Cabeçalho com título
        Label tituloLabel = new Label(TranslationManager.getInstance().get("login.confirmation"));
        tituloLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #333;");
        tituloLabel.setAlignment(Pos.CENTER);

        VBox header = new VBox(tituloLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        layout.setTop(header);

        // Campos de entrada
        Label cpfLabel = new Label(TranslationManager.getInstance().get("enter.cpf"));
        cpfLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #555;");
        TextField cpfField = new TextField();
        cpfField.setPromptText(TranslationManager.getInstance().get("cpf"));
        cpfField.setStyle("-fx-background-color: #fff; -fx-border-radius: 5; -fx-padding: 5;");

        Label senhaLabel = new Label(TranslationManager.getInstance().get("enter.password"));
        senhaLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #555;");
        PasswordField senhaField = new PasswordField();
        senhaField.setPromptText(TranslationManager.getInstance().get("password"));
        senhaField.setStyle("-fx-background-color: #fff; -fx-border-radius: 5; -fx-padding: 5;");

        VBox inputFields = new VBox(10, cpfLabel, cpfField, senhaLabel, senhaField);
        inputFields.setAlignment(Pos.CENTER_LEFT);
        inputFields.setPadding(new Insets(20));
        inputFields.setStyle("-fx-background-color: #fff; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 15;");
        layout.setCenter(inputFields);

        // Botões
        Button confirmarButton = new Button(TranslationManager.getInstance().get("confirm"));
        confirmarButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: #fff; -fx-font-size: 14; -fx-padding: 10; -fx-border-radius: 5;");
        confirmarButton.setOnAction(event -> {
            try {
                confirmarLogin(cpfField.getText().trim(), senhaField.getText());
            } catch (IOException e) {
                showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.accessing.storage"));
            }
        });

        Button cancelarButton = new Button(TranslationManager.getInstance().get("cancel"));
        cancelarButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: #fff; -fx-font-size: 14; -fx-padding: 10; -fx-border-radius: 5;");
        cancelarButton.setOnAction(event -> new AppScreenView(stage, usuario, controller, armazenamento).show());

        HBox buttonBox = new HBox(15, confirmarButton, cancelarButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        layout.setBottom(buttonBox);

        // Criação da cena
        Scene scene = new Scene(layout, 700, 500);
        stage.setScene(scene);
        stage.setTitle(TranslationManager.getInstance().get("login.confirmation"));
        stage.show();
    }

    private void confirmarLogin(String cpf, String senha) throws IOException {
        if (cpf.isEmpty() || senha.isEmpty()) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.empty.fields"));
            return;
        }

        if (controller.loginUsuario(cpf, senha, armazenamento)) {
            new AtualizarDadosView(stage, usuario, controller, armazenamento).show();
        } else {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.invalid.credentials"));
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
