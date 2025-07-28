package pl.smoleck.hajsownik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.smoleck.hajsownik.model.Account;
import pl.smoleck.hajsownik.model.User;
import pl.smoleck.hajsownik.repository.AccountRepository;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "redirect:/login"; // Przekieruj do logowania, jeśli użytkownik nie jest zalogowany
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // <-- tutaj masz login
        System.out.println(username);

        // Pobierz konta zalogowanego użytkownika
        List<Account> accounts = accountRepository.findByUser(user);
    /*    model.addAttribute("accounts", accounts);*/
        return "home"; // Zwraca widok home.html
    }
}
