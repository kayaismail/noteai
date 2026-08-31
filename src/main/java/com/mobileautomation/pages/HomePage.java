package com.mobileautomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * HomePage - Uygulamanın ana ekranını temsil eder.
 *
 * ADAPTASYON:
 *   @AndroidFindBy locator'larını kendi uygulamanıza göre güncelleyin.
 */
public class HomePage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────────────────

    @AndroidFindBy(accessibility = "home_screen_title")
    private WebElement homeScreenTitle;

    @AndroidFindBy(id = "com.example.app:id/welcome_text")
    private WebElement welcomeText;

    @AndroidFindBy(accessibility = "search_button")
    private WebElement searchButton;

    @AndroidFindBy(accessibility = "profile_icon")
    private WebElement profileIcon;

    @AndroidFindBy(accessibility = "menu_button")
    private WebElement menuButton;

    @AndroidFindBy(accessibility = "logout_button")
    private WebElement logoutButton;

    @AndroidFindBy(id = "com.example.app:id/notification_badge")
    private WebElement notificationBadge;

    // ─── Page Load Doğrulama ──────────────────────────────────────────────────

    @Override
    public boolean isPageLoaded() {
        boolean loaded = isDisplayed(homeScreenTitle);
        log.info("HomePage yüklendi mi: {}", loaded);
        return loaded;
    }

    // ─── Aksiyonlar ──────────────────────────────────────────────────────────

    /**
     * Karşılama mesajını döner.
     */
    public String getWelcomeText() {
        return getText(welcomeText);
    }

    /**
     * Ana ekran başlığını döner.
     */
    public String getHomeTitle() {
        return getText(homeScreenTitle);
    }

    /**
     * Arama butonuna tıklar.
     */
    public void clickSearch() {
        log.info("Arama butonuna tıklanıyor.");
        click(searchButton);
    }

    /**
     * Profil ikonuna tıklar.
     */
    public void clickProfile() {
        log.info("Profil ikonuna tıklanıyor.");
        click(profileIcon);
    }

    /**
     * Menü butonuna tıklar.
     */
    public void clickMenu() {
        log.info("Menü butonuna tıklanıyor.");
        click(menuButton);
    }

    /**
     * Uygulamadan çıkış yapar ve LoginPage döner.
     */
    public LoginPage logout() {
        log.info("Çıkış yapılıyor.");
        click(logoutButton);
        return new LoginPage();
    }

    /**
     * Bildirim rozetinin görünür olup olmadığını döner.
     */
    public boolean hasNotification() {
        return isDisplayed(notificationBadge);
    }
}
