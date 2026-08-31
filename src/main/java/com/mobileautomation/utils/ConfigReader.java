package com.mobileautomation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader - config.properties dosyasını okur.
 *
 * Singleton pattern ile properties yalnızca bir kez yüklenir.
 * Sistem ortam değişkenleri (environment variables) config dosyasını override eder —
 * bu sayede CI/CD pipeline'larında dışarıdan değer geçilebilir.
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final String CONFIG_FILE = "config.properties";

    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private ConfigReader() {
        // Utility class — instantiation yok
    }

    // ─── Properties Yükleme ──────────────────────────────────────────────────

    private static void loadProperties() {
        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new RuntimeException(
                        CONFIG_FILE + " bulunamadı. src/test/resources altında olduğundan emin olun.");
            }

            properties.load(input);
            log.info("config.properties başarıyla yüklendi.");

        } catch (IOException e) {
            log.error("config.properties yüklenirken hata oluştu.", e);
            throw new RuntimeException("Config dosyası yüklenemedi.", e);
        }
    }

    // ─── Değer Okuma ─────────────────────────────────────────────────────────

    /**
     * Verilen key'in değerini döner.
     * Önce sistem ortam değişkenlerine, sonra config dosyasına bakar.
     *
     * @param key properties anahtarı
     * @return string değer
     * @throws RuntimeException key bulunamazsa
     */
    public static String get(String key) {
        // CI/CD'den override gelebilir — önce env var'a bak
        String envValue = System.getenv(key.replace(".", "_").toUpperCase());
        if (envValue != null && !envValue.isBlank()) {
            log.debug("Config '{}' değeri env var'dan alındı.", key);
            return envValue;
        }

        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new RuntimeException(
                    "Config key bulunamadı veya boş: '" + key + "'. config.properties dosyasını kontrol edin.");
        }

        return value.trim();
    }

    /**
     * Değeri döner; bulunamazsa defaultValue döner (exception fırlatmaz).
     */
    public static String get(String key, String defaultValue) {
        try {
            return get(key);
        } catch (RuntimeException e) {
            log.debug("Config key '{}' bulunamadı, default değer kullanılıyor: '{}'", key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Integer değer okur.
     */
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /**
     * Boolean değer okur.
     */
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
