package api.librus.librusapi;

import api.librus.librusapi.LibrusTimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/librus")
public class LibrusController {

    private final LibrusLoginService librusLoginService;
    private final LibrusTimetableService librusTimetableService;

    @Autowired
    public LibrusController(LibrusLoginService librusLoginService, LibrusTimetableService librusTimetableService) {
        this.librusLoginService = librusLoginService;
        this.librusTimetableService = librusTimetableService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String login, @RequestParam String password) {
        String token = librusLoginService.login(login, password);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Nie udało się zalogować");
        }
    }

    @GetMapping("/timetables")
    public ResponseEntity<Object> getTimetables(@RequestHeader("Authorization") String token) {
        try {
            LibrusTimetableService timetableService = new LibrusTimetableService(token);
            Object timetables = timetableService.getTimetables();
            return ResponseEntity.ok(timetables);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Wystąpił błąd podczas pobierania planu lekcji");
        }
    }
}
