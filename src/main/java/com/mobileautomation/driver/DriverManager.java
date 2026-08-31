package com.mobileautomation.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * DriverManager - AndroidDriver'ın yaşam döngüsünü yönetir.
 * ThreadLocal kullanılarak paralel test desteği sağlanır.
 */
public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<AndroidDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
        // Utility class — instantiation yok
    }

    /**
     * Driver'ı başlatır. Config bilgileri ConfigReader üzerinden gelir.
     *
     * @param appiumServerUrl Appium server URL (ör: http://127.0.0.1:4723)
     * @param options         UiAutomator2Options nesnesi
     */
    public static void initDriver(String appiumServerUrl, UiAutomator2Options options) {
        if (driverThreadLocal.get() != null) {
            log.warn("Driver zaten başlatılmış, mevcut instance kullanılıyor.");
            return;
        }
        try {
            AndroidDriver driver = new AndroidDriver(new URL(appiumServerUrl), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driverThreadLocal.set(driver);
            log.info("AndroidDriver başarıyla başlatıldı. Server: {}", appiumServerUrl);
        } catch (MalformedURLException e) {
            log.error("Geçersiz Appium server URL: {}", appiumServerUrl, e);
            throw new RuntimeException("Appium server URL hatalı: " + appiumServerUrl, e);
        }
    }

    /**
     * Mevcut thread'e ait driver instance'ını döner.
     */
    public static AndroidDriver getDriver() {
        AndroidDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("Driver henüz başlatılmadı. Önce initDriver() çağırın.");
        }
        return driver;
    }

    /**
     * Driver'ı kapatır ve ThreadLocal'dan temizler.
     */
    public static void quitDriver() {
        AndroidDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("AndroidDriver kapatıldı.");
            } catch (Exception e) {
                log.error("Driver kapatılırken hata oluştu.", e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    /**
     * Driver'ın aktif olup olmadığını kontrol eder.
     */
    public static boolean isDriverActive() {
        return driverThreadLocal.get() != null;
    }
}
