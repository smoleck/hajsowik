package pl.smoleck.hajsownik.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.smoleck.hajsownik.model.Account;
import pl.smoleck.hajsownik.model.User;
import pl.smoleck.hajsownik.repository.AccountRepository;

import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    // Wyświetlenie formularza do dodania konta
    @GetMapping("/add")
    public String showAddAccountForm() {
        return "add-account"; // Zwraca widok add-account.html
    }

    // Obsługa przesłanego formularza
    @PostMapping
    public String addAccount(
            @RequestParam String name,
            @RequestParam String bank,
            @RequestParam double balance,
            @AuthenticationPrincipal User user) {

        Account account = new Account();
        account.setName(name);
        account.setBank(bank);
        account.setBalance(balance);
        account.setUser(user);

        accountRepository.save(account);
        return "redirect:/home"; // Przekieruj na stronę główną po dodaniu konta
    }

    // Pobranie listy kont zalogowanego użytkownika
    @GetMapping
    public String getAccounts(@AuthenticationPrincipal User user, Model model) {
        List<Account> accounts = accountRepository.findByUser(user);
        model.addAttribute("accounts", accounts);
        return "home"; // Zwraca widok home.html z listą kont
    }
}