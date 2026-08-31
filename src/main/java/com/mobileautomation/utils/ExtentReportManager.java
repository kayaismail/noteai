package com.mobileautomation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportManager - HTML test raporu üretimini yönetir.
 *
 * ThreadLocal kullanılarak paralel testlerde her thread kendi ExtentTest'ini tutar.
 */
public class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    private static final String REPORT_DIR = ConfigReader.get("report.output.dir", "test-output/reports");
    private static final String REPORT_NAME = "AppiumTestReport_"
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
            + ".html";

    private ExtentReportManager() {
        // Utility class
    }

    // ─── Rapor Başlatma / Kapatma ────────────────────────────────────────────

    /**
     * ExtentReports instance'ını başlatır. BeforeSuite'te çağrılır.
     */
    public static synchronized void initReport() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_DIR + "/" + REPORT_NAME);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Appium Android Test Raporu");
        sparkReporter.config().setReportName("Mobile Automation Test Results");
        sparkReporter.config().setEncoding("UTF-8");

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java", System.getProperty("java.version"));
        extentReports.setSystemInfo("Device", ConfigReader.get("device.name", "N/A"));
        extentReports.setSystemInfo("App Package", ConfigReader.get("app.package", "N/A"));

        log.info("ExtentReport başlatıldı: {}/{}", REPORT_DIR, REPORT_NAME);
    }

    /**
     * Raporu diske yazar. AfterSuite'te çağrılır.
     */
    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            log.info("ExtentReport yazıldı: {}/{}", REPORT_DIR, REPORT_NAME);
        }
    }

    // ─── Test Yönetimi ───────────────────────────────────────────────────────

    /**
     * Yeni bir test node'u oluşturur. BeforeMethod'ta çağrılır.
     */
    public static void createTest(String testName) {
        ExtentTest test = extentReports.createTest(testName);
        extentTestThreadLocal.set(test);
    }

    /**
     * Mevcut thread'e ait ExtentTest'i döner.
     */
    public static ExtentTest getTest() {
        return extentTestThreadLocal.get();
    }

    // ─── Test Sonuç Loglama ──────────────────────────────────────────────────

    public static void passTest(String message) {
        getTest().log(Status.PASS, message);
    }

    public static void failTest(Throwable throwable) {
        getTest().log(Status.FAIL, throwable);
    }

    public static void failTest(String message) {
        getTest().log(Status.FAIL, message);
    }

    public static void skipTest(String message) {
        getTest().log(Status.SKIP, message);
    }

    public static void infoLog(String message) {
        getTest().log(Status.INFO, message);
    }

    /**
     * Rapora screenshot ekler.
     */
    public static void addScreenshot(String screenshotPath) {
        try {
            getTest().addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            log.warn("Screenshot rapora eklenemedi: {}", e.getMessage());
        }
    }
}
