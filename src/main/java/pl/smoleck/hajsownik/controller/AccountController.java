package pl.smoleck.hajsownik.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.smoleck.hajsownik.model.Account;
import pl.smoleck.hajsownik.model.User;
import pl.smoleck.hajsownik.repository.AccountRepository;
import pl.smoleck.hajsownik.service.AccountService;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<?> addAccount(@RequestBody Account account, Principal principal) {
        try {
            Account saved = accountService.addAccount(account, principal.getName());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts(Principal principal) {
        return ResponseEntity.ok(accountService.getAccountsForUser(principal.getName()));
    }
}