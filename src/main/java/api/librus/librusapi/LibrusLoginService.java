package api.librus.librusapi;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;

@Service
public class LibrusLoginService {

    private final String apiUrl = "https://api.librus.pl";
    private String token;

    public String login(String username, String password) {
        String loginUrl = apiUrl + "/OAuth/Authorization?client_id=46&response_type=code&scope=mydata";

        try {
            // Wysyłanie żądania logowania
            HttpResponse<String> loginResponse = Unirest.post(loginUrl)
                    .field("action", "login")
                    .field("login", username)
                    .field("pass", password)
                    .asString();

            if (loginResponse.getStatus() == 200) {
                this.token = loginResponse.getHeaders().getFirst("Authorization");
                activateApiAccess();
                return this.token;
            } else {
                throw new RuntimeException("Błąd logowania: " + loginResponse.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Wystąpił błąd: " + e.getMessage();
        }
    }

    public boolean activateApiAccess() {
        try {
            // Uzyskanie informacji o tokenie
            HttpResponse<String> tokenInfoResponse = Unirest.get(apiUrl + "/Auth/TokenInfo")
                    .header("Authorization", "Bearer " + token)
                    .asString();

            if (tokenInfoResponse.getStatus() == 200) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getToken() {
        return token; // Zwraca aktualny token
    }
}
