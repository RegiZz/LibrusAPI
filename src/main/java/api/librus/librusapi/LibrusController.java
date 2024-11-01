package api.librus.librusapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/librus")
public class LibrusController {

    private final LibrusLoginService librusLoginService;
    private final LibrusTimetableService librusTimetableService;
    private final LibrusGradesService librusGradesService;
    private final LibrusAbsenceService librusAbsenceService;

    @Autowired
    public LibrusController(LibrusLoginService librusLoginService,
                            LibrusTimetableService librusTimetableService,
                            LibrusGradesService librusGradesService,
                            LibrusAbsenceService librusAbsenceService) {
        this.librusLoginService = librusLoginService;
        this.librusTimetableService = librusTimetableService;
        this.librusGradesService = librusGradesService;
        this.librusAbsenceService = librusAbsenceService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String login,
                                        @RequestParam String password) {

        String token = librusLoginService.login(login, password);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Nie udało się zalogować");
        }
    }

    @GetMapping("/timetables")
    public ResponseEntity<Object> getTimetables(@RequestHeader("Authorization") String token,
                                                @RequestParam("from") String from,
                                                @RequestParam("to") String to) {
        try {
            LocalDate startDate = LocalDate.parse(from);
            LocalDate endDate = LocalDate.parse(to);
            Object timetables = librusTimetableService.getTimetable(startDate, endDate, token);
            return ResponseEntity.ok(timetables);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Wystąpił błąd podczas pobierania planu lekcji: " + e.getMessage());
        }
    }

    @GetMapping("/grades")
    public ResponseEntity<Object> getGrades(@RequestHeader("Authorization") String token) {
        try{
            Object grades = librusGradesService.getGrades(token);
            return ResponseEntity.ok(grades);
        } catch (Exception e){
            return ResponseEntity.status(500).body("Wystąpił błąd podczas pobierania ocen: " + e.getMessage());
        }
    }

    @GetMapping("/grades/getAverage/allSubjects")
    public ResponseEntity<Object> getAverageGrades(@RequestHeader("Authorization") String token) {
        try{
            Object  averageGradesAll = librusGradesService.getAverage(token);
            return ResponseEntity.ok(averageGradesAll);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body("Nie udalo sie pobrac sredniej: " + e.getMessage());
        }

    }

    @GetMapping("/grades/getAverage/specificSubject")
    public ResponseEntity<Object> getAverageGradesSpecificSubject(@RequestHeader("Authorization") String token,
                                                                  @RequestParam("subject") String subject) {
        try{
            Object averageGradesSpecific = librusGradesService.getAverage(token, subject);
            return ResponseEntity.ok(averageGradesSpecific);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body("Nie udalo sie pobrac sredniej dla podanego przedmiotu: " + e.getMessage());
        }
    }

    @GetMapping("/absences/{id}")
    public ResponseEntity<Map<String, Object>> getAbsenceById(@PathVariable String id,
                                                              @RequestHeader("Authorization") String token) {
        try {
            Map<String, Object> absenceData = librusAbsenceService.getAbsencesForSubject(token, id);
            return ResponseEntity.ok(absenceData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Wystąpił błąd podczas pobierania danych o nieobecności"));
        }
    }

    @GetMapping("/absences")
    public ResponseEntity<Map<String, Object>> getAllAbsences(@RequestHeader("Authorization") String token) {
        try {
            Map<String, Object> allAbsences = librusAbsenceService.getAbsences(token);
            return ResponseEntity.ok(allAbsences);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Wystąpił błąd podczas pobierania listy nieobecności"));
        }
    }
}
