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

public class TranslationManager {
    private static TranslationManager instance;
    private Locale currentLocale;
    private ResourceBundle bundle;
    private List<Runnable> languageChangeListeners = new ArrayList<>();
    private static final Preferences preferences = Preferences.userNodeForPackage(TranslationManager.class);

    private TranslationManager() {
        // Carrega o idioma salvo nas preferências
        loadLanguagePreference();
    }

    public static TranslationManager getInstance() {
        if (instance == null) {
            instance = new TranslationManager();
        }
        return instance;
    }

    public void setLanguage(String languageCode) {
        if (currentLocale == null || !currentLocale.getLanguage().equals(languageCode)) {
            this.currentLocale = new Locale(languageCode);
            loadBundle();
            saveLanguagePreference(languageCode, "");
            notifyLanguageChange();
        }
    }

    public void setLanguage(String languageCode, String countryCode) {
        if (currentLocale == null || !currentLocale.getLanguage().equals(languageCode) || !currentLocale.getCountry().equals(countryCode)) {
            this.currentLocale = new Locale(languageCode, countryCode);
            loadBundle();
            saveLanguagePreference(languageCode, countryCode);
            notifyLanguageChange();
        }
    }

    private void loadBundle() {
        try {
            // Caminho absoluto no sistema de arquivos
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

    public String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key; // Retorna a chave se a tradução não for encontrada
        }
    }

    public String get(String key, Object... args) {
        try {
            String template = bundle.getString(key);
            return String.format(template, args);
        } catch (MissingResourceException e) {
            return key; // Retorna a chave se a tradução não for encontrada
        }
    }

    public boolean hasTranslation(String key) {
        try {
            bundle.getString(key);
            return true;
        } catch (MissingResourceException e) {
            return false;
        }
    }

    public Set<String> getAllKeys() {
        return bundle.keySet();
    }

    public String getCurrentLanguage() {
        return currentLocale.getLanguage();
    }

    public void addLanguageChangeListener(Runnable listener) {
        languageChangeListeners.add(listener);
    }

    private void notifyLanguageChange() {
        for (Runnable listener : languageChangeListeners) {
            listener.run();
        }
    }

    private void saveLanguagePreference(String languageCode, String countryCode) {
        preferences.put("language", languageCode);
        preferences.put("country", countryCode);
    }

    public void loadLanguagePreference() {
        String language = preferences.get("language", "pt"); // Valor padrão: português
        String country = preferences.get("country", "");
        this.currentLocale = new Locale(language, country);
        loadBundle();
    }
}
