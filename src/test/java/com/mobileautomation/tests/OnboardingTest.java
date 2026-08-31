package com.mobileautomation.tests;

import com.mobileautomation.driver.DriverManager;
import com.mobileautomation.pages.GetStartedPage;
import com.mobileautomation.pages.OnboardingPage;
import com.mobileautomation.pages.PrivacyPolicyPage;
import com.mobileautomation.pages.TermsOfUsePage;
import com.mobileautomation.utils.ConfigReader;
import com.mobileautomation.utils.ExtentReportManager;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * OnboardingTest - Uygulama açılışından onboarding tamamlanmasına kadar
 * olan tam akışı test eder.
 *
 * ──────────────────────────────────────────────────────────────────
 * SENARYO:
 *   1. Uygulamayı başlat → Get Started ekranı yüklenmeli
 *   2. Terms of Use linkine tıkla → Terms ekranı açılmalı
 *   3. Get Started'a geri dön → Get Started ekranı tekrar görünmeli
 *   4. Privacy Policy linkine tıkla → Privacy ekranı açılmalı
 *   5. Get Started'a geri dön → Get Started ekranı tekrar görünmeli
 *   6. Get Started butonuna tıkla → Onboarding adımları başlamalı
 *   7. Tüm onboarding adımlarını tamamla → Onboarding bitmeli
 * ──────────────────────────────────────────────────────────────────
 *
 * NOT: Bu sınıf BaseTest'ten EXTEND ETMEZ.
 * Nedenı: BaseTest her @Test metodunda driver'ı yeniden başlatır.
 * Bu senaryoda ise tüm adımlar tek bir uygulama session'ı üzerinde
 * sırayla yürütülmesi gerekir. Bu nedenle driver yönetimi
 * @BeforeClass / @AfterClass seviyesinde bu sınıfta yapılır.
 *
 * Raporlama (ExtentReports) yine de kullanılır — BeforeSuite/AfterSuite
 * için testng.xml'de listener tanımlanmıştır.
 */
public class OnboardingTest {

    private static final Logger log = LogManager.getLogger(OnboardingTest.class);

    // Sayfa nesneleri — sınıf genelinde paylaşılır
    private GetStartedPage getStartedPage;

    // ─── Suite Seviyesi: Rapor ────────────────────────────────────────────────

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        log.info("========== ONBOARDING TEST SUITE BAŞLIYOR ==========");
        ExtentReportManager.initReport();
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        ExtentReportManager.flushReport();
        log.info("========== ONBOARDING TEST SUITE TAMAMLANDI ==========");
    }

    // ─── Class Seviyesi: Driver (tek session tüm testler için) ───────────────

    @BeforeClass(alwaysRun = true)
    public void classSetup() {
        log.info(">>> Onboarding session başlatılıyor...");

        UiAutomator2Options options = buildCapabilities();
        DriverManager.initDriver(ConfigReader.get("appium.server.url"), options);

        log.info("Driver başlatıldı. Package: {}", ConfigReader.get("app.package"));
    }

    @AfterClass(alwaysRun = true)
    public void classTeardown() {
        log.info(">>> Onboarding session kapatılıyor...");
        DriverManager.quitDriver();
    }

    // ─── Method Seviyesi: Raporlama ──────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        log.info("--- Test başlıyor: {} ---", method.getName());
        ExtentReportManager.createTest(method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                ExtentReportManager.passTest("Test başarıyla tamamlandı.");
                log.info("✓ BAŞARILI: {}", testName);
                break;
            case ITestResult.FAILURE:
                ExtentReportManager.failTest(result.getThrowable());
                captureScreenshot(testName);
                log.error("✗ BAŞARISIZ: {}", testName);
                break;
            case ITestResult.SKIP:
                ExtentReportManager.skipTest("Test atlandı.");
                log.warn("- ATLANDI: {}", testName);
                break;
        }
    }

    // ─── TEST 1: Uygulamayı başlat, Get Started ekranını doğrula ────────────

    @Test(
        priority = 1,
        description = "Uygulama başlatıldığında Get Started ekranı yüklenmeli"
    )
    public void test01_appLaunchesOnGetStartedScreen() {
        ExtentReportManager.infoLog("Uygulama başlatıldı, Get Started ekranı bekleniyor.");

        getStartedPage = new GetStartedPage();

        Assert.assertTrue(
            getStartedPage.isPageLoaded(),
            "Get Started ekranı yüklenmedi! Locator'ları veya app.activity değerini kontrol et."
        );

        Assert.assertTrue(
            getStartedPage.isTermsOfUseLinkVisible(),
            "Terms of Use linki Get Started ekranında görünmüyor!"
        );

        Assert.assertTrue(
            getStartedPage.isPrivacyPolicyLinkVisible(),
            "Privacy Policy linki Get Started ekranında görünmüyor!"
        );

        ExtentReportManager.infoLog("Get Started ekranı başarıyla doğrulandı.");
        log.info("TEST 1 TAMAM: Get Started ekranı yüklendi.");
    }

    // ─── TEST 2: Terms of Use ekranını aç ve doğrula ─────────────────────────

    @Test(
        priority = 2,
        description = "Terms of Use linkine tıklandığında Terms ekranı açılmalı",
        dependsOnMethods = "test01_appLaunchesOnGetStartedScreen"
    )
    public void test02_openTermsOfUseScreen() {
        ExtentReportManager.infoLog("Get Started → Terms of Use linkine tıklanıyor.");

        TermsOfUsePage termsPage = getStartedPage.openTermsOfUse();

        Assert.assertTrue(
            termsPage.isPageLoaded(),
            "Terms of Use ekranı açılmadı!"
        );

        Assert.assertTrue(
            termsPage.isContentVisible(),
            "Terms of Use içeriği ekranda görünmüyor!"
        );

        ExtentReportManager.infoLog("Terms of Use ekranı başarıyla doğrulandı. Sayfa başlığı: "
            + termsPage.getTitle());

        log.info("TEST 2 TAMAM: Terms of Use ekranı açıldı ve doğrulandı.");
    }

    // ─── TEST 3: Terms of Use'dan Get Started'a geri dön ────────────────────

    @Test(
        priority = 3,
        description = "Terms of Use ekranından geri dönüldüğünde Get Started ekranı görünmeli",
        dependsOnMethods = "test02_openTermsOfUseScreen"
    )
    public void test03_returnToGetStartedFromTerms() {
        ExtentReportManager.infoLog("Terms of Use ekranından Get Started'a geri dönülüyor.");

        // Önce Terms sayfasına git (test02 zaten açık bırakmış olabilir)
        TermsOfUsePage termsPage = new TermsOfUsePage();

        // Toolbar back butonu varsa goBack(), yoksa native back
        getStartedPage = termsPage.goBackWithNativeBack();

        Assert.assertTrue(
            getStartedPage.isPageLoaded(),
            "Get Started ekranına geri dönülemedi (Terms → Back)!"
        );

        ExtentReportManager.infoLog("Get Started ekranına başarıyla geri dönüldü.");
        log.info("TEST 3 TAMAM: Terms → Get Started geri navigasyonu başarılı.");
    }

    // ─── TEST 4: Privacy Policy ekranını aç ve doğrula ───────────────────────

    @Test(
        priority = 4,
        description = "Privacy Policy linkine tıklandığında Privacy Policy ekranı açılmalı",
        dependsOnMethods = "test03_returnToGetStartedFromTerms"
    )
    public void test04_openPrivacyPolicyScreen() {
        ExtentReportManager.infoLog("Get Started → Privacy Policy linkine tıklanıyor.");

        PrivacyPolicyPage privacyPage = getStartedPage.openPrivacyPolicy();

        Assert.assertTrue(
            privacyPage.isPageLoaded(),
            "Privacy Policy ekranı açılmadı!"
        );

        Assert.assertTrue(
            privacyPage.isContentVisible(),
            "Privacy Policy içeriği ekranda görünmüyor!"
        );

        ExtentReportManager.infoLog("Privacy Policy ekranı başarıyla doğrulandı. Sayfa başlığı: "
            + privacyPage.getTitle());

        log.info("TEST 4 TAMAM: Privacy Policy ekranı açıldı ve doğrulandı.");
    }

    // ─── TEST 5: Privacy Policy'den Get Started'a geri dön ──────────────────

    @Test(
        priority = 5,
        description = "Privacy Policy ekranından geri dönüldüğünde Get Started ekranı görünmeli",
        dependsOnMethods = "test04_openPrivacyPolicyScreen"
    )
    public void test05_returnToGetStartedFromPrivacyPolicy() {
        ExtentReportManager.infoLog("Privacy Policy ekranından Get Started'a geri dönülüyor.");

        PrivacyPolicyPage privacyPage = new PrivacyPolicyPage();
        getStartedPage = privacyPage.goBackWithNativeBack();

        Assert.assertTrue(
            getStartedPage.isPageLoaded(),
            "Get Started ekranına geri dönülemedi (Privacy Policy → Back)!"
        );

        ExtentReportManager.infoLog("Get Started ekranına başarıyla geri dönüldü.");
        log.info("TEST 5 TAMAM: Privacy Policy → Get Started geri navigasyonu başarılı.");
    }

    // ─── TEST 6: Get Started butonuna tıkla, Onboarding başlamalı ────────────

    @Test(
        priority = 6,
        description = "Get Started butonuna tıklandığında Onboarding ekranı açılmalı",
        dependsOnMethods = "test05_returnToGetStartedFromPrivacyPolicy",
        enabled = false  // TODO: OnboardingPage locator'ları doldurulunca true yap
    )
    public void test06_getStartedButtonNavigatesToOnboarding() {
        ExtentReportManager.infoLog("Get Started butonuna tıklanıyor.");

        OnboardingPage onboardingPage = getStartedPage.clickGetStarted();

        Assert.assertTrue(
            onboardingPage.isPageLoaded(),
            "Get Started butonuna tıklandıktan sonra Onboarding ekranı açılmadı!"
        );

        int totalSteps = onboardingPage.getTotalStepCount();
        if (totalSteps > 0) {
            ExtentReportManager.infoLog("Onboarding adım sayısı: " + totalSteps);
            log.info("Onboarding toplam adım sayısı: {}", totalSteps);
        }

        ExtentReportManager.infoLog("Onboarding ekranı başarıyla açıldı.");
        log.info("TEST 6 TAMAM: Get Started → Onboarding navigasyonu başarılı.");
    }

    // ─── TEST 7: Tüm onboarding adımlarını tamamla ───────────────────────────

    @Test(
        priority = 7,
        description = "Tüm onboarding adımları Next/Finish butonlarıyla tamamlanabilmeli",
        dependsOnMethods = "test06_getStartedButtonNavigatesToOnboarding",
        enabled = false  // TODO: OnboardingPage locator'ları doldurulunca true yap
    )
    public void test07_completeAllOnboardingSteps() {
        ExtentReportManager.infoLog("Onboarding adımları tamamlanıyor...");

        OnboardingPage onboardingPage = new OnboardingPage();

        Assert.assertTrue(
            onboardingPage.isPageLoaded(),
            "Onboarding ekranı bu adımda yüklü değil!"
        );

        onboardingPage.completeAllSteps();

        Assert.assertFalse(
            onboardingPage.isPageLoaded(),
            "Onboarding tamamlandı ama hâlâ Onboarding ekranındayız!"
        );

        ExtentReportManager.infoLog("Onboarding başarıyla tamamlandı.");
        log.info("TEST 7 TAMAM: Tüm onboarding adımları tamamlandı.");
    }

    // ─── Yardımcı Metodlar ───────────────────────────────────────────────────

    private UiAutomator2Options buildCapabilities() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName(ConfigReader.get("device.name"));
        options.setPlatformVersion(ConfigReader.get("platform.version"));
        options.setAppPackage(ConfigReader.get("app.package"));
        options.setAppActivity(ConfigReader.get("app.activity"));
        // Her test çalışmasında uygulamayı sıfırdan başlat (onboarding tekrar görünsün)
        options.setNoReset(false);
        options.setAutoGrantPermissions(true);
        options.setImplicitWaitTimeout(Duration.ofSeconds(10));
        // ANDROID_HOME env variable tanımlı değilse SDK yolunu capability ile ver
        options.setCapability("appium:androidSdkPath",
            ConfigReader.get("android.sdk.path", "C:\\Users\\Şeyma Kaya\\AppData\\Local\\Android\\Sdk"));
        return options;
    }

    private void captureScreenshot(String testName) {
        try {
            String path = "test-output/screenshots/" + testName + "_"
                + System.currentTimeMillis() + ".png";
            com.mobileautomation.utils.ScreenshotUtils.captureScreenshot(
                DriverManager.getDriver(), path);
            ExtentReportManager.addScreenshot(path);
        } catch (Exception e) {
            log.warn("Screenshot alınamadı: {}", e.getMessage());
        }
    }
}
