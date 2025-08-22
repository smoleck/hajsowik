package pl.smoleck.hajsownik.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.smoleck.hajsownik.model.Account;
import pl.smoleck.hajsownik.model.Transaction;
import pl.smoleck.hajsownik.model.User;
import pl.smoleck.hajsownik.repository.AccountRepository;
import pl.smoleck.hajsownik.repository.TransactionRepository;
import pl.smoleck.hajsownik.repository.UserRepository;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Transaction addTransaction(Transaction transaction, Long accountId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!account.getUser().equals(user)) {
            throw new AccessDeniedException("You do not own this account");
        }

        // Ustaw konto w transakcji
        transaction.setAccount(account);

        // Aktualizujemy saldo konta
        account.setBalance(account.getBalance() + transaction.getAmount());
        accountRepository.save(account);

        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long transactionId, Transaction updatedTransaction, String username) {
        Transaction existing = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        Account account = existing.getAccount();

        if (!account.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You do not own this transaction");
        }

        // Korekta salda (najpierw usuwamy starą wartość, potem dodajemy nową)
        account.setBalance(account.getBalance() - existing.getAmount() + updatedTransaction.getAmount());
        accountRepository.save(account);

        // Update danych
        existing.setName(updatedTransaction.getName());
        existing.setAmount(updatedTransaction.getAmount());
        existing.setCategory(updatedTransaction.getCategory());
        existing.setDate(updatedTransaction.getDate());

        return transactionRepository.save(existing);
    }

    public void deleteTransaction(Long transactionId, String username) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        Account account = transaction.getAccount();

        if (!account.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You do not own this transaction");
        }

        // Cofnięcie wpływu na saldo
        account.setBalance(account.getBalance() - transaction.getAmount());
        accountRepository.save(account);

        transactionRepository.delete(transaction);
    }

    public List<Transaction> getTransactionsForAccount(Long accountId, String username) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!account.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You do not own this account");
        }

        return transactionRepository.findByAccount(account);
    }
}