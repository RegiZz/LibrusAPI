package api.librus.librusapi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class LibrusLoginService {

    private WebDriver driver;

    // Metoda logująca użytkownika do systemu Librus
    public WebDriver login(String username, String password) throws InterruptedException {
        // Ustawienia dla Selenium WebDriver
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        driver = new ChromeDriver(options);

        driver.get("https://portal.librus.pl/rodzina/login");

        // Logowanie
        WebElement loginField = driver.findElement(By.name("login"));
        WebElement passwordField = driver.findElement(By.name("password"));
        loginField.sendKeys(username);
        passwordField.sendKeys(password);

        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        // Czekaj na zalogowanie
        Thread.sleep(2000);

        return driver; // Zwracamy WebDriver, który będzie używany w dalszej części aplikacji
    }

    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
}
