package com.mobileautomation.pages;

import com.mobileautomation.driver.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage - Tüm Page Object sınıflarının kalıtım aldığı temel sınıf.
 * Ortak WebElement işlemlerini ve bekleme mekanizmalarını barındırır.
 */
public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(this.getClass());
    protected AndroidDriver driver;
    protected WebDriverWait wait;

    private static final int DEFAULT_TIMEOUT = 15;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        // @AndroidFindBy annotasyonlarını aktif eder
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        log.debug("{} sayfası oluşturuldu.", this.getClass().getSimpleName());
    }

    // ─── Bekleme Yardımcıları ────────────────────────────────────────────────

    /**
     * Element görünür hale gelene kadar bekler.
     */
    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * By locator ile element görünür hale gelene kadar bekler.
     */
    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Element tıklanabilir hale gelene kadar bekler.
     */
    protected WebElement waitForClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * By locator ile element tıklanabilir hale gelene kadar bekler.
     */
    protected WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ─── Temel Aksiyon Metodları ─────────────────────────────────────────────

    /**
     * Elemente tıklar (önce tıklanabilir olmasını bekler).
     */
    protected void click(WebElement element) {
        waitForClickability(element).click();
        log.debug("Tıklandı: {}", element);
    }

    /**
     * Elemente metin yazar (önce temizler).
     */
    protected void sendKeys(WebElement element, String text) {
        waitForVisibility(element).clear();
        element.sendKeys(text);
        log.debug("Yazıldı: '{}' → {}", text, element);
    }

    /**
     * Elementin text değerini döner.
     */
    protected String getText(WebElement element) {
        String text = waitForVisibility(element).getText();
        log.debug("getText: '{}' → {}", text, element);
        return text;
    }

    /**
     * Elementin ekranda görünür olup olmadığını döner (exception fırlatmaz).
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

    /**
     * By locator ile element var mı kontrol eder.
     */
    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // ─── Sayfa Geçiş Kontrolü ────────────────────────────────────────────────

    /**
     * Alt sınıfların sayfa yüklenme doğrulaması için override ettiği metod.
     */
    public abstract boolean isPageLoaded();
}
