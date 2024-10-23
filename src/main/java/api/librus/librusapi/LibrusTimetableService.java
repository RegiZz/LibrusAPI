package api.librus.librusapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class LibrusTimetableService {
    private final String apiUrl = "https://api.librus.pl";
    private final String token;

    public LibrusTimetableService(@Value("token") String token) {
        this.token = token;
    }

    public Object getTimetables() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = apiUrl + "/Timetables";

            // Dodaj token do nagłówków
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

            return response.getBody();
        } catch (Exception e) {
            System.out.println("[LibrusAPI: Timetables] Szkoła nie obsługuje tej funkcji");
            throw e;
        }
    }
}