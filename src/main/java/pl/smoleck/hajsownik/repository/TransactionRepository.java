package pl.smoleck.hajsownik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.smoleck.hajsownik.model.Account;
import pl.smoleck.hajsownik.model.Transaction;
import pl.smoleck.hajsownik.model.User;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount(Account account);
    List<Transaction> findByUser(User user);
    Optional<Transaction> findByIdAndUser(Long id, User user);
    List<Transaction> findByUserAndCategory(User user, String category);

}
