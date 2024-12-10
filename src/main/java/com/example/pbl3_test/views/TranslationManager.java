package com.example.pbl3_test.views;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.prefs.Preferences;

/**
 * Classe responsável pela gestão de traduções e troca de idiomas em uma aplicação.
 * 
 * <p>
 * Esta classe é implementada como um Singleton para garantir que apenas uma
 * instância gerencie o idioma atual da aplicação e forneça as traduções
 * correspondentes.
 * </p>
 */
public class TranslationManager {
    private static TranslationManager instance; // Instância Singleton da classe
    private Locale currentLocale; // Locale atual da aplicação
    private ResourceBundle bundle; // ResourceBundle com as traduções carregadas
    private List<Runnable> languageChangeListeners = new ArrayList<>(); // Listeners para alterações de idioma
    private static final Preferences preferences = Preferences.userNodeForPackage(TranslationManager.class); // Preferências de idioma salvas

    /**
     * Construtor privado que inicializa o idioma a partir das preferências salvas.
     */
    private TranslationManager() {
        loadLanguagePreference();
    }

    /**
     * Obtém a instância única do TranslationManager.
     *
     * @return Instância do TranslationManager.
     */
    public static TranslationManager getInstance() {
        if (instance == null) {
            instance = new TranslationManager();
        }
        return instance;
    }

    /**
     * Define o idioma atual da aplicação.
     *
     * @param languageCode Código ISO do idioma (ex.: "en" para inglês).
     */
    public void setLanguage(String languageCode) {
        if (currentLocale == null || !currentLocale.getLanguage().equals(languageCode)) {
            this.currentLocale = new Locale(languageCode);
            loadBundle();
            saveLanguagePreference(languageCode, "");
            notifyLanguageChange();
        }
    }

    /**
     * Define o idioma e o país atual da aplicação.
     *
     * @param languageCode Código ISO do idioma (ex.: "en").
     * @param countryCode  Código ISO do país (ex.: "US").
     */
    public void setLanguage(String languageCode, String countryCode) {
        if (currentLocale == null || !currentLocale.getLanguage().equals(languageCode) || !currentLocale.getCountry().equals(countryCode)) {
            this.currentLocale = new Locale(languageCode, countryCode);
            loadBundle();
            saveLanguagePreference(languageCode, countryCode);
            notifyLanguageChange();
        }
    }

    /**
     * Carrega o arquivo de tradução correspondente ao Locale atual.
     */
    private void loadBundle() {
        try {
            // Caminho absoluto no sistema de arquivos para o arquivo de tradução
            String resourcePath = "src/main/java/com/example/pbl3_test/translations/messages_" + currentLocale.getLanguage() + ".properties";
            File file = new File(resourcePath);

            if (!file.exists()) {
                throw new MissingResourceException(
                        "Arquivo de tradução não encontrado",
                        this.getClass().getName(),
                        resourcePath
                );
            }

            try (InputStream inputStream = new FileInputStream(file)) {
                this.bundle = new PropertyResourceBundle(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao carregar o arquivo de tradução: " + currentLocale.getLanguage(), e);
        }
    }

    /**
     * Obtém a tradução correspondente a uma chave.
     *
     * @param key Chave da tradução.
     * @return Texto traduzido ou a própria chave se não encontrada.
     */
    public String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key; // Retorna a chave se a tradução não for encontrada
        }
    }

    /**
     * Obtém a tradução correspondente a uma chave, com suporte a formatação.
     *
     * @param key  Chave da tradução.
     * @param args Argumentos para formatação.
     * @return Texto traduzido formatado ou a própria chave se não encontrada.
     */
    public String get(String key, Object... args) {
        try {
            String template = bundle.getString(key);
            return String.format(template, args);
        } catch (MissingResourceException e) {
            return key; // Retorna a chave se a tradução não for encontrada
        }
    }

    /**
     * Verifica se há tradução disponível para uma determinada chave.
     *
     * @param key Chave da tradução.
     * @return {@code true} se a chave tiver tradução; {@code false} caso contrário.
     */
    public boolean hasTranslation(String key) {
        try {
            bundle.getString(key);
            return true;
        } catch (MissingResourceException e) {
            return false;
        }
    }

    /**
     * Retorna todas as chaves disponíveis no arquivo de tradução atual.
     *
     * @return Conjunto de chaves de tradução.
     */
    public Set<String> getAllKeys() {
        return bundle.keySet();
    }

    /**
     * Retorna o código ISO do idioma atual.
     *
     * @return Código do idioma atual.
     */
    public String getCurrentLanguage() {
        return currentLocale.getLanguage();
    }

    /**
     * Adiciona um listener para notificações de mudanças de idioma.
     *
     * @param listener Função a ser executada quando o idioma for alterado.
     */
    public void addLanguageChangeListener(Runnable listener) {
        languageChangeListeners.add(listener);
    }

    /**
     * Notifica todos os listeners sobre uma mudança de idioma.
     */
    private void notifyLanguageChange() {
        for (Runnable listener : languageChangeListeners) {
            listener.run();
        }
    }

    /**
     * Salva as preferências de idioma no armazenamento persistente.
     *
     * @param languageCode Código ISO do idioma.
     * @param countryCode  Código ISO do país.
     */
    private void saveLanguagePreference(String languageCode, String countryCode) {
        preferences.put("language", languageCode);
        preferences.put("country", countryCode);
    }

    /**
     * Carrega as preferências de idioma do armazenamento persistente.
     */
    public void loadLanguagePreference() {
        String language = preferences.get("language", "pt"); // Valor padrão: português
        String country = preferences.get("country", "");
        this.currentLocale = new Locale(language, country);
        loadBundle();
    }
}
