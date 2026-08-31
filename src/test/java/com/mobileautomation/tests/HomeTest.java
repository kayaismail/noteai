package com.mobileautomation.tests;

import com.mobileautomation.pages.HomePage;
import com.mobileautomation.pages.LoginPage;
import com.mobileautomation.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * HomeTest - Ana ekrana ait test senaryoları.
 *
 * Önkoşul: Her test, önce login yaparak HomePage'e ulaşır.
 * Bu ortak adım loginAndGetHomePage() helper metodu ile tekrarsız hale getirilmiştir.
 */
public class HomeTest extends BaseTest {

    // ─── Helper ──────────────────────────────────────────────────────────────

    /**
     * Geçerli credentials ile login yapar ve HomePage döner.
     */
    private HomePage loginAndGetHomePage() {
        String username = ConfigReader.get("test.username");
        String password = ConfigReader.get("test.password");

        LoginPage loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isPageLoaded(),
                "Login sayfası yüklenmedi — test önkoşulu sağlanamadı!");

        HomePage homePage = loginPage.login(username, password);
        Assert.assertTrue(homePage.isPageLoaded(),
                "Ana sayfa login sonrası yüklenmedi!");
        return homePage;
    }

    // ─── Test Senaryoları ────────────────────────────────────────────────────

    @Test(description = "Başarılı giriş sonrası ana sayfa yüklenmiş olmalı")
    public void testHomePageLoadsAfterLogin() {
        // Act + Assert
        HomePage homePage = loginAndGetHomePage();
        Assert.assertTrue(homePage.isPageLoaded(),
                "Ana sayfa yüklenmedi!");
    }

    @Test(description = "Karşılama mesajı boş olmamalı")
    public void testWelcomeTextIsVisible() {
        // Arrange
        HomePage homePage = loginAndGetHomePage();

        // Act
        String welcomeText = homePage.getWelcomeText();

        // Assert
        Assert.assertNotNull(welcomeText, "Karşılama metni null!");
        Assert.assertFalse(welcomeText.isBlank(), "Karşılama metni boş!");
    }

    @Test(description = "Arama butonuna tıklanabilmeli")
    public void testSearchButtonIsClickable() {
        // Arrange
        HomePage homePage = loginAndGetHomePage();

        // Act — exception fırlatmadan tıklanabilmeli
        homePage.clickSearch();

        // Assert — uygulama çökmeden bu noktaya geldiyse başarılı
        Assert.assertTrue(true, "Arama butonuna tıklanamadı!");
    }

    @Test(description = "Çıkış yapıldığında Login sayfasına dönülmeli")
    public void testLogoutNavigatesToLoginPage() {
        // Arrange
        HomePage homePage = loginAndGetHomePage();

        // Act
        LoginPage loginPage = homePage.logout();

        // Assert
        Assert.assertTrue(loginPage.isPageLoaded(),
                "Logout sonrası Login sayfası yüklenmedi!");
    }
}
