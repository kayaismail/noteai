package com.mobileautomation.tests;

import com.mobileautomation.pages.HomePage;
import com.mobileautomation.pages.LoginPage;
import com.mobileautomation.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest - Giriş ekranına ait test senaryoları.
 *
 * Tüm kurulum/teardown BaseTest üzerinden yönetilir.
 * Her @Test metodu bağımsız çalışır — kendi driver'ı başlar, biter.
 */
public class LoginTest extends BaseTest {

    // ─── Başarılı Giriş ──────────────────────────────────────────────────────

    @Test(description = "Geçerli kullanıcı adı ve şifre ile başarılı giriş yapılabilmeli")
    public void testSuccessfulLogin() {
        // Arrange
        String username = ConfigReader.get("test.username");
        String password = ConfigReader.get("test.password");

        LoginPage loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isPageLoaded(),
                "Login sayfası yüklenmedi!");

        // Act
        HomePage homePage = loginPage.login(username, password);

        // Assert
        Assert.assertTrue(homePage.isPageLoaded(),
                "Giriş sonrası Ana Sayfa yüklenmedi!");
    }

    // ─── Başarısız Giriş ─────────────────────────────────────────────────────

    @Test(description = "Geçersiz şifre ile giriş denendiğinde hata mesajı görünmeli")
    public void testLoginWithWrongPassword() {
        // Arrange
        LoginPage loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isPageLoaded(),
                "Login sayfası yüklenmedi!");

        // Act
        loginPage.enterUsername(ConfigReader.get("test.username"))
                 .enterPassword("wrongPassword123!");

        loginPage.clickLoginButton();

        // Assert — hata mesajının göründüğünü doğrula
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Hata mesajı görünmüyor!");
    }

    @Test(description = "Boş alanlarla giriş denendiğinde hata mesajı görünmeli")
    public void testLoginWithEmptyCredentials() {
        // Arrange
        LoginPage loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isPageLoaded(),
                "Login sayfası yüklenmedi!");

        // Act — hiçbir şey yazmadan login butonuna bas
        loginPage.clickLoginButton();

        // Assert
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Boş form submit edildiğinde hata mesajı görünmüyor!");
    }

    @Test(description = "Geçersiz kullanıcı adı ile giriş denendiğinde hata mesajı görünmeli")
    public void testLoginWithInvalidUsername() {
        // Arrange
        LoginPage loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isPageLoaded(),
                "Login sayfası yüklenmedi!");

        // Act
        loginPage.enterUsername("invalid_user_xyz")
                 .enterPassword(ConfigReader.get("test.password"));
        loginPage.clickLoginButton();

        // Assert
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Geçersiz kullanıcı adıyla hata mesajı görünmüyor!");
    }
}
