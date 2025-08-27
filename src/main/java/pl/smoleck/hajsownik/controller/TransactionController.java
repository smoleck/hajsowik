package pl.smoleck.hajsownik.controller;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smoleck.hajsownik.model.Transaction;
import pl.smoleck.hajsownik.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{accountId}")
    public Transaction addTransaction(@RequestBody Transaction transaction,
                                      @PathVariable Long accountId,
                                      Authentication authentication) {
        return transactionService.addTransaction(transaction, accountId, authentication.getName());
    }

    @PutMapping("/{transactionId}")
    public Transaction updateTransaction(@PathVariable Long transactionId,
                                         @RequestBody Transaction transaction,
                                         Authentication authentication) {
        return transactionService.updateTransaction(transactionId, transaction, authentication.getName());
    }

    @DeleteMapping("/{transactionId}")
    public void deleteTransaction(@PathVariable Long transactionId, Authentication authentication) {
        transactionService.deleteTransaction(transactionId, authentication.getName());
    }

    @GetMapping
    public List<Transaction> getAllUserTransactions(Authentication authentication) {
        return transactionService.getAllTransactions(authentication.getName());
    }




}