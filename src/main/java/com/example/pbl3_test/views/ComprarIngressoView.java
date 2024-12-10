package com.example.pbl3_test.views;

import com.example.pbl3_test.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe responsável por exibir a interface de compra de ingressos para um evento.
 * Permite selecionar uma forma de pagamento, confirmar a compra e visualizar os detalhes do evento.
 */
public class ComprarIngressoView {

    private final Stage stage; // Janela principal
    private final Usuario usuario; // Usuário logado
    private final Evento evento; // Evento relacionado
    private final Controller controller; // Controlador para manipular ações
    private final Armazenamento armazenamento; // Objeto de armazenamento
    private final Ingresso ingresso; // Ingresso associado ao evento
    private String selectedPaymentMethod; // Método de pagamento selecionado

    /**
     * Construtor da classe ComprarIngressoView.
     *
     * @param stage         Janela principal.
     * @param usuario       Usuário logado.
     * @param evento        Evento relacionado.
     * @param controller    Controlador principal.
     * @param armazenamento Objeto de armazenamento de dados.
     */
    public ComprarIngressoView(Stage stage, Usuario usuario, Evento evento, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.evento = evento;
        this.controller = controller;
        this.armazenamento = armazenamento;
        this.ingresso = new Ingresso(evento, evento.getPreco());
        this.selectedPaymentMethod = null;
    }

    /**
     * Exibe a interface de compra de ingressos.
     */
    public void show() {
        // Layout principal
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb); -fx-padding: 20;");

        // Título no topo
        Label titleLabel = new Label(TranslationManager.getInstance().get("buy.ticket"));
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e88e5;");
        StackPane topPane = new StackPane(titleLabel);
        topPane.setStyle("-fx-padding: 10; -fx-background-color: #42a5f5; -fx-border-color: #1e88e5; -fx-border-width: 0 0 3 0;");
        mainLayout.setTop(topPane);

        // Informações do evento
        VBox eventInfoBox = new VBox(10);
        eventInfoBox.setAlignment(Pos.CENTER_LEFT);
        eventInfoBox.setStyle("-fx-padding: 20; -fx-background-color: #ffffff; -fx-border-radius: 10; -fx-border-width: 2; -fx-border-color: #bbdefb;");
        
        Label eventName = new Label(TranslationManager.getInstance().get("event.name") + ": " + evento.getNome());
        Label eventDescription = new Label(TranslationManager.getInstance().get("event.description") + ": " + evento.getDescricao());
        Label eventDate = new Label(TranslationManager.getInstance().get("event.date") + ": " + formatDate(evento.getData()));
        Label ticketPrice = new Label(TranslationManager.getInstance().get("event.tickets.price") + ": R$ " + ingresso.getPreco());

        // Estilo das labels
        eventName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        eventDescription.setStyle("-fx-font-size: 16px;");
        eventDate.setStyle("-fx-font-size: 16px;");
        ticketPrice.setStyle("-fx-font-size: 16px; -fx-text-fill: #1e88e5;");

        eventInfoBox.getChildren().addAll(eventName, eventDescription, eventDate, ticketPrice);

        // Seleção de forma de pagamento
        Label paymentLabel = new Label(TranslationManager.getInstance().get("select.payment"));
        paymentLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        ComboBox<String> paymentOptions = new ComboBox<>();
        paymentOptions.getItems().addAll(
            TranslationManager.getInstance().get("credit.card"),
            TranslationManager.getInstance().get("bank.billet"),
            TranslationManager.getInstance().get("pix")
        );
        paymentOptions.setPromptText(TranslationManager.getInstance().get("select.payment"));
        paymentOptions.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        VBox paymentBox = new VBox(10, paymentLabel, paymentOptions);
        paymentBox.setAlignment(Pos.CENTER_LEFT);

        // Botões de navegação
        Button nextButton = new Button(TranslationManager.getInstance().get("next"));
        nextButton.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-border-radius: 5;");
        nextButton.setOnAction(e -> handleNext(paymentOptions));
        Button cancelButton = new Button(TranslationManager.getInstance().get("cancel"));
        cancelButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-border-radius: 5;");
        cancelButton.setOnAction(e -> new AppScreenView(stage, usuario, controller, armazenamento).show());
        HBox buttonBox = new HBox(20, nextButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

    nextButton.setOnAction(e -> {
        selectedPaymentMethod = paymentOptions.getValue();
        if (selectedPaymentMethod == null || selectedPaymentMethod.isEmpty()) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.select.payment"));
        } else {
            showConfirmationScreen();
        }
    });

    cancelButton.setOnAction(e -> new AppScreenView(stage, usuario, controller, armazenamento).show());

    // Centralize tudo
        VBox centerBox = new VBox(20, eventInfoBox, paymentBox, buttonBox);
        centerBox.setAlignment(Pos.CENTER);
        mainLayout.setCenter(centerBox);

        // Rodapé
        Label footerLabel = new Label("© 2024 - Event Manager");
        footerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #757575;");
        StackPane footerPane = new StackPane(footerLabel);
        footerPane.setStyle("-fx-padding: 10; -fx-background-color: #e3f2fd;");
        mainLayout.setBottom(footerPane);

        // Configura cena
        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setScene(scene);
        stage.setTitle(TranslationManager.getInstance().get("buy.ticket"));
        stage.show();
    }

    /**
     * Manipula a ação do botão "Próximo".
     *
     * @param paymentOptions Combobox com as opções de pagamento.
     */
    private void handleNext(ComboBox<String> paymentOptions) {
        selectedPaymentMethod = paymentOptions.getValue();
        if (selectedPaymentMethod == null || selectedPaymentMethod.isEmpty()) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("error.select.payment"));
        } else {
            showConfirmationScreen();
        }
    }

    /**
     * Exibe a tela de confirmação de compra.
     */
    private void showConfirmationScreen() {
        VBox confirmationBox = new VBox(20);
        confirmationBox.setAlignment(Pos.CENTER);
        confirmationBox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 30; -fx-background-color: #e0f7fa;");

        Label titleLabel = new Label(TranslationManager.getInstance().get("purchase.confirmation"));
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Label eventName = new Label(TranslationManager.getInstance().get("event.name") + ": " + evento.getNome());
        Label eventDate = new Label(TranslationManager.getInstance().get("event.date") + ": " + formatDate(evento.getData()));
        Label ticketPrice = new Label(TranslationManager.getInstance().get("event.tickets.price") + ": R$ " + ingresso.getPreco());
        Label paymentMethod = new Label(TranslationManager.getInstance().get("payment.method") + ": " + selectedPaymentMethod);

        eventName.setStyle("-fx-font-size: 16;");
        eventDate.setStyle("-fx-font-size: 14;");
        ticketPrice.setStyle("-fx-font-size: 14;");
        paymentMethod.setStyle("-fx-font-size: 14;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button confirmButton = new Button(TranslationManager.getInstance().get("confirm.purchase"));
        confirmButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        confirmButton.setOnAction(e -> handlePurchase());

        Button cancelButton = new Button(TranslationManager.getInstance().get("cancel"));
        cancelButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> show());

        buttonBox.getChildren().addAll(confirmButton, cancelButton);

        confirmationBox.getChildren().addAll(titleLabel, eventName, eventDate, ticketPrice, paymentMethod, buttonBox);

        Scene scene = new Scene(confirmationBox, 1000, 600);
        stage.setScene(scene);
    }

    private void handlePurchase() {
        try {
            if (evento.getIngressos() > 0) {
                controller.comprarIngresso(usuario, evento.getID(), armazenamento, selectedPaymentMethod, new Date());

                String purchaseDetails = TranslationManager.getInstance().get("purchase.success") +
                        "\n" + TranslationManager.getInstance().get("event.name") + ": " + evento.getNome() +
                        "\n" + TranslationManager.getInstance().get("payment.method") + ": " + selectedPaymentMethod +
                        "\n" + TranslationManager.getInstance().get("purchase.date") + ": " + formatDate(new Date()) +
                        "\n" + TranslationManager.getInstance().get("receipt.sent") + ": " + usuario.getEmail();

                showAlert(TranslationManager.getInstance().get("success"), purchaseDetails);
                showAlert(TranslationManager.getInstance().get("notification"), TranslationManager.getInstance().get("purchase.success.notification"));

                new AppScreenView(stage, usuario, controller, armazenamento).show();
            } else {
                showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("no.tickets"));
            }
        } catch (Exception e) {
            showAlert(TranslationManager.getInstance().get("error"), TranslationManager.getInstance().get("purchase.failed") + ": " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Formata uma data para o formato "dd/MM/yyyy".
     *
     * @param date Data a ser formatada.
     * @return String representando a data formatada.
     */
    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    private void refreshUI() {
        // Atualiza os textos da interface quando o idioma é alterado
        stage.setTitle(TranslationManager.getInstance().get("buy.ticket"));
        // Atualiza os textos dos componentes da interface
        for (javafx.scene.Node node : stage.getScene().getRoot().lookupAll(".label")) {
            if (node instanceof Label) {
                Label label = (Label) node;
                String key = getKeyFromLabel(label);
                if (key != null) {
                    label.setText(TranslationManager.getInstance().get(key));
                }
            }
        }
        for (javafx.scene.Node node : stage.getScene().getRoot().lookupAll(".button")) {
            if (node instanceof Button) {
                Button button = (Button) node;
                String key = getKeyFromButton(button);
                if (key != null) {
                    button.setText(TranslationManager.getInstance().get(key));
                }
            }
        }
        for (javafx.scene.Node node : stage.getScene().getRoot().lookupAll(".combo-box")) {
            if (node instanceof ComboBox) {
                ComboBox<?> comboBox = (ComboBox<?>) node;
                if (comboBox.getPromptText() != null) {
                    comboBox.setPromptText(TranslationManager.getInstance().get(comboBox.getPromptText()));
                }
            }
        }
    }

    private String getKeyFromLabel(Label label) {
        // Mapear o texto do Label para a chave de tradução
        return null; // Implementar mapeamento específico, se necessário
    }

    private String getKeyFromButton(Button button) {
        // Mapear o texto do Button para a chave de tradução
        return null; // Implementar mapeamento específico, se necessário
    }
}
