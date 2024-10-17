package api.librus.librusapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class LibrusApiApplication {

	public static void main(String[] args) {
		checkChromedriverInstallation(); // Sprawdzenie czy chromedriver jest zainstalowany i dostępny
		SpringApplication.run(LibrusApiApplication.class, args);
	}

	private static void checkChromedriverInstallation() {
		String path = "/usr/bin/chromedriver"; // Ścieżka do chromedrivera
		File file = new File(path);

		if (!file.exists()) {
			System.err.println("Chromedriver nie jest zainstalowany lub nie znajduje się w oczekiwanej lokalizacji: " + path);
			System.exit(1); // Zakończenie aplikacji z kodem błędu
		} else {
			System.out.println("Chromedriver jest zainstalowany i dostępny.");
		}
	}

}
