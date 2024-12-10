package com.example.pbl3_test.views;

import com.example.pbl3_test.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;

/**
 * Classe responsável por exibir a tela de listagem de ingressos de um usuário.
 * Apresenta ingressos válidos e inválidos em colunas separadas, permitindo
 * o cancelamento de ingressos ativos.
 */
public class ListarIngressosView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    /**
     * Construtor da classe ListarIngressosView.
     *
     * @param stage         Janela principal do JavaFX onde a tela será exibida.
     * @param usuario       Usuário atual logado no sistema.
     * @param controller    Controlador responsável pelas operações de negócios.
     * @param armazenamento Instância do sistema de armazenamento.
     */
    public ListarIngressosView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
    }

    /**
     * Método principal para exibir a tela de listagem de ingressos.
     * Configura o layout, popula as colunas de ingressos e exibe a janela.
     */
    public void show() {
        // Layout principal
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f9f9f9;");

        // Barra superior com título
        Label titleLabel = new Label(TranslationManager.getInstance().get("purchased.tickets"));
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #333;");
        HBox header = new HBox(titleLabel);
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #4caf50; -fx-padding: 10;");
        mainLayout.setTop(header);

        // Layout das colunas de ingressos
        HBox contentLayout = new HBox(20);
        contentLayout.setPadding(new Insets(20));

        // Coluna de ingressos válidos
        VBox validTicketsLayout = criarColunaIngressos("valid.tickets", "#d9f7be");
        // Coluna de ingressos inválidos
        VBox invalidTicketsLayout = criarColunaIngressos("invalid.tickets", "#ffd6e7");

        // Preenchimento das colunas
        List<Ingresso> ingressos = usuario.getIngressos();
        if (ingressos.isEmpty()) {
            validTicketsLayout.getChildren().add(new Label(TranslationManager.getInstance().get("no.tickets")));
        } else {
            for (Ingresso ingresso : ingressos) {
                VBox ingressoBox = criarCartaoIngresso(ingresso);
                if (ingresso.isAtivo()) {
                    validTicketsLayout.getChildren().add(ingressoBox);
                } else {
                    invalidTicketsLayout.getChildren().add(ingressoBox);
                }
            }
        }

        // Scroll para as colunas
        ScrollPane validTicketsScrollPane = new ScrollPane(validTicketsLayout);
        validTicketsScrollPane.setFitToWidth(true);

        ScrollPane invalidTicketsScrollPane = new ScrollPane(invalidTicketsLayout);
        invalidTicketsScrollPane.setFitToWidth(true);

        contentLayout.getChildren().addAll(validTicketsScrollPane, invalidTicketsScrollPane);
        mainLayout.setCenter(contentLayout);

        // Botão de voltar
        Button backButton = new Button(TranslationManager.getInstance().get("back.to.home"));
        backButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14;");
        backButton.setOnAction(event -> new AppScreenView(stage, usuario, controller, armazenamento).show());

        HBox footer = new HBox(backButton);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(15));
        mainLayout.setBottom(footer);

        // Cena
        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setScene(scene);
        stage.setTitle(TranslationManager.getInstance().get("purchased.tickets"));
        stage.show();
    }

    /**
     * Cria uma coluna para exibição de ingressos.
     *
     * @param tituloKey Chave de tradução do título da coluna.
     * @param bgColor   Cor de fundo da coluna.
     * @return Um VBox representando a coluna de ingressos.
     */
    private VBox criarColunaIngressos(String tituloKey, String bgColor) {
        Label title = new Label(TranslationManager.getInstance().get(tituloKey));
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        VBox column = new VBox(10, title);
        column.setPadding(new Insets(15));
        column.setStyle("-fx-background-color: " + bgColor + "; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 10;");
        column.setPrefWidth(400);

        return column;
    }

    /**
     * Cria um cartão de exibição para um ingresso.
     *
     * @param ingresso O ingresso a ser exibido.
     * @return Um VBox representando o cartão do ingresso.
     */
    private VBox criarCartaoIngresso(Ingresso ingresso) {
        VBox ingressoBox = new VBox(10);
        ingressoBox.setPadding(new Insets(10));
        ingressoBox.setStyle("-fx-background-color: #fff; -fx-border-color: #ddd; -fx-border-radius: 10;");

        Label ingressoInfo = new Label(TranslationManager.getInstance().get("ticket.id") + ": " + ingresso.getId());
        Label eventoInfo = new Label(TranslationManager.getInstance().get("event.id") + ": " + ingresso.getEventoID());
        Label precoInfo = new Label(TranslationManager.getInstance().get("price") + ": R$" + ingresso.getPreco());

        if (ingresso.isAtivo()) {
            Button cancelButton = new Button(TranslationManager.getInstance().get("cancel.ticket"));
            cancelButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            cancelButton.setOnAction(event -> {
                boolean sucesso = controller.cancelarCompra(usuario, ingresso, new Date(), armazenamento);
                if (sucesso) {
                    show(); // Atualiza a tela após o cancelamento
                } else {
                    mostrarErro(TranslationManager.getInstance().get("error.cancel.ticket"));
                }
            });
            ingressoBox.getChildren().addAll(ingressoInfo, eventoInfo, precoInfo, cancelButton);
        } else {
            Label statusInfo = new Label(TranslationManager.getInstance().get("status.cancelled"));
            ingressoBox.getChildren().addAll(ingressoInfo, eventoInfo, precoInfo, statusInfo);
        }

        return ingressoBox;
    }

    /**
     * Exibe uma mensagem de erro em um alerta.
     *
     * @param mensagem A mensagem de erro a ser exibida.
     */
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
