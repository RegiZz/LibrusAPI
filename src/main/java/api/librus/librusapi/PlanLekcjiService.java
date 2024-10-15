package api.librus.librusapi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlanLekcjiService {

    // Metoda pobierająca plan lekcji
    public List<Map<String, String>> getPlanLekcji(WebDriver driver) throws InterruptedException {
        List<Map<String, String>> planLekcji = new ArrayList<>();

        // Przejdź do strony planu lekcji
        driver.get("https://portal.librus.pl/planlekcji");

        Thread.sleep(2000);



        // Pobierz dane z tabeli planu lekcji z aktualnego tygodnia
        List<WebElement> rows = driver.findElements(By.xpath("//table[@class='planLekcji']/tbody/tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (!cells.isEmpty()) {
                Map<String, String> lekcja = new HashMap<>();
                lekcja.put("godzina", cells.get(0).getText());  // Godzina lekcji
                lekcja.put("przedmiot", cells.get(1).getText());  // Przedmiot
                lekcja.put("nauczyciel", cells.get(2).getText()); // Nauczyciel
                // Sprawdzanie czy lekcja jest odwołana
                if (row.getAttribute("class").contains("cancelled")) {
                    lekcja.put("status", "odwołana");
                } else {
                    lekcja.put("status", "normalna");
                }

                planLekcji.add(lekcja);
            }
        }

        return planLekcji;
    }
}
