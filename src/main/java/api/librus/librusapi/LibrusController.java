package api.librus.librusapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/librus")
public class LibrusController {

    private final LibrusLoginService librusLoginService;
    private final LibrusTimetableService librusTimetableService;
    private final LibrusGradesService librusGradesService;

    @Autowired
    public LibrusController(LibrusLoginService librusLoginService, LibrusTimetableService librusTimetableService, LibrusGradesService librusGradesService) {
        this.librusLoginService = librusLoginService;
        this.librusTimetableService = librusTimetableService;
        this.librusGradesService = librusGradesService;
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
}
