package pl.smoleck.hajsownik.repository;

import pl.smoleck.hajsownik.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

}