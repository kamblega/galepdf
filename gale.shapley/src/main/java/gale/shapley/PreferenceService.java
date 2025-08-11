package gale.shapley;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PreferenceService {

	public static void main(String[] args) {
		SpringApplication.run( PreferenceService.class, args);
		System.out.println("Preference Service is running...");
	}

}
