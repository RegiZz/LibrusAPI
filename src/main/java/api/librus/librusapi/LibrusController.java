package api.librus.librusapi;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class LibrusController {

    @Autowired
    private LibrusLoginService librusLoginService;

    @Autowired
    private PlanLekcjiService planLekcjiService;

    static class LoginRequest {
        public String username;
        public String password;
    }

    @PostMapping("/api/planlekcji")
    public List<Map<String, String>> getPlanLekcji(@RequestBody LoginRequest loginRequest) throws InterruptedException {
        WebDriver driver = null;
        List<Map<String, String>> planLekcji = null;

        try {
            // Logowanie do Librusa
            driver = librusLoginService.login(loginRequest.username, loginRequest.password);

            // Pobieranie planu lekcji
            planLekcji = planLekcjiService.getPlanLekcji(driver);

        } finally {
            // Zamykanie WebDrivera
            if (driver != null) {
                librusLoginService.quit();
            }
        }

        return planLekcji;
    }
}
