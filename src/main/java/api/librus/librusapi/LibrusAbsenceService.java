package api.librus.librusapi;


import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LibrusAbsenceService {

    private static final String BASE_URL = "https://api.librus.pl";

    public Map<String, Object> getAbsences(String token) {
        Map<String, Object> absenceSummary = new HashMap<>();

        HttpResponse<JsonNode> response = Unirest.get(BASE_URL + "/przegladaj_nb/uczen/")
                .header("Authorization", "Bearer " + token)
                .asJson();

        if (response.getStatus() == 200) {
            JSONArray absencesArray = response.getBody().getObject().getJSONArray("absences");

            int totalClasses = 0;
            int missedClasses = 0;
            Map<String, Integer> subjectAttendance = new HashMap<>();

            for (int i = 0; i < absencesArray.length(); i++) {
                JSONObject absence = absencesArray.getJSONObject(i);
                String subject = absence.getString("subject");
                boolean isPresent = absence.getBoolean("isPresent");

                totalClasses++;

                if (!isPresent) {
                    missedClasses++;
                    subjectAttendance.put(subject, subjectAttendance.getOrDefault(subject, 0) + 1);
                }
            }

            absenceSummary.put("totalClasses", totalClasses);
            absenceSummary.put("missedClasses", missedClasses);
            absenceSummary.put("attendancePercentage", ((totalClasses - missedClasses) / (double) totalClasses) * 100);
            absenceSummary.put("subjectAttendance", subjectAttendance);
        } else {
            absenceSummary.put("error", "Failed to retrieve absences. Status: " + response.getStatus());
        }

        return absenceSummary;
    }

    public Map<String, Object> getAbsencesForSubject(String token, String subjectName) {
        Map<String, Object> subjectAbsences = new HashMap<>();

        HttpResponse<JsonNode> response = Unirest.get(BASE_URL + "/przegladaj_nb/szczegoly/")
                .header("Authorization", "Bearer " + token)
                .asJson();

        if (response.getStatus() == 200) {
            JSONArray absencesArray = response.getBody().getObject().getJSONArray("absences");

            int totalSubjectClasses = 0;
            int missedSubjectClasses = 0;

            for (int i = 0; i < absencesArray.length(); i++) {
                JSONObject absence = absencesArray.getJSONObject(i);
                String subject = absence.getString("subject");
                boolean isPresent = absence.getBoolean("isPresent");

                if (subject.equalsIgnoreCase(subjectName)) {
                    totalSubjectClasses++;
                    if (!isPresent) {
                        missedSubjectClasses++;
                    }
                }
            }

            subjectAbsences.put("totalSubjectClasses", totalSubjectClasses);
            subjectAbsences.put("missedSubjectClasses", missedSubjectClasses);
            subjectAbsences.put("attendancePercentage",
                    totalSubjectClasses > 0 ? ((totalSubjectClasses - missedSubjectClasses) / (double) totalSubjectClasses) * 100 : 0.0);
        } else {
            subjectAbsences.put("error", "Failed to retrieve absences. Status: " + response.getStatus());
        }

        return subjectAbsences;
    }
}
