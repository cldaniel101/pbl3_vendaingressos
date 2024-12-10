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

/**
 * Classe responsável por exibir a tela de confirmação de login.
 * Esta tela permite que o usuário insira seu CPF e senha para autenticar no sistema.
 */
public class ConfirmarLoginView {

    private final Stage stage; // Janela principal da aplicação.
    private final Usuario usuario; // Usuário atualmente autenticado.
    private final Controller controller; // Controlador da aplicação.
    private final Armazenamento armazenamento; // Gerenciador de armazenamento de dados.

    /**
     * Construtor da classe.
     *
     * @param stage        a janela principal da aplicação.
     * @param usuario      o usuário atualmente autenticado.
     * @param controller   o controlador da aplicação.
     * @param armazenamento o gerenciador de armazenamento de dados.
     */
    public ConfirmarLoginView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
    }

    /**
     * Método responsável por exibir a tela de confirmação de login.
     */
    public void show() {
        // Criação do layout principal da tela
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

        // Campos de entrada: CPF e senha
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

        // Botões de ação
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

        // Configuração da cena
        Scene scene = new Scene(layout, 700, 500);
        stage.setScene(scene);
        stage.setTitle(TranslationManager.getInstance().get("login.confirmation"));
        stage.show();
    }

    /**
     * Método responsável por confirmar o login com os dados fornecidos.
     *
     * @param cpf   o CPF do usuário.
     * @param senha a senha do usuário.
     * @throws IOException caso ocorra um erro de acesso ao armazenamento.
     */
    private void confirmarLogin(String cpf, String senha) throws IOException {
        if (cpf.isEmpty() || senha.isEmpty()) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.empty.fields"));
            return;
        }

        // Verificação de credenciais
        if (controller.loginUsuario(cpf, senha, armazenamento)) {
            new AtualizarDadosView(stage, usuario, controller, armazenamento).show();
        } else {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.invalid.credentials"));
        }
    }

    /**
     * Exibe uma janela de alerta com uma mensagem.
     *
     * @param title   o título do alerta.
     * @param message a mensagem do alerta.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
