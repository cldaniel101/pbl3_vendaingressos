package com.example.pbl3_test.views;

import com.example.pbl3_test.Armazenamento;
import com.example.pbl3_test.Controller;
import com.example.pbl3_test.Recibo;
import com.example.pbl3_test.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Classe responsável por exibir a tela de listagem de recibos de compras de um usuário.
 * Apresenta os recibos em formato de cartões contendo as informações relevantes.
 */
public class ListarRecibosView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    /**
     * Construtor da classe ListarRecibosView.
     *
     * @param stage         Janela principal do JavaFX onde a tela será exibida.
     * @param usuario       Usuário atual logado no sistema.
     * @param controller    Controlador responsável pelas operações de negócios.
     * @param armazenamento Instância do sistema de armazenamento.
     */
    public ListarRecibosView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
    }

    /**
     * Método principal para exibir a tela de listagem de recibos.
     * Configura o layout, popula a lista de recibos e exibe a janela.
     */
    public void show() {
        // Layout principal
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f0f0f0;");

        // Barra de título
        Label titleLabel = new Label(TranslationManager.getInstance().get("purchase.receipts"));
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox titleBar = new HBox(titleLabel);
        titleBar.setStyle("-fx-background-color: #4caf50; -fx-padding: 10;");
        titleBar.setAlignment(Pos.CENTER);
        mainLayout.setTop(titleBar);

        // Layout para exibição dos recibos
        VBox recibosLayout = new VBox(15);
        recibosLayout.setPadding(new Insets(20));

        // Obtém a lista de recibos do usuário
        List<Recibo> recibos = controller.listarRecibos(usuario);

        // Verifica se há recibos para exibir
        if (recibos.isEmpty()) {
            Label noReceiptsLabel = new Label(TranslationManager.getInstance().get("no.receipts"));
            noReceiptsLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #555;");
            recibosLayout.getChildren().add(noReceiptsLabel);
        } else {
            // Adiciona cada recibo como um cartão
            for (Recibo recibo : recibos) {
                VBox reciboBox = criarCartaoRecibo(recibo);
                recibosLayout.getChildren().add(reciboBox);
            }
        }

        // Adiciona rolagem à lista de recibos
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(recibosLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");

        mainLayout.setCenter(scrollPane);

        // Botão de voltar para a tela inicial
        Button backButton = new Button(TranslationManager.getInstance().get("back"));
        backButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14;");
        backButton.setOnAction(event -> new AppScreenView(stage, usuario, controller, armazenamento).show());

        HBox footer = new HBox(backButton);
        footer.setAlignment(Pos.CENTER);
        footer.setStyle("-fx-padding: 10;");
        mainLayout.setBottom(footer);

        // Cria e exibe a cena
        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setScene(scene);
        stage.setTitle(TranslationManager.getInstance().get("purchase.receipts"));
        stage.setResizable(true);
        stage.show();
    }

    /**
     * Cria um cartão de exibição para um recibo.
     *
     * @param recibo O recibo a ser exibido.
     * @return Um VBox representando o cartão do recibo.
     */
    private VBox criarCartaoRecibo(Recibo recibo) {
        VBox reciboBox = new VBox(10);
        reciboBox.setPadding(new Insets(15));
        reciboBox.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-padding: 10;");

        // Informações do recibo
        Label ingressoInfo = new Label(TranslationManager.getInstance().get("ticket.id") + ": " + recibo.getIngresso().getId());
        Label nomeUsuario = new Label(TranslationManager.getInstance().get("buyer.name") + ": " + recibo.getFullName());
        Label cpfUsuario = new Label(TranslationManager.getInstance().get("buyer.cpf") + ": " + recibo.getCpf());
        Label emailUsuario = new Label(TranslationManager.getInstance().get("buyer.email") + ": " + recibo.getEmail());
        Label pagamentoInfo = new Label(TranslationManager.getInstance().get("payment.method") + ": " + recibo.Pagamento());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Label dataCompraInfo = new Label(TranslationManager.getInstance().get("purchase.date") + ": " + dateFormat.format(recibo.getData()));

        // Aplica estilo às labels
        for (Label label : List.of(ingressoInfo, nomeUsuario, cpfUsuario, emailUsuario, pagamentoInfo, dataCompraInfo)) {
            label.setStyle("-fx-font-size: 14; -fx-text-fill: #555;");
        }

        reciboBox.getChildren().addAll(ingressoInfo, nomeUsuario, cpfUsuario, emailUsuario, pagamentoInfo, dataCompraInfo);
        return reciboBox;
    }
}
