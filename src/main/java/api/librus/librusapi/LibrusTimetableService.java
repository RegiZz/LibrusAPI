package api.librus.librusapi;


import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class LibrusTimetableService {
    public Map<String, Object> getTimetable(LocalDate from, LocalDate to, String token) {
        // Tworzenie formularza danych (POST)
        Map<String, Object> formData = new HashMap<>();
        formData.put("tydzien", from.toString() + "_" + to.toString());

        // Wysłanie żądania POST do API Librusa
        HttpResponse<String> response = Unirest.post("https://api.librus.pl/przegladaj_plan_lekcji")
                .header("Authorization", "Bearer " + token)
                .fields(formData)
                .asString();

        // Parsowanie odpowiedzi HTML
        Document document = Jsoup.parse(response.getBody());
        Elements rows = document.select("table.decorated.plan-lekcji tr:nth-child(odd)");

        // Inicjalizacja wynikowej mapy
        Map<String, Object> timetable = new HashMap<>();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        // Zmienna przechowująca godziny lekcji
        List<String> hours = new ArrayList<>();

        // Tworzenie mapy dla każdego dnia tygodnia
        Map<String, List<String>> lessonsByDay = new HashMap<>();
        for (String day : days) {
            lessonsByDay.put(day, new ArrayList<>());
        }

        // Iterowanie po wierszach tabeli
        for (Element row : rows) {
            // Pobranie godziny lekcji
            String hour = row.select("th").text();
            hours.add(hour);

            // Iterowanie po komórkach lekcji (każdy dzień ma osobną komórkę)
            Elements cells = row.select("td:not(:first-child):not(:last-child)");
            for (int i = 0; i < cells.size(); i++) {
                Element cell = cells.get(i);
                String lessonText = cell.select(".text").text().trim();

                // Jeśli istnieje lekcja, dodajemy ją do odpowiedniego dnia tygodnia
                if (!lessonText.isEmpty()) {
                    lessonsByDay.get(days[i]).add(lessonText);
                } else {
                    lessonsByDay.get(days[i]).add("No lesson");
                }
            }
        }

        // Złożenie końcowej mapy wynikowej
        timetable.put("hours", hours);
        timetable.put("lessons", lessonsByDay);

        return timetable;
    }

}