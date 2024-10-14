package api.librus.librusapi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class LibrusController {

    static class LoginRequest {
        public String username;
        public String password;
    }

    @GetMapping("/api/planlekcji")
    public List<Map<String, String>> getPlanLekcji(@RequestBody LoginRequest loginRequest) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64/chromedriver.exe");

        // Opcje dla WebDrivera (działanie bez GUI)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);
        List<Map<String, String>> planLekcji = new ArrayList<>();

        try {
            driver.get("https://portal.librus.pl/rodzina/login");

            // Logowanie
            WebElement loginField = driver.findElement(By.name("login"));
            WebElement passwordField = driver.findElement(By.name("password"));
            loginField.sendKeys(loginRequest.username);
            passwordField.sendKeys(loginRequest.password);

            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
            loginButton.click();

            // Czekaj na zalogowanie
            Thread.sleep(2000);

            driver.get("https://portal.librus.pl/planlekcji");

            Thread.sleep(2000);

            // Pobierz dane z tabeli planu lekcji
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='planLekcji']/tbody/tr"));
            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() > 0) {
                    Map<String, String> lekcja = new HashMap<>();
                    lekcja.put("godzina", cells.get(0).getText());
                    lekcja.put("przedmiot", cells.get(1).getText());
                    lekcja.put("nauczyciel", cells.get(2).getText());
                    lekcja.put("sala", cells.get(3).getText());
                    planLekcji.add(lekcja);
                }
            }

        } finally {
            driver.quit();
        }

        return planLekcji;
    }
}
