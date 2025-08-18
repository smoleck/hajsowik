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

    @PutMapping("/accounts/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id,
                                           @RequestBody Account updatedAccount,
                                           Principal principal) {
        try {
            Account saved = accountService.updateAccount(id, updatedAccount, principal.getName());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, Principal principal) {
        try {
            accountService.deleteAccount(id, principal.getName());
            return ResponseEntity.ok(Map.of("message", "Konto usunięte"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

//    @PostMapping("/transfer")
//    public ResponseEntity<?> transfer(@RequestBody Map<String, Object> payload, Principal principal) {
//        try {
//            Long fromId = ((Number) payload.get("fromAccountId")).longValue();
//            Long toId = ((Number) payload.get("toAccountId")).longValue();
//            Double amount = ((Number) payload.get("amount")).doubleValue();
//
//            accountService.transferBetweenAccounts(principal.getName(), fromId, toId, amount);
//
//            return ResponseEntity.ok(Map.of("message", "Przelew wykonany!"));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//        }
//    }
}