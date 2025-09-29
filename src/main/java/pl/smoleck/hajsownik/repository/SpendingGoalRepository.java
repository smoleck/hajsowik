package pl.smoleck.hajsownik.repository;

import pl.smoleck.hajsownik.model.SpendingGoal;
import pl.smoleck.hajsownik.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpendingGoalRepository extends JpaRepository<SpendingGoal, Long> {
    List<SpendingGoal> findByUser(User user);
}