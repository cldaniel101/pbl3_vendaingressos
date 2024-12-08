package com.example.pbl3_test.views;

import com.example.pbl3_test.Armazenamento;
import com.example.pbl3_test.Controller;
import com.example.pbl3_test.Usuario;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginRegisterView {
    private final Controller controller = new Controller();
    private final Armazenamento armazenamentoDados = new Armazenamento();
    private final Stage stage;

    // Componentes de UI para serem atualizados
    private Label languageLabel;
    private ComboBox<String> languageComboBox;
    private Label userLabel;
    private Label passLabel;
    private Button loginButton;
    private Label feedbackLabel;
    private Label registrationLabel;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField nameField;
    private TextField cpfField;
    private TextField emailField;
    private Button submitButton;
    private Label registrationFeedback;

    public LoginRegisterView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        TranslationManager.getInstance().loadLanguagePreference();

        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: #f5f7fa; -fx-padding: 20;");

        Label titleLabel = new Label("IngressOnline");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #4a90e2;");

        HBox languageBox = createLanguageSelector();
        HBox formBox = createFormBox();

        mainLayout.getChildren().addAll(titleLabel, languageBox, formBox);

        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setTitle(TranslationManager.getInstance().get("app.title"));
        stage.setScene(scene);
        stage.show();
    }

    private HBox createLanguageSelector() {
        languageLabel = new Label(TranslationManager.getInstance().get("select.language"));
        languageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("pt", "en");
        languageComboBox.setValue(TranslationManager.getInstance().getCurrentLanguage());
        languageComboBox.setStyle("-fx-padding: 5;");

        languageComboBox.setOnAction(event -> {
            String selectedLanguage = languageComboBox.getValue();
            TranslationManager.getInstance().setLanguage(selectedLanguage);
            refreshUI();
        });

        HBox languageBox = new HBox(10);
        languageBox.setAlignment(Pos.CENTER);
        languageBox.getChildren().addAll(languageLabel, languageComboBox);
        return languageBox;
    }

    private HBox createFormBox() {
        VBox loginBox = createLoginBox();
        VBox registerBox = createRegisterBox();

        HBox formBox = new HBox(40);
        formBox.setAlignment(Pos.CENTER);
        formBox.setStyle("-fx-padding: 20;");
        formBox.getChildren().addAll(loginBox, registerBox);
        return formBox;
    }

    private VBox createLoginBox() {
        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #4a90e2; -fx-border-radius: 8; -fx-padding: 20; -fx-border-width: 2; -fx-background-radius: 8;");

        userLabel = new Label(TranslationManager.getInstance().get("login.cpf"));
        userLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
        TextField userField = new TextField();
        userField.setStyle("-fx-padding: 8; -fx-border-color: #ccc; -fx-border-radius: 4;");

        passLabel = new Label(TranslationManager.getInstance().get("login.password"));
        passLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
        PasswordField passField = new PasswordField();
        passField.setStyle("-fx-padding: 8; -fx-border-color: #ccc; -fx-border-radius: 4;");

        loginButton = new Button(TranslationManager.getInstance().get("login.button"));
        loginButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 6;");
        feedbackLabel = new Label();

        loginBox.getChildren().addAll(userLabel, userField, passLabel, passField, loginButton, feedbackLabel);

        loginButton.setOnAction(event -> handleLogin(userField, passField, feedbackLabel));
        return loginBox;
    }

    private VBox createRegisterBox() {
        VBox registerBox = new VBox(15);
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #4a90e2; -fx-border-radius: 8; -fx-padding: 20; -fx-border-width: 2; -fx-background-radius: 8;");

        registrationLabel = new Label(TranslationManager.getInstance().get("register.new.user"));
        registrationLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        usernameField = new TextField();
        usernameField.setPromptText(TranslationManager.getInstance().get("register.username"));
        usernameField.setStyle("-fx-padding: 8; -fx-border-color: #ccc; -fx-border-radius: 4;");

        passwordField = new PasswordField();
        passwordField.setPromptText(TranslationManager.getInstance().get("register.password"));
        passwordField.setStyle("-fx-padding: 8; -fx-border-color: #ccc; -fx-border-radius: 4;");

        nameField = new TextField();
        nameField.setPromptText(TranslationManager.getInstance().get("register.full.name"));
        nameField.setStyle("-fx-padding: 8; -fx-border-color: #ccc; -fx-border-radius: 4;");

        cpfField = new TextField();
        cpfField.setPromptText(TranslationManager.getInstance().get("register.cpf"));
        cpfField.setStyle("-fx-padding: 8; -fx-border-color: #ccc; -fx-border-radius: 4;");

        emailField = new TextField();
        emailField.setPromptText(TranslationManager.getInstance().get("register.email"));
        emailField.setStyle("-fx-padding: 8; -fx-border-color: #ccc; -fx-border-radius: 4;");

        submitButton = new Button(TranslationManager.getInstance().get("register.button"));
        submitButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 6;");

        registrationFeedback = new Label();

        registerBox.getChildren().addAll(registrationLabel, usernameField, passwordField, nameField, cpfField, emailField, submitButton, registrationFeedback);

        submitButton.setOnAction(event -> handleRegistration(usernameField, passwordField, nameField, cpfField, emailField, registrationFeedback));
        submitButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) handleRegistration(usernameField, passwordField, nameField, cpfField, emailField, registrationFeedback);
        });

        return registerBox;
    }


    private void handleLogin(TextField userField, PasswordField passField, Label feedbackLabel) {
        String cpf = userField.getText();
        String password = passField.getText();

        if (cpf.isEmpty() || password.isEmpty()) {
            feedbackLabel.setText(TranslationManager.getInstance().get("error.empty.fields"));
            return;
        }

        try {
            if (controller.loginUsuario(cpf, password, armazenamentoDados)) {
                Usuario user = armazenamentoDados.LerArquivoUsuario(cpf);
                new AppScreenView(stage, user, controller, armazenamentoDados).show();
            } else {
                feedbackLabel.setText(TranslationManager.getInstance().get("error.invalid.credentials"));
            }
        } catch (IOException e) {
            feedbackLabel.setText(TranslationManager.getInstance().get("error.accessing.data"));
            e.printStackTrace();
        }
    }

    private void handleRegistration(TextField usernameField, PasswordField passwordField, TextField nameField, TextField cpfField, TextField emailField, Label registrationFeedback) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String cpf = cpfField.getText();
        String email = emailField.getText();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || cpf.isEmpty() || email.isEmpty()) {
            registrationFeedback.setText(TranslationManager.getInstance().get("error.empty.fields"));
            return;
        }

        try {
            controller.cadastrarUsuario(username, password, name, cpf, email, false, armazenamentoDados);
            Usuario user = armazenamentoDados.LerArquivoUsuario(cpf);
            registrationFeedback.setText(TranslationManager.getInstance().get("registration.success"));
            new AppScreenView(stage, user, controller, armazenamentoDados).show();
        } catch (Exception e) {
            registrationFeedback.setText(TranslationManager.getInstance().get("error.registration"));
            e.printStackTrace();
        }
    }

    private void refreshUI() {
        languageLabel.setText(TranslationManager.getInstance().get("select.language"));
        userLabel.setText(TranslationManager.getInstance().get("login.cpf"));
        passLabel.setText(TranslationManager.getInstance().get("login.password"));
        loginButton.setText(TranslationManager.getInstance().get("login.button"));
        registrationLabel.setText(TranslationManager.getInstance().get("register.new.user"));
        usernameField.setPromptText(TranslationManager.getInstance().get("register.username"));
        passwordField.setPromptText(TranslationManager.getInstance().get("register.password"));
        nameField.setPromptText(TranslationManager.getInstance().get("register.full.name"));
        cpfField.setPromptText(TranslationManager.getInstance().get("register.cpf"));
        emailField.setPromptText(TranslationManager.getInstance().get("register.email"));
        submitButton.setText(TranslationManager.getInstance().get("register.button"));
        feedbackLabel.setText("");
        registrationFeedback.setText("");
        stage.setTitle(TranslationManager.getInstance().get("app.title"));
    }
}
