package pl.smoleck.hajsownik.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Wyświetlanie strony logowania
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Nazwa pliku HTML w folderze templates
    }
}