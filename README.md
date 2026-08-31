# Appium Android Automation Framework

Java + Appium + TestNG + POM mimarisiyle kurulmuş Android mobil otomasyon projesi.

---

## Proje Yapısı

```
appium-android-automation/
├── pom.xml
└── src/
    ├── main/java/com/mobileautomation/
    │   ├── driver/
    │   │   └── DriverManager.java         # ThreadLocal AndroidDriver yönetimi
    │   ├── pages/
    │   │   ├── BasePage.java              # Tüm page'lerin base sınıfı
    │   │   ├── LoginPage.java             # Giriş ekranı POM
    │   │   └── HomePage.java             # Ana ekran POM
    │   └── utils/
    │       ├── ConfigReader.java          # config.properties okuyucu
    │       ├── ExtentReportManager.java   # HTML rapor yöneticisi
    │       └── ScreenshotUtils.java       # Ekran görüntüsü yardımcısı
    └── test/
        ├── java/com/mobileautomation/tests/
        │   ├── BaseTest.java              # setUp / tearDown hookları
        │   ├── LoginTest.java             # Giriş senaryoları
        │   └── HomeTest.java             # Ana ekran senaryoları
        └── resources/
            ├── config.properties          # Cihaz ve uygulama konfigürasyonu
            ├── log4j2.xml                 # Log konfigürasyonu
            └── testng.xml                 # Test suite tanımı
```

---

## Ön Gereksinimler

| Araç | Versiyon | İndirme |
|------|----------|---------|
| Java JDK | 11+ | https://adoptium.net |
| Apache Maven | 3.8+ | https://maven.apache.org |
| Node.js | 18+ | https://nodejs.org |
| Appium | 2.x | `npm install -g appium` |
| UiAutomator2 Driver | latest | `appium driver install uiautomator2` |
| Android SDK / ADB | - | Android Studio ile gelir |

---

## Kurulum Adımları

### 1. Appium Server Kurulumu

```bash
# Appium'u global kur
npm install -g appium

# UiAutomator2 driver'ı kur
appium driver install uiautomator2

# Kurulumu doğrula
appium driver list
```

### 2. Android Cihaz Hazırlığı

Telefonu USB ile bilgisayara bağlayın ve şu ayarları yapın:

1. **Geliştirici Seçenekleri** → Ayarlar > Telefon Hakkında > Derleme Numarasına 7 kez dokunun
2. **USB Hata Ayıklama (USB Debugging)** → Geliştirici Seçenekleri > açık
3. Bağlantıyı doğrulayın:
   ```bash
   adb devices
   # Çıktı: emulator-5554   device  (veya cihaz seri numarası)
   ```

### 3. Uygulama Bilgilerini Öğrenin

```bash
# Telefonda açık olan uygulamanın package ve activity bilgisi
adb shell dumpsys window | grep mCurrentFocus

# Örnek çıktı:
# mCurrentFocus=Window{... com.example.myapp/com.example.myapp.MainActivity}
```

### 4. config.properties Güncelleme

`src/test/resources/config.properties` dosyasını kendi değerlerinizle doldurun:

```properties
appium.server.url=http://127.0.0.1:4723
device.name=Android Device        # adb devices çıktısındaki isim
platform.version=13               # Telefonunuzun Android versiyonu
app.package=com.example.myapp     # Uygulamanın package adı
app.activity=com.example.myapp.MainActivity
test.username=testuser@example.com
test.password=Test@1234
```

### 5. Locator'ları Güncelleme

`LoginPage.java` ve `HomePage.java` içindeki `@AndroidFindBy` locator'larını
**Appium Inspector** ile bulduğunuz gerçek element locator'larıyla değiştirin.

```bash
# Appium Inspector'ı başlatmak için önce server'ı çalıştırın
appium --port 4723

# Sonra Appium Inspector uygulamasını indirin:
# https://github.com/appium/appium-inspector/releases
```

---

## Testleri Çalıştırma

### Appium Server'ı Başlatın (ayrı terminalde)

```bash
appium --port 4723
```

### Tüm Testleri Çalıştır

```bash
mvn test
```

### Belirli Test Sınıfı

```bash
mvn test -Dtest=LoginTest
```

### Belirli Test Metodu

```bash
mvn test -Dtest=LoginTest#testSuccessfulLogin
```

### Belirli TestNG Suite

```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

---

## Raporlar ve Loglar

Test çalıştıktan sonra çıktılar şu klasörlerde oluşur:

| Çıktı | Konum |
|-------|-------|
| HTML Rapor | `test-output/reports/AppiumTestReport_*.html` |
| Log Dosyası | `test-output/logs/test-run.log` |
| Hata Screenshots | `test-output/screenshots/` |

---

## Mimari Kararlar

| Karar | Gerekçe |
|-------|---------|
| ThreadLocal Driver | Paralel test desteği için thread-safe driver yönetimi |
| BasePage + PageFactory | Ortak aksiyonlar tek noktada, DRY prensibine uygun |
| ConfigReader env var override | CI/CD pipeline'larında dışarıdan değer geçilebilir |
| ExtentReports Spark | Dark theme destekli, detaylı HTML rapor çıktısı |
| @AfterMethod screenshot | Sadece başarısız testlerde screenshot alınır, disk tasarrufu |

---

## Yeni Sayfa Eklemek

1. `src/main/java/com/mobileautomation/pages/` altına yeni bir class oluşturun
2. `BasePage`'den extend edin
3. `@AndroidFindBy` ile locator'ları tanımlayın
4. `isPageLoaded()` metodunu override edin
5. Aksiyon metodlarını yazın

```java
public class SearchPage extends BasePage {

    @AndroidFindBy(accessibility = "search_input")
    private WebElement searchInput;

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(searchInput);
    }

    public SearchPage search(String query) {
        sendKeys(searchInput, query);
        return this;
    }
}
```

---

## Sorun Giderme

**`adb devices` cihazı görmüyor**
- USB kablosunu kontrol edin (data kablosu olmalı, şarj kablosu değil)
- USB Debugging'in açık olduğunu doğrulayın
- `adb kill-server && adb start-server` deneyin

**`SessionNotCreatedException`**
- config.properties'deki `app.package` ve `app.activity` değerlerini doğrulayın
- Appium server'ın çalıştığını kontrol edin (`http://127.0.0.1:4723/status`)

**Element bulunamıyor (`NoSuchElementException`)**
- Appium Inspector ile güncel locator'ları alın
- `@AndroidFindBy` locator'larını güncelleyin
- `implicit.wait` değerini artırmayı deneyin
