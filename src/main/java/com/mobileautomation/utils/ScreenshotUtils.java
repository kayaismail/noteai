package com.mobileautomation.utils;

import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ScreenshotUtils - Test sırasında ekran görüntüsü almayı yönetir.
 */
public class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
        // Utility class
    }

    /**
     * Ekran görüntüsü alır ve belirtilen yola kaydeder.
     *
     * @param driver         AndroidDriver instance'ı
     * @param destinationPath Kaydedilecek tam dosya yolu (ör: test-output/screenshots/login_fail.png)
     * @return Kaydedilen dosyanın path'i, hata olursa null
     */
    public static String captureScreenshot(AndroidDriver driver, String destinationPath) {
        try {
            // Klasör yoksa oluştur
            Path path = Paths.get(destinationPath);
            Files.createDirectories(path.getParent());

            // Screenshot al
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), path);

            log.info("Screenshot kaydedildi: {}", destinationPath);
            return destinationPath;

        } catch (IOException e) {
            log.error("Screenshot kaydedilemedi: {}", destinationPath, e);
            return null;
        }
    }

    /**
     * Test adını kullanarak otomatik isimlendirilmiş screenshot alır.
     *
     * @param driver   AndroidDriver instance'ı
     * @param testName Test metodu adı
     * @return Kaydedilen dosyanın path'i
     */
    public static String captureScreenshot(AndroidDriver driver, String testName, String suffix) {
        String screenshotDir = ConfigReader.get("screenshot.dir", "test-output/screenshots");
        String fileName = testName + "_" + suffix + "_" + System.currentTimeMillis() + ".png";
        String fullPath = screenshotDir + "/" + fileName;
        return captureScreenshot(driver, fullPath);
    }
}
