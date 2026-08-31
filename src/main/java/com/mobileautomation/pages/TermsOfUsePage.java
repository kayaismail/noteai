package com.mobileautomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * TermsOfUsePage - Kullanım Koşulları ekranı.
 *
 * LOCATOR GÜNCELLEME:
 *   Appium Inspector'dan aldığın değerleri @AndroidFindBy anotasyonlarına yaz.
 */
public class TermsOfUsePage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────────────────
    // TODO: Appium Inspector'dan alınan değerlerle güncelle

    /** Sayfa başlığı — "Terms of Use" veya "Kullanım Koşulları" */
    @AndroidFindBy(accessibility = "Terms of Use")
    private WebElement termsTitle;

    /** Terms içerik alanı (ScrollView veya TextView) */
    @AndroidFindBy(accessibility = "Terms of Use")
    private WebElement termsContent;

    /** Geri butonu — native back veya toolbar back arrow */
    @AndroidFindBy(accessibility = "TODO_back_button")
    private WebElement backButton;

    /** "Kabul Et" / "Accept" butonu (varsa) */
    @AndroidFindBy(accessibility = "TODO_accept_button")
    private WebElement acceptButton;

    // ─── Page Load Doğrulama ──────────────────────────────────────────────────

    @Override
    public boolean isPageLoaded() {
        boolean loaded = isDisplayed(termsTitle);
        log.info("TermsOfUsePage yüklendi mi: {}", loaded);
        return loaded;
    }

    // ─── Aksiyonlar ──────────────────────────────────────────────────────────

    /**
     * Sayfa başlığının metnini döner.
     */
    public String getTitle() {
        return getText(termsTitle);
    }

    /**
     * İçerik alanının görünür olup olmadığını döner.
     */
    public boolean isContentVisible() {
        return isDisplayed(termsContent);
    }

    /**
     * Accept butonunun görünür olup olmadığını döner.
     */
    public boolean isAcceptButtonVisible() {
        return isDisplayed(acceptButton);
    }

    /**
     * Toolbar/native geri butonuna tıklayarak GetStartedPage'e döner.
     */
    public GetStartedPage goBack() {
        log.info("TermsOfUsePage'den geri dönülüyor.");
        click(backButton);
        return new GetStartedPage();
    }

    /**
     * Android native back gesture ile GetStartedPage'e döner.
     * Toolbar back butonu yoksa bu metodu kullan.
     */
    public GetStartedPage goBackWithNativeBack() {
        log.info("Native back ile TermsOfUsePage'den geri dönülüyor.");
        driver.navigate().back();
        return new GetStartedPage();
    }
}
