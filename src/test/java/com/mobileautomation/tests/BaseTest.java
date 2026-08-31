package com.mobileautomation.tests;

import com.mobileautomation.driver.DriverManager;
import com.mobileautomation.utils.ConfigReader;
import com.mobileautomation.utils.ExtentReportManager;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;

/**
 * BaseTest - Tüm test sınıflarının kalıtım aldığı temel sınıf.
 * Driver kurulumu, teardown ve raporlama hookları burada yönetilir.
 */
public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    // ─── Suite Seviyesi ──────────────────────────────────────────────────────

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        log.info("========== TEST SUITE BAŞLIYOR ==========");
        ExtentReportManager.initReport();
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        ExtentReportManager.flushReport();
        log.info("========== TEST SUITE TAMAMLANDI ==========");
    }

    // ─── Test Metodu Seviyesi ────────────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        log.info("--- Test başlıyor: {} ---", method.getName());
        ExtentReportManager.createTest(method.getName());

        UiAutomator2Options options = buildCapabilities();
        DriverManager.initDriver(ConfigReader.get("appium.server.url"), options);

        log.info("Driver başlatıldı. Device: {}", ConfigReader.get("device.name"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test BAŞARISIZ: {}", testName);
            captureScreenshotOnFailure(testName);
            ExtentReportManager.failTest(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("Test BAŞARILI: {}", testName);
            ExtentReportManager.passTest("Test başarıyla tamamlandı.");
        } else {
            log.warn("Test ATLANDI: {}", testName);
            ExtentReportManager.skipTest("Test atlandı.");
        }

        DriverManager.quitDriver();
        log.info("--- Test bitti: {} ---", testName);
    }

    // ─── Capability Builder ──────────────────────────────────────────────────

    private UiAutomator2Options buildCapabilities() {
        UiAutomator2Options options = new UiAutomator2Options();

        options.setDeviceName(ConfigReader.get("device.name"));
        options.setPlatformVersion(ConfigReader.get("platform.version"));
        options.setAppPackage(ConfigReader.get("app.package"));
        options.setAppActivity(ConfigReader.get("app.activity"));

        // Uygulamayı sıfırdan başlatmak yerine mevcut session'ı kullan
        options.setNoReset(Boolean.parseBoolean(ConfigReader.get("no.reset")));

        // ADB'nin portu otomatik açmasına izin ver
        options.setAutoGrantPermissions(true);

        // Implicit wait
        options.setImplicitWaitTimeout(Duration.ofSeconds(10));

        log.debug("Capabilities hazırlandı: package={}, activity={}",
                ConfigReader.get("app.package"),
                ConfigReader.get("app.activity"));

        return options;
    }

    // ─── Screenshot Yardımcısı ───────────────────────────────────────────────

    private void captureScreenshotOnFailure(String testName) {
        try {
            // Screenshots klasörü yoksa oluştur
            File screenshotDir = new File("test-output/screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            String screenshotPath = "test-output/screenshots/" + testName + "_" +
                    System.currentTimeMillis() + ".png";

            com.mobileautomation.utils.ScreenshotUtils.captureScreenshot(
                    DriverManager.getDriver(), screenshotPath);

            ExtentReportManager.addScreenshot(screenshotPath);
            log.info("Screenshot alındı: {}", screenshotPath);
        } catch (Exception e) {
            log.warn("Screenshot alınamadı: {}", e.getMessage());
        }
    }
}
