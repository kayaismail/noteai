package com.mobileautomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * OnboardingPage - "Get Started" butonuna basıldıktan sonra
 * görünen adım adım onboarding / tanıtım ekranları.
 *
 * Tipik akış:
 *   Step 1 (ör: hoş geldin)  →  Next
 *   Step 2 (ör: özellik tanıtımı)  →  Next
 *   Step N  →  "Finish" / "Done" / "Let's Go"  →  Ana ekran
 *
 * LOCATOR GÜNCELLEME:
 *   Appium Inspector'dan aldığın değerleri TODO alanlarına yaz.
 *   Her slide için ayrı başlık/açıklama locator'ı varsa
 *   getStepTitle() / getStepDescription() metotlarını kullan.
 */
public class OnboardingPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────────────────
    // TODO: Appium Inspector'dan alınan değerlerle güncelle

    /** Onboarding container / root view — sayfanın yüklendiğini doğrular */
    @AndroidFindBy(accessibility = "TODO_onboarding_container")
    private WebElement onboardingContainer;

    /** Aktif adımın başlık metni */
    @AndroidFindBy(accessibility = "TODO_onboarding_step_title")
    private WebElement stepTitle;

    /** Aktif adımın açıklama metni */
    @AndroidFindBy(accessibility = "TODO_onboarding_step_description")
    private WebElement stepDescription;

    /** "İleri" / "Next" butonu */
    @AndroidFindBy(accessibility = "TODO_onboarding_next_button")
    private WebElement nextButton;

    /** "Atla" / "Skip" butonu (varsa — bazı onboarding'lerde olmayabilir) */
    @AndroidFindBy(accessibility = "TODO_onboarding_skip_button")
    private WebElement skipButton;

    /** Son adımdaki "Bitir" / "Finish" / "Let's Go" butonu */
    @AndroidFindBy(accessibility = "TODO_onboarding_finish_button")
    private WebElement finishButton;

    /**
     * İlerleme göstergesi noktaları (ViewPager indicator dots).
     * Kaç adım olduğunu ve hangi adımda olunduğunu tespit etmek için kullanılır.
     * TODO: Appium Inspector'da bu element yoksa kaldır.
     */
    @AndroidFindBy(xpath = "TODO_onboarding_indicator_dots_xpath")
    private List<WebElement> indicatorDots;

    // ─── Page Load Doğrulama ──────────────────────────────────────────────────

    @Override
    public boolean isPageLoaded() {
        boolean loaded = isDisplayed(onboardingContainer);
        log.info("OnboardingPage yüklendi mi: {}", loaded);
        return loaded;
    }

    // ─── Adım Bilgisi ────────────────────────────────────────────────────────

    /**
     * Aktif adımın başlığını döner.
     */
    public String getStepTitle() {
        return getText(stepTitle);
    }

    /**
     * Aktif adımın açıklamasını döner.
     */
    public String getStepDescription() {
        return getText(stepDescription);
    }

    /**
     * Kaç adım olduğunu döner (indicator dot sayısından).
     * indicator dots yoksa -1 döner.
     */
    public int getTotalStepCount() {
        try {
            return indicatorDots.size();
        } catch (Exception e) {
            log.warn("Indicator dots bulunamadı, adım sayısı bilinmiyor.");
            return -1;
        }
    }

    // ─── Navigasyon ──────────────────────────────────────────────────────────

    /**
     * "Next" butonuna tıklar. Aynı sayfada kalır (bir sonraki slide açılır).
     */
    public OnboardingPage clickNext() {
        log.info("Onboarding Next butonuna tıklanıyor.");
        click(nextButton);
        return this;
    }

    /**
     * "Skip" butonunun görünür olup olmadığını döner.
     */
    public boolean isSkipButtonVisible() {
        return isDisplayed(skipButton);
    }

    /**
     * "Finish" / "Done" butonunun görünür olup olmadığını döner.
     * Son adımda Next yerine bu buton görünür.
     */
    public boolean isFinishButtonVisible() {
        return isDisplayed(finishButton);
    }

    /**
     * "Next" mi yoksa "Finish" mi görünüyor — ikisini birden kontrol eder.
     */
    public boolean isNextButtonVisible() {
        return isDisplayed(nextButton);
    }

    /**
     * Onboarding'in son adımında "Finish" butonuna tıklar.
     * Onboarding tamamlandıktan sonra uygulamanın ana ekranına geçilir.
     * TODO: finish sonrası açılan ekrana göre dönüş tipini değiştir
     *       (ör: HomeScreen, Dashboard, LoginPage).
     */
    public void clickFinish() {
        log.info("Onboarding Finish butonuna tıklanıyor — onboarding tamamlanıyor.");
        click(finishButton);
    }

    /**
     * "Skip" butonuna tıklayarak onboarding'i atlar.
     */
    public void clickSkip() {
        log.info("Onboarding Skip butonuna tıklanıyor.");
        click(skipButton);
    }

    /**
     * Tüm onboarding adımlarını sırayla tamamlar:
     *   - Next butonunu görünür olduğu sürece tıklar
     *   - Son adımda Finish butonuna tıklar
     *
     * @param maxSteps Sonsuz döngüden kaçınmak için maksimum adım limiti
     */
    public void completeAllSteps(int maxSteps) {
        log.info("Onboarding adımları tamamlanıyor (max {} adım).", maxSteps);
        int step = 0;

        while (step < maxSteps) {
            step++;
            log.info("Onboarding adım: {}", step);

            if (isFinishButtonVisible()) {
                log.info("Son adım tespit edildi — Finish butonuna tıklanıyor.");
                clickFinish();
                return;
            }

            if (isNextButtonVisible()) {
                clickNext();
            } else {
                log.warn("Ne Next ne de Finish butonu görünüyor (adım {}). Döngü durduruluyor.", step);
                break;
            }
        }

        // Döngü bittiyse Finish henüz tıklanmamış olabilir
        if (isFinishButtonVisible()) {
            clickFinish();
        }
    }

    /**
     * Varsayılan maks. 10 adımla onboarding'i tamamlar.
     */
    public void completeAllSteps() {
        completeAllSteps(10);
    }
}
