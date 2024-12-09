package com.example.pbl3_test.views;

import com.example.pbl3_test.Armazenamento;
import com.example.pbl3_test.Controller;
import com.example.pbl3_test.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AtualizarDadosView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    public AtualizarDadosView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
    }

    public void show() {
        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #4caf50; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Título
        Label tituloLabel = new Label(TranslationManager.getInstance().get("update.profile"));
        tituloLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #4caf50;");
        GridPane.setColumnSpan(tituloLabel, 2);
        layout.add(tituloLabel, 0, 0);

        // Campo de nome
        Label nomeLabel = new Label(TranslationManager.getInstance().get("name") + ":");
        nomeLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        TextField nomeField = new TextField(usuario.getNome());
        nomeField.setPromptText(TranslationManager.getInstance().get("name"));
        layout.add(nomeLabel, 0, 1);
        layout.add(nomeField, 1, 1);

        // Campo de email
        Label emailLabel = new Label(TranslationManager.getInstance().get("email") + ":");
        emailLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        TextField emailField = new TextField(usuario.getEmail());
        emailField.setPromptText(TranslationManager.getInstance().get("email"));
        layout.add(emailLabel, 0, 2);
        layout.add(emailField, 1, 2);

        // Botões
        Button salvarButton = new Button(TranslationManager.getInstance().get("save"));
        salvarButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold;");
        salvarButton.setOnAction(event -> salvarDados(nomeField.getText().trim(), emailField.getText().trim()));

        Button cancelarButton = new Button(TranslationManager.getInstance().get("cancel"));
        cancelarButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelarButton.setOnAction(event -> new AppScreenView(stage, usuario, controller, armazenamento).show());

        HBox buttonBox = new HBox(10, salvarButton, cancelarButton);
        buttonBox.setAlignment(Pos.CENTER);
        GridPane.setColumnSpan(buttonBox, 2);
        layout.add(buttonBox, 0, 3);

        // Cena
        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.setTitle(TranslationManager.getInstance().get("update.profile"));

        // Listener para mudanças de idioma
        TranslationManager.getInstance().addLanguageChangeListener(this::refreshUI);

        stage.show();
    }

    private void refreshUI() {
        // Atualiza os textos conforme o idioma muda
        stage.setTitle(TranslationManager.getInstance().get("update.profile"));
        // Implementação da atualização dos textos omitida para foco no design
    }

    private void salvarDados(String nome, String email) {
        if (nome.isEmpty() || email.isEmpty()) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.empty.fields"));
            return;
        }

        try {
            controller.NovoCadastroUsuario(usuario, usuario.getLogin(), usuario.getSenha(), nome, email, armazenamento);
            showAlert(TranslationManager.getInstance().get("success"), TranslationManager.getInstance().get("data.updated.success"));
            new AppScreenView(stage, usuario, controller, armazenamento).show();
        } catch (Exception e) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.updating.data"));
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
