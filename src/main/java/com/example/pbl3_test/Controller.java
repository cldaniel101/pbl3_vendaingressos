
package com.example.pbl3_test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Classe responsável pelo controle de operações relacionadas a eventos, 
 * usuários e ingressos.
 */
public class Controller {

/**
 * Cadastra um novo usuário no sistema.
 * 
 * @param username o nome de usuário do novo usuário
 * @param senha a senha do usuário
 * @param nome o nome completo do usuário
 * @param cpf o CPF do usuário
 * @param email o e-mail do usuário
 * @param ativo indica se o usuário está ativo
 * @return o objeto {@code Usuario} criado
 */
    public Usuario cadastrarUsuario(String username, String senha, String nome, String cpf, String email, boolean ativo, Armazenamento dados) {
        // Validação de campos obrigatórios
        if (username == null || username.isEmpty() ||
            senha == null || senha.isEmpty() ||
            nome == null || nome.isEmpty() ||
            cpf == null || cpf.isEmpty() ||
            email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Erro: Todos os campos são obrigatórios.");
        }

        // Validação do CPF
        cpf = cpf.replaceAll("[^a-zA-Z0-9]", ""); // Limpa caracteres inválidos
        if (!cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("Erro: CPF deve conter 11 dígitos numéricos.");
        }

        // Criação do objeto usuário
        Usuario user = new Usuario(username, senha, nome, cpf, email, ativo);

        // Verifica se o CPF já existe
        if (dados.lerUsuario(user.getCpf()) == null) {
            dados.armazenarUsuario(user);
            System.out.println("Usuário armazenado com sucesso!");
            return user;
        } else {
            throw new IllegalArgumentException("Erro: Usuário com CPF " + user.getCpf() + " já existe.");
        }
    }

    /**
     * Cadastra um novo evento no sistema, desde que o usuário seja administrador.
     * 
     * @param admin o usuário que está tentando cadastrar o evento
     * @param nome o nome do evento
     * @param descricao a descrição do evento
     * @param data a data do evento
     * @param ingressos a quantidade de ingressos disponíveis para o evento
     * @param dados a instância de {@code Armazenamento} para salvar o evento
     * @return o objeto {@code Evento} criado
     * @throws SecurityException se o usuário não for administrador
     */
    public Evento cadastrarEvento(Usuario admin, String nome, String descricao, Date data, int ingressos, Armazenamento dados) {
        if (admin.isAdmin()) {
            Evento evento = new Evento(nome, descricao, data, ingressos);
            dados.armazenarEvento(evento);
            return evento;
        } else {
            throw new SecurityException("Somente administradores podem cadastrar eventos.");
        }
    }

    public Evento cadastrarEvento(Usuario admin, String nome, String descricao, Date data, int ingressos, double preco,Armazenamento dados) {
        if (admin.isAdmin()) {
            Evento evento = new Evento(nome, descricao, data, ingressos, preco);
            dados.armazenarEvento(evento);
            return evento;
        } else {
            throw new SecurityException("Somente administradores podem cadastrar eventos.");
        }
    }

    /**
     * Compra um ingresso para um usuário.
     *
     * @param usuario o usuário que está comprando o ingresso
     * @param eventoId o ID do evento para o qual o ingresso está sendo comprado
     * @param dados a instância de {@code Armazenamento} para salvar os dados
     * @param pagamento o método de pagamento usado para a compra
     * @param data a data da compra do ingresso
     * @return o objeto {@code Ingresso} comprado
     */
    public Ingresso comprarIngresso(Usuario usuario, String eventoId, Armazenamento dados, String pagamento, Date data) {
        Evento EventoAtual = dados.lerEvento(eventoId);
        Ingresso ingresso = new Ingresso(EventoAtual, EventoAtual.getPreco());
        Recibo recibo = new Recibo(usuario.getNome(), usuario.getCpf(), usuario.getEmail(), ingresso, pagamento, eventoId, data);

        EventoAtual.setIngressos(EventoAtual.getIngressos() - 1);
        usuario.adicionarIngresso(ingresso);
        usuario.adicionarRecibo(recibo);

        dados.armazenarUsuario(usuario);
        dados.armazenarEvento(EventoAtual);

        System.out.println("Recibo enviado para " + usuario.getEmail());
        return ingresso;
    }

    /**
     * Cancela a compra de um ingresso de um usuário.
     * 
     * @param usuario o usuário que está cancelando a compra
     * @param ingresso o ingresso que está sendo cancelado
     * @param data a data do cancelamento
     * @param dados a instância de {@code Armazenamento} para atualizar os dados
     * @return {@code true} se o cancelamento foi bem-sucedido, {@code false} caso contrário
     */
    public boolean cancelarCompra(Usuario usuario, Ingresso ingresso, Date data, Armazenamento dados) {
        String eventoId = ingresso.getEventoID();
        Evento evento = dados.lerEvento(eventoId);
        if (usuario.removeIngresso(ingresso, evento, data)) {
            evento.setIngressos(evento.getIngressos() + 1);
            dados.armazenarEvento(evento);
            dados.armazenarUsuario(usuario);
        }

        return usuario.removeIngresso(ingresso, evento, data);
    }

    /**
     * Lista os ingressos comprados por um usuário.
     * 
     * @param usuario o usuário cujos ingressos serão listados
     * @return a lista de ingressos comprados pelo usuário
     */
    public List<Ingresso> listarIngressosComprados(Usuario usuario) {
        return usuario.getIngressos();
    }

    /**
     * Lista os recibos de um usuário.
     * 
     * @param usuario o usuário cujos recibos serão listados
     * @return a lista de recibos do usuário
     */
    public List<Recibo> listarRecibos(Usuario usuario) {
        return usuario.getRecibos();
    }

    /**
     * Atualiza os dados de cadastro de um usuário.
     * 
     * @param usuario o usuário a ser atualizado
     * @param username o novo nome de usuário
     * @param senha a nova senha
     * @param nome o novo nome completo
     * @param email o novo e-mail
     */
    public void NovoCadastroUsuario(Usuario usuario, String username, String senha, String nome, String email, Armazenamento dados) {
        usuario.atualizarDados(username, senha, nome, email);
        ArmazenarDadosUsuario(usuario, dados);
    }

    public void ArmazenarDadosUsuario(Usuario usuario, Armazenamento dados) {
       dados.armazenarUsuario(usuario);
    }

    /**
     * Lê os dados de um usuário a partir do CPF.
     * 
     * @param cpf o CPF do usuário
     * @param dados a instância de {@code Armazenamento} para buscar os dados
     * @return o objeto {@code Usuario} correspondente ao CPF fornecido
     */
    public Usuario LerDadosUsuario(String cpf, Armazenamento dados) {
        return dados.lerUsuario(cpf);
    }

    /**
     * Armazena os dados de um evento.
     * 
     * @param evento o evento a ser armazenado
     * @param dados a instância de {@code Armazenamento} para salvar os dados
     */
    public void armazenarEvento(Evento evento, Armazenamento dados) {
        dados.armazenarEvento(evento);
    }

    /**
     * Lê os dados de um evento a partir do ID do evento.
     * 
     * @param eventoId o ID do evento
     * @param dados a instância de {@code Armazenamento} para buscar os dados
     * @return o objeto {@code Evento} correspondente ao ID fornecido
     */
    public Evento LerDadosEvento(String eventoId, Armazenamento dados) {
        return dados.lerEvento(eventoId);
    }

    /**
     * Avalia um evento se o usuário participou do mesmo.
     * 
     * @param evento o evento a ser avaliado
     * @param usuario o usuário que está fazendo a avaliação
     * @param avaliacao o texto da avaliação
     * @throws SecurityException se o usuário não participou do evento
     */
    public void avaliarEvento(Evento evento, Usuario usuario, String avaliacao, Armazenamento dados) {
        // Verifica se o usuário possui um ingresso para o evento
       
        if (usuario.getIngressos().stream().anyMatch(ingresso -> ingresso.getEventoID().equals(evento.getID()))) {
            evento.adicionarAvaliacao(usuario.getLogin(), avaliacao);
            dados.armazenarEvento(evento);
        } else {
            throw new SecurityException("Apenas usuários que participaram do evento podem avaliar.");
        }
    }

    /**
     * Realiza o login do usuário verificando as credenciais fornecidas.
     *
     * @param CPF       O CPF do usuário que deseja realizar o login.
     * @param password  A senha do usuário que deseja realizar o login.
     * @param armazenamento O objeto {@code Armazenamento} utilizado para acessar os dados do usuário.
     * @return {@code true} se as credenciais estiverem corretas, {@code false} caso contrário.
     * @throws IOException Caso ocorra um erro ao acessar os dados do usuário.
     */
    
    public boolean loginUsuario(String CPF, String password, Armazenamento armazenamento) throws IOException {
        try {
            // Obtém os dados do usuário a partir do CPF
            Usuario verificador = armazenamento.lerUsuario(CPF);

            // Verifica se o usuário existe e a senha está correta
            if (verificador != null && verificador.getSenha().equals(password)) {
                return true; // Login bem-sucedido
            } else {
                System.out.println("Credenciais inválidas.");
            }
        } catch (Exception e) {
            // Imprime o erro para depuração
            e.printStackTrace();
        }

        return false; // Retorna falso se algo falhar
    }
    
}