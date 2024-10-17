package api.librus.librusapi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class LibrusLoginService {
    WebDriver driver = null;
    // Metoda logująca użytkownika do systemu Librus
    public WebDriver login(String username, String password) throws InterruptedException {

        try {
            // Ustawienia dla Selenium WebDriver
            System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox"); // Bypass OS security model
            options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems

            driver = new ChromeDriver(options);
            System.out.println("WebDriver został zainicjalizowany");

            driver.get("https://portal.librus.pl/rodzina/login");
            System.out.println("Zaladowano strone logowania");

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
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
}
