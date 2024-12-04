package com.example.pbl3_test.views;

import com.example.pbl3_test.Armazenamento;
import com.example.pbl3_test.Controller;
import com.example.pbl3_test.Recibo;
import com.example.pbl3_test.Usuario;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListarRecibosView {

    private final Stage stage;
    private final Usuario usuario;
    private final Controller controller;
    private final Armazenamento armazenamento;

    public ListarRecibosView(Stage stage, Usuario usuario, Controller controller, Armazenamento armazenamento) {
        this.stage = stage;
        this.usuario = usuario;
        this.controller = controller;
        this.armazenamento = armazenamento;
    }

    public void show() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setStyle("-fx-padding: 20;");

        Label titleLabel = new Label("Recibos de Compras");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Adiciona o título ao layout
        layout.getChildren().add(titleLabel);

        // Lista de recibos do usuário
        List<Recibo> recibos = controller.listarRecibos(usuario);

        if (recibos.isEmpty()) {
            Label noReceiptsLabel = new Label("Você não possui nenhum recibo.");
            layout.getChildren().add(noReceiptsLabel);
        } else {
            for (Recibo recibo : recibos) {
                VBox reciboBox = new VBox(5);
                reciboBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

                // Informações do recibo
                Label ingressoInfo = new Label("ID do Ingresso: " + recibo.getIngresso().getId());
                Label nomeUsuario = new Label("Nome do Comprador: " + recibo.getFullName());
                Label cpfUsuario = new Label("CPF do Comprador: " + recibo.getCpf());
                Label emailUsuario = new Label("Email do Comprador: " + recibo.getEmail());
                Label pagamentoInfo = new Label("Método de Pagamento: " + recibo.Pagamento());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Label dataCompraInfo = new Label("Data da Compra: " + dateFormat.format(recibo.getData()));

                // Adiciona os detalhes do recibo ao layout
                reciboBox.getChildren().addAll(ingressoInfo, nomeUsuario, cpfUsuario, emailUsuario, pagamentoInfo, dataCompraInfo);
                layout.getChildren().add(reciboBox);
            }
        }

        // Botão de volta
        Button backButton = new Button("Voltar");
        backButton.setOnAction(event -> {
            new AppScreenView(stage, usuario, controller, armazenamento).show();
        });
        layout.getChildren().add(backButton);

        // Cria um ScrollPane para permitir rolagem
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true); // Ajusta a largura para o tamanho da tela
        scrollPane.setFitToHeight(true); // Ajusta a altura para o tamanho da tela

        // Define o tamanho padrão da cena
        Scene scene = new Scene(scrollPane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Recibos de Compras");
        stage.setResizable(true); // Permite redimensionar a janela
        stage.show();
    }
}
