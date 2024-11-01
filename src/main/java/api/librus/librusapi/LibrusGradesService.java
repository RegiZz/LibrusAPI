package api.librus.librusapi;

import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import kong.unirest.HttpResponse;

import java.util.*;

@Service
public class LibrusGradesService {
    public List<Map<String, Object>> getGrades(String token) {
        List<Map<String, Object>> gradesList = new ArrayList<>();
        // Get the grades from Librus
        HttpResponse<String> response = Unirest.get("https://api.librus.pl/przegladaj_oceny/uczen")
                .header("Authorization", "Bearer " + token)
                .asString();

        Document document = Jsoup.parse(response.getBody());
        Elements rows = document.select("table.decorated.stretch > tbody > tr[class^='line']:not([name])");

        for (Element row : rows) {
            Map<String, Object> gradeData = parseRow(row);
            if (gradeData != null) {
                gradesList.add(gradeData);
            }
        }

        return gradesList;
    }
    private Map<String, Object> parseRow(Element row) {
        Elements columns = row.select("td");

        String subjectName = columns.get(1).text();
        if (subjectName.isEmpty()) {
            return null;
        }

        Map<String, Object> gradeInfo = new HashMap<>();
        gradeInfo.put("name", subjectName);
        gradeInfo.put("semester", Arrays.asList(parseSemester(columns, 2), parseSemester(columns, 5),
                parseSemester(columns, 6), parseSemester(columns, 9)));
        gradeInfo.put("tempAverage", parseAverage(columns.get(8)));
        gradeInfo.put("average", parseAverage(columns.get(9)));

        return gradeInfo;
    }

    private double parseAverage(Element column) {
        try {
            return Double.parseDouble(column.text());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private Map<String, Object> parseSemester(Elements columns, int startColumn) {
        Map<String, Object> semesterInfo = new HashMap<>();
        Elements gradeElements = columns.get(startColumn).select("span.grade-box");

        List<Map<String, String>> grades = new ArrayList<>();
        for (Element gradeElement : gradeElements) {
            Map<String, String> grade = new HashMap<>();
            grade.put("id", gradeElement.select("a").attr("href").split("/")[1]);
            grade.put("info", gradeElement.select("a").attr("title").replaceAll("<\\s*br\\s*/?\\s*>", "\n"));
            grade.put("value", gradeElement.text());
            grades.add(grade);
        }

        semesterInfo.put("grades", grades);
        semesterInfo.put("tempAverage", parseAverage(columns.get(startColumn + 1)));
        semesterInfo.put("average", parseAverage(columns.get(startColumn + 2)));

        return semesterInfo;
    }

    public double getAverage(String token) {
        List<Map<String, Object>> gradesList = getGrades(token);
        double totalSum = 0.0;
        int gradeCount = 0;

        for (Map<String, Object> subject : gradesList) {
            Double subjectAverage = (Double) subject.get("average");
            if (subjectAverage != null && subjectAverage > 0) {
                totalSum += subjectAverage;
                gradeCount++;
            }
        }

        return gradeCount > 0 ? totalSum / gradeCount : 0.0;
    }

    public double getAverage(String token, String subjectName) {
        List<Map<String, Object>> gradesList = getGrades(token);
        for (Map<String, Object> subject : gradesList) {
            String name = (String) subject.get("name");
            Double subjectAverage = (Double) subject.get("average");
            if (name != null && name.equalsIgnoreCase(subjectName) && subjectAverage != null) {
                return subjectAverage;
            }
        }
        return 0.0;
    }
}
