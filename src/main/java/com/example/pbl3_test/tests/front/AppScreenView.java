package com.example.pbl3_test.views;

import com.example.pbl3_test.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppScreenView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    private VBox notificationBox;
    private List<Ingresso> lastCheckedIngressos;

    public AppScreenView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
        this.lastCheckedIngressos = new ArrayList<>(usuario.getIngressos());
    }

    public void show() {
        VBox userInfoBox = createUserInfoBox();
        ScrollPane eventListPane = createEventListPane();
        notificationBox = createNotificationBox();

        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-padding: 20; -fx-background-color: #f3f4f6;");
        
        mainLayout.setLeft(userInfoBox);
        mainLayout.setCenter(eventListPane);
        mainLayout.setRight(notificationBox);

        Scene scene = new Scene(mainLayout, 1100, 700);
        stage.setTitle("Dashboard - " + usuario.getNome());
        stage.setScene(scene);
        stage.show();
    }

    private VBox createUserInfoBox() {
        VBox userInfoBox = new VBox(15);
        userInfoBox.setAlignment(Pos.TOP_CENTER);
        userInfoBox.setStyle(
                "-fx-border-color: #4caf50; -fx-border-width: 2; -fx-padding: 20; " +
                "-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-radius: 10;"
        );
        userInfoBox.setPrefWidth(300);

        Label titleLabel = new Label("Informações do Usuário");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #4caf50;");
        
        Label welcomeLabel = new Label("Bem-vindo(a), " + usuario.getNome());
        welcomeLabel.setStyle("-fx-font-size: 14;");

        Button listarIngressosButton = createStyledButton("Ingressos", "#42a5f5");
        listarIngressosButton.setOnAction(event -> new ListarIngressosView(stage, usuario, controller, armazenamento).show());

        Button listarRecibosButton = createStyledButton("Recibos", "#42a5f5");
        listarRecibosButton.setOnAction(event -> new ListarRecibosView(stage, usuario, controller, armazenamento).show());

        Button atualizarCadastroButton = createStyledButton("Atualizar Dados", "#ff9800");
        atualizarCadastroButton.setOnAction(event -> abrirAtualizarDados());

        Button logoutButton = createStyledButton("Sair", "#f44336");
        logoutButton.setOnAction(event -> new LoginRegisterView(stage).show());

        userInfoBox.getChildren().addAll(titleLabel, welcomeLabel, listarIngressosButton, listarRecibosButton, atualizarCadastroButton, logoutButton);
        return userInfoBox;
    }

    private ScrollPane createEventListPane() {
        VBox eventListBox = new VBox(20);
        eventListBox.setAlignment(Pos.TOP_CENTER);
        eventListBox.setStyle("-fx-padding: 20; -fx-background-color: #ffffff; -fx-background-radius: 10;");
        
        Label eventListLabel = new Label("Eventos Disponíveis");
        eventListLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #4caf50;");

        List<String> eventosDisponiveis = armazenamento.listarEventosDisponiveis();

        if (eventosDisponiveis.isEmpty()) {
            Label noEventsLabel = new Label("Nenhum evento disponível.");
            eventListBox.getChildren().add(noEventsLabel);
        } else {
            for (String eventoID : eventosDisponiveis) {
                Evento evento = armazenamento.LerArquivoEvento(eventoID);

                if (evento != null) {
                    VBox eventBox = new VBox(10);
                    eventBox.setStyle("-fx-border-color: #4caf50; -fx-border-width: 1; -fx-padding: 15; -fx-background-color: #e8f5e9; -fx-background-radius: 10;");

                    Label eventName = new Label("Nome: " + evento.getNome());
                    eventName.setStyle("-fx-font-weight: bold;");
                    Label eventDescription = new Label("Descrição: " + evento.getDescricao());
                    Label eventDate = new Label("Data: " + formatDate(evento.getData()));
                    Label ticketsAvailable = new Label("Ingressos: " + evento.getIngressos());

                    Button buyButton = createStyledButton("Comprar", "#4caf50");
                    buyButton.setOnAction(e -> openBuyTicketView(evento));

                    eventBox.getChildren().addAll(eventName, eventDescription, eventDate, ticketsAvailable, buyButton);
                    eventListBox.getChildren().add(eventBox);
                }
            }
        }

        ScrollPane scrollPane = new ScrollPane(eventListBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setPrefWidth(700);

        return scrollPane;
    }

    private VBox createNotificationBox() {
        VBox notificationBox = new VBox(15);
        notificationBox.setAlignment(Pos.TOP_CENTER);
        notificationBox.setStyle(
                "-fx-background-color: #e3f2fd; -fx-border-color: #42a5f5; -fx-border-width: 2; " +
                "-fx-padding: 20; -fx-background-radius: 10; -fx-border-radius: 10;"
        );
        notificationBox.setPrefWidth(300);

        Label notificationTitle = new Label("Notificações");
        notificationTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #42a5f5;");
        notificationBox.getChildren().add(notificationTitle);

        return notificationBox;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-size: 14; -fx-background-radius: 5;"
        );
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #ffffff; -fx-text-fill: " + color + "; -fx-border-color: " + color + "; -fx-border-width: 2;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 5;"));
        return button;
    }

    private void abrirAtualizarDados() {
        new ConfirmarLoginView(stage, usuario, controller, armazenamento).show();
    }

    private void openBuyTicketView(Evento evento) {
        new ComprarIngressoView(stage, usuario, evento, controller, armazenamento).show();
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
}
