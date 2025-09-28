package pl.smoleck.hajsownik.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.smoleck.hajsownik.model.TransferRequest;
import pl.smoleck.hajsownik.service.AccountService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TransferController {

    private final AccountService accountService;
    public TransferController(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request, Principal principal) {
        try {
            accountService.transferBetweenAccounts(
                    principal.getName(),
                    request.getFromAccountId(),
                    request.getToAccountId(),
                    request.getAmount()
            );
            var accounts = accountService.getAccountsForUser(principal.getName());
            return ResponseEntity.ok(accounts);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }



}

