package com.mobileautomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * GetStartedPage - Uygulama ilk açıldığında karşılanan ekran.
 *
 * LOCATOR GÜNCELLEME:
 *   Appium Inspector'dan aldığın değerleri aşağıdaki @AndroidFindBy
 *   anotasyonlarına yaz. Üç strateji desteklenir:
 *     - accessibility id  → @AndroidFindBy(accessibility = "...")
 *     - resource-id       → @AndroidFindBy(id = "com.pkg:id/...")
 *     - xpath             → @AndroidFindBy(xpath = "//...")
 */
public class GetStartedPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────────────────
    // TODO: Appium Inspector'dan alınan değerlerle güncelle

    /** "Get Started" başlık etiketi — sayfanın yüklenip yüklenmediğini doğrular */
    // Get Started butonu hem title hem de ana CTA olarak kullanılıyor
    @AndroidFindBy(accessibility = "Get Started")
    private WebElement getStartedTitle;

    /** Ana "Get Started" / "Başla" butonu */
    @AndroidFindBy(accessibility = "Get Started")
    private WebElement getStartedButton;

    /** Terms of Use bağlantısı */
    @AndroidFindBy(accessibility = "Terms of Use")
    private WebElement termsOfUseLink;

    /** Privacy Policy bağlantısı */
    @AndroidFindBy(accessibility = "Privacy Policy")
    private WebElement privacyPolicyLink;

    // ─── Page Load Doğrulama ──────────────────────────────────────────────────

    @Override
    public boolean isPageLoaded() {
        boolean loaded = isDisplayed(getStartedTitle);
        log.info("GetStartedPage yüklendi mi: {}", loaded);
        return loaded;
    }

    // ─── Aksiyonlar ──────────────────────────────────────────────────────────

    /**
     * Terms of Use linkine tıklar → TermsOfUsePage döner.
     */
    public TermsOfUsePage openTermsOfUse() {
        log.info("Terms of Use linkine tıklanıyor.");
        click(termsOfUseLink);
        return new TermsOfUsePage();
    }

    /**
     * Privacy Policy linkine tıklar → PrivacyPolicyPage döner.
     */
    public PrivacyPolicyPage openPrivacyPolicy() {
        log.info("Privacy Policy linkine tıklanıyor.");
        click(privacyPolicyLink);
        return new PrivacyPolicyPage();
    }

    /**
     * "Get Started" butonuna tıklar → OnboardingPage döner.
     */
    public OnboardingPage clickGetStarted() {
        log.info("Get Started butonuna tıklanıyor.");
        click(getStartedButton);
        return new OnboardingPage();
    }

    /**
     * Terms of Use linkinin görünür olup olmadığını döner.
     */
    public boolean isTermsOfUseLinkVisible() {
        return isDisplayed(termsOfUseLink);
    }

    /**
     * Privacy Policy linkinin görünür olup olmadığını döner.
     */
    public boolean isPrivacyPolicyLinkVisible() {
        return isDisplayed(privacyPolicyLink);
    }
}
