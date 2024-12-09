package com.example.pbl3_test;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe responsável por gerenciar a persistência de dados de usuários e eventos,
 * utilizando arquivos JSON como meio de armazenamento.
 */
public class Armazenamento {

    private final String baseDir;

    /**
     * Construtor da classe, que inicializa o caminho base para os arquivos de dados.
     */
    public Armazenamento() {
        String projectDir = System.getProperty("user.dir");
        this.baseDir = projectDir + File.separator + "src" + File.separator + "main" + File.separator +
                       "java" + File.separator + "com" + File.separator + "example" + File.separator +
                       "pbl3_test" + File.separator + "Data";
        System.out.println("Caminho gerado: " + this.baseDir);
    }

    /**
     * Verifica e cria os diretórios necessários para armazenamento de dados.
     *
     * @return {@code true} se os diretórios foram criados ou já existiam; {@code false} em caso de erro.
     */
    public boolean verificarExistencia() {
        File baseDirFile = new File(this.baseDir);
        File usuariosDir = new File(this.baseDir + File.separator + "Usuarios");
        File eventosDir = new File(this.baseDir + File.separator + "Eventos");

        boolean baseCriado = baseDirFile.exists() || baseDirFile.mkdirs();
        boolean usuariosCriado = usuariosDir.exists() || usuariosDir.mkdirs();
        boolean eventosCriado = eventosDir.exists() || eventosDir.mkdirs();

        if (baseCriado && usuariosCriado && eventosCriado) {
            System.out.println("Todos os diretórios estão prontos: " + this.baseDir);
            return true;
        } else {
            System.err.println("Falha ao criar os diretórios necessários!");
            return false;
        }
    }

    /**
     * Armazena os dados de um usuário em um arquivo JSON.
     *
     * @param usuario o objeto {@code Usuario} a ser armazenado.
     */
    public void armazenarUsuario(Usuario usuario) {
        Gson gson = new Gson();
        String json = gson.toJson(usuario);
        String userCpf = usuario.getCpf().replaceAll("[^a-zA-Z0-9]", "");
        String caminhoDiretorio = baseDir + File.separator + "Usuarios";
        criarDiretorioSeNecessario(caminhoDiretorio);

        String caminhoArquivo = caminhoDiretorio + File.separator + userCpf + ".json";

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(caminhoArquivo), "UTF-8")) {
            writer.write(json);
            System.out.println("Dados do usuário armazenados com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao armazenar os dados do usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lê os dados de um usuário a partir de um arquivo JSON.
     *
     * @param cpf o CPF do usuário para buscar os dados.
     * @return o objeto {@code Usuario} lido do arquivo ou {@code null} em caso de erro.
     */
    public Usuario lerUsuario(String cpf) {
        Gson gson = new Gson();
        String caminhoArquivo = baseDir + File.separator + "Usuarios" + File.separator + cpf + ".json";
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            System.out.println("Arquivo não encontrado: " + caminhoArquivo);
            return null;
        }

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(caminhoArquivo), "UTF-8")) {
            Usuario usuario = gson.fromJson(reader, Usuario.class);
            System.out.println("Dados do usuário lidos com sucesso!");
            return usuario;
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Erro ao ler os dados do usuário: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Armazena os dados de um evento em um arquivo JSON.
     *
     * @param evento o objeto {@code Evento} a ser armazenado.
     */
    public void armazenarEvento(Evento evento) {
        if (!verificarExistencia()) {
            System.err.println("Erro: Não foi possível preparar os diretórios para salvar o evento.");
            return;
        }

        Gson gson = new Gson();
        String json = gson.toJson(evento);
        String eventoId = evento.getID();
        String caminhoArquivo = baseDir + File.separator + "Eventos" + File.separator + eventoId + ".json";

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(caminhoArquivo), "UTF-8")) {
            writer.write(json);
            System.out.println("Dados do evento armazenados com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao armazenar os dados do evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lê os dados de um evento a partir de um arquivo JSON.
     *
     * @param eventoId o ID do evento para buscar os dados.
     * @return o objeto {@code Evento} lido do arquivo ou {@code null} em caso de erro.
     */
    public Evento lerEvento(String eventoId) {
        Gson gson = new Gson();
        String caminhoArquivo = baseDir + File.separator + "Eventos" + File.separator + eventoId + ".json";

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(caminhoArquivo), "UTF-8")) {
            Evento evento = gson.fromJson(reader, Evento.class);
            System.out.println("Dados do evento lidos com sucesso!");
            return evento;
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Erro ao ler os dados do evento: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista todos os eventos futuros, baseando-se nos arquivos JSON na pasta de eventos.
     *
     * @return uma lista com os nomes dos eventos futuros.
     */
    public List<String> listarEventosDisponiveis() {
        List<String> eventosFuturos = new ArrayList<>();
        File diretorio = new File(baseDir + File.separator + "Eventos");
        Date dataAtual = new Date();

        if (diretorio.exists() && diretorio.isDirectory()) {
            File[] arquivos = diretorio.listFiles((dir, name) -> name.endsWith(".json"));

            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    String nomeArquivo = arquivo.getName().replace(".json", "");

                    try {
                        Date dataArquivo = new SimpleDateFormat("yyMMdd").parse(nomeArquivo.substring(0, 6));
                        if (dataArquivo.after(dataAtual)) {
                            eventosFuturos.add(nomeArquivo);
                        }
                    } catch (ParseException e) {
                        System.err.println("Erro ao processar o arquivo " + nomeArquivo + ": " + e.getMessage());
                    }
                }
            }
        }

        return eventosFuturos;
    }

    /**
     * Cria o diretório especificado, caso ele não exista.
     *
     * @param caminho o caminho do diretório a ser criado.
     */
    private void criarDiretorioSeNecessario(String caminho) {
        File dir = new File(caminho);
        if (!dir.exists() && dir.mkdirs()) {
            System.out.println("Diretório criado: " + caminho);
        }
    }
}
