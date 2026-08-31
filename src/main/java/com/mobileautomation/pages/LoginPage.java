package com.mobileautomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * LoginPage - Uygulamanın giriş ekranını temsil eder.
 *
 * ADAPTASYON:
 *   @AndroidFindBy içindeki locator değerlerini
 *   kendi uygulamanıza göre güncelleyin.
 *   uiAutomator2 inspector (Appium Inspector) ile bulabilirsiniz.
 */
public class LoginPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────────────────

    @AndroidFindBy(accessibility = "username_field")
    private WebElement usernameInput;

    @AndroidFindBy(accessibility = "password_field")
    private WebElement passwordInput;

    @AndroidFindBy(accessibility = "login_button")
    private WebElement loginButton;

    @AndroidFindBy(accessibility = "error_message")
    private WebElement errorMessage;

    @AndroidFindBy(id = "com.example.app:id/forgot_password_link")
    private WebElement forgotPasswordLink;

    // ─── Page Load Doğrulama ──────────────────────────────────────────────────

    @Override
    public boolean isPageLoaded() {
        boolean loaded = isDisplayed(loginButton);
        log.info("LoginPage yüklendi mi: {}", loaded);
        return loaded;
    }

    // ─── Aksiyonlar ──────────────────────────────────────────────────────────

    /**
     * Kullanıcı adı alanına yazar.
     */
    public LoginPage enterUsername(String username) {
        log.info("Kullanıcı adı giriliyor: {}", username);
        sendKeys(usernameInput, username);
        return this;
    }

    /**
     * Şifre alanına yazar.
     */
    public LoginPage enterPassword(String password) {
        log.info("Şifre giriliyor.");
        sendKeys(passwordInput, password);
        return this;
    }

    /**
     * Login butonuna tıklar ve başarılı girişte HomePage döner.
     */
    public HomePage clickLoginButton() {
        log.info("Login butonuna tıklanıyor.");
        click(loginButton);
        return new HomePage();
    }

    /**
     * Giriş bilgilerini doldurup login butonuna tıklar (fluent API).
     */
    public HomePage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
    }

    /**
     * Hata mesajının görünür olup olmadığını döner.
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    /**
     * Hata mesajının metnini döner.
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * "Şifremi Unuttum" linkine tıklar.
     */
    public void clickForgotPassword() {
        log.info("Şifremi unuttum linkine tıklanıyor.");
        click(forgotPasswordLink);
    }
}
