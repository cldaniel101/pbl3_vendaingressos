package com.example.pbl3_test.views;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe principal responsável por iniciar a aplicação JavaFX.
 * 
 * <p>
 * Esta classe serve como ponto de entrada para a aplicação. Ela configura o
 * estágio inicial e exibe a tela de login/registro ao iniciar.
 * </p>
 */
public class ViewController extends Application {

    /**
     * Método principal da aplicação. Inicia o ciclo de vida do JavaFX.
     * 
     * @param args Argumentos da linha de comando (se aplicável).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Método chamado ao iniciar a aplicação JavaFX.
     *
     * <p>
     * Configura a janela principal (Stage) da aplicação e exibe a tela inicial.
     * Neste caso, a tela inicial é definida pela classe {@link LoginRegisterView}.
     * </p>
     *
     * @param primaryStage Janela principal da aplicação.
     */
    @Override
    public void start(Stage primaryStage) {
        // Instancia a tela de login/registro e a associa ao Stage principal
        LoginRegisterView loginRegisterView = new LoginRegisterView(primaryStage);

        // Exibe a tela de login/registro
        loginRegisterView.show();
    }
}
