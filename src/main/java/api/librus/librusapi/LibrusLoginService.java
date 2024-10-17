package api.librus.librusapi;

import io.github.bonigarcia.wdm.WebDriverManager;
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

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);

        try {
            if(driver == null){
                throw new Exception("Nie udało się stworzyć WebDrivera Chrome");
            }else{
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
        }catch (Exception e) {
            e.printStackTrace();
            driver.quit();
            return null;
        }

    }

    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
}
