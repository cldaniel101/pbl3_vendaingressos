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

/**
 * Classe responsável por exibir a tela de atualização de dados do usuário.
 * Permite alterar o nome e o email do usuário logado.
 */
public class AtualizarDadosView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    /**
     * Construtor da classe AtualizarDadosView.
     *
     * @param stage         Janela principal onde a tela será exibida.
     * @param usuario       Usuário atualmente logado.
     * @param controller    Controlador responsável por gerenciar as ações.
     * @param armazenamento Classe responsável pelo gerenciamento de dados.
     */
    public AtualizarDadosView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
    }

    /**
     * Exibe a tela de atualização de dados do usuário.
     */
    public void show() {
        // Layout principal
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

        // Botões de ação
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

        // Configura a cena e exibe a janela
        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.setTitle(TranslationManager.getInstance().get("update.profile"));

        // Listener para mudanças de idioma
        TranslationManager.getInstance().addLanguageChangeListener(this::refreshUI);

        stage.show();
    }

    /**
     * Atualiza os textos da interface conforme o idioma configurado.
     */
    private void refreshUI() {
        stage.setTitle(TranslationManager.getInstance().get("update.profile"));
        // Atualizações adicionais de UI podem ser implementadas aqui.
    }

    /**
     * Salva os dados atualizados do usuário.
     *
     * @param nome  Novo nome do usuário.
     * @param email Novo email do usuário.
     */
    private void salvarDados(String nome, String email) {
        // Verifica se os campos estão preenchidos
        if (nome.isEmpty() || email.isEmpty()) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.empty.fields"));
            return;
        }

        try {
            // Atualiza os dados do usuário usando o controlador
            controller.NovoCadastroUsuario(usuario, usuario.getLogin(), usuario.getSenha(), nome, email, armazenamento);
            showAlert(TranslationManager.getInstance().get("success"), TranslationManager.getInstance().get("data.updated.success"));
            new AppScreenView(stage, usuario, controller, armazenamento).show();
        } catch (Exception e) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.updating.data"));
        }
    }

    /**
     * Exibe uma mensagem de alerta para o usuário.
     *
     * @param title   Título do alerta.
     * @param message Mensagem do alerta.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
