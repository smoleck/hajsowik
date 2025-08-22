package pl.smoleck.hajsownik.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.smoleck.hajsownik.model.Account;
import pl.smoleck.hajsownik.model.Transaction;
import pl.smoleck.hajsownik.model.TransactionType;
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

        transaction.setAccount(account);

        // Zmiana salda w zależności od typu
        if (transaction.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance() + transaction.getAmount());
        } else if (transaction.getType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance() - transaction.getAmount());
        }

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

        // Cofamy wpływ starej transakcji
        if (existing.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance() - existing.getAmount());
        } else if (existing.getType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance() + existing.getAmount());
        }

        // Nakładamy nową
        if (updatedTransaction.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance() + updatedTransaction.getAmount());
        } else if (updatedTransaction.getType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance() - updatedTransaction.getAmount());
        }

        accountRepository.save(account);

        existing.setName(updatedTransaction.getName());
        existing.setAmount(updatedTransaction.getAmount());
        existing.setCategory(updatedTransaction.getCategory());
        existing.setDate(updatedTransaction.getDate());
        existing.setType(updatedTransaction.getType());

        return transactionRepository.save(existing);
    }

    public void deleteTransaction(Long transactionId, String username) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        Account account = transaction.getAccount();

        if (!account.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You do not own this transaction");
        }

        // Cofamy wpływ na saldo
        if (transaction.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance() - transaction.getAmount());
        } else if (transaction.getType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance() + transaction.getAmount());
        }

        accountRepository.save(account);
        transactionRepository.delete(transaction);
    }
}