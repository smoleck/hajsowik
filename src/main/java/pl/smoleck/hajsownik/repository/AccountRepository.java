package pl.smoleck.hajsownik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.smoleck.hajsownik.model.Account;
import pl.smoleck.hajsownik.model.User;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
//    Optional<Account> findByUser(User user);
//
//    User findByIdAndUser(Long fromId, User user);

    List<Account> findByUser(User user);
    Optional<Account> findByIdAndUser(Long id, User user);

}