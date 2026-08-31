package com.mobileautomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * PrivacyPolicyPage - Gizlilik Politikası ekranı.
 *
 * LOCATOR GÜNCELLEME:
 *   Appium Inspector'dan aldığın değerleri @AndroidFindBy anotasyonlarına yaz.
 */
public class PrivacyPolicyPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────────────────
    // TODO: Appium Inspector'dan alınan değerlerle güncelle

    /** Sayfa başlığı — "Privacy Policy" veya "Gizlilik Politikası" */
    @AndroidFindBy(accessibility = "Privacy Policy")
    private WebElement privacyTitle;

    /** İçerik alanı */
    @AndroidFindBy(accessibility = "Privacy Policy")
    private WebElement privacyContent;

    /** Geri butonu */
    @AndroidFindBy(accessibility = "TODO_back_button")
    private WebElement backButton;

    // ─── Page Load Doğrulama ──────────────────────────────────────────────────

    @Override
    public boolean isPageLoaded() {
        boolean loaded = isDisplayed(privacyTitle);
        log.info("PrivacyPolicyPage yüklendi mi: {}", loaded);
        return loaded;
    }

    // ─── Aksiyonlar ──────────────────────────────────────────────────────────

    /**
     * Sayfa başlığının metnini döner.
     */
    public String getTitle() {
        return getText(privacyTitle);
    }

    /**
     * İçerik alanının görünür olup olmadığını döner.
     */
    public boolean isContentVisible() {
        return isDisplayed(privacyContent);
    }

    /**
     * Toolbar geri butonuna tıklayarak GetStartedPage'e döner.
     */
    public GetStartedPage goBack() {
        log.info("PrivacyPolicyPage'den geri dönülüyor.");
        click(backButton);
        return new GetStartedPage();
    }

    /**
     * Android native back gesture ile GetStartedPage'e döner.
     */
    public GetStartedPage goBackWithNativeBack() {
        log.info("Native back ile PrivacyPolicyPage'den geri dönülüyor.");
        driver.navigate().back();
        return new GetStartedPage();
    }
}
