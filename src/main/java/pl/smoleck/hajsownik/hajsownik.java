package pl.smoleck.hajsownik;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.smoleck.hajsownik.service.UserService;

@SpringBootApplication
public class hajsownik {

    public static void main(String[] args) {
        SpringApplication.run(hajsownik.class, args);
    }


}

