package pl.smoleck.hajsownik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smoleck.hajsownik.model.SpendingGoal;
import pl.smoleck.hajsownik.model.Transaction;
import pl.smoleck.hajsownik.model.TransactionType;
import pl.smoleck.hajsownik.model.User;
import pl.smoleck.hajsownik.repository.SpendingGoalRepository;
import pl.smoleck.hajsownik.repository.TransactionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpendingGoalService {

    private final SpendingGoalRepository spendingGoalRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Tworzy nowy cel dla użytkownika.
     */
    public SpendingGoal createGoalForUser(SpendingGoal goal, User user) {
        goal.setUser(user); // ustawiamy właściciela
        return spendingGoalRepository.save(goal);
    }

    /**
     * Pobiera wszystkie cele użytkownika z aktualnym stanem wydatków w kategorii.
     */
    public List<SpendingGoal> getGoalsForUser(User user) {
        List<SpendingGoal> goals = spendingGoalRepository.findByUser(user);

        goals.forEach(goal -> {
            // Zliczamy tylko wydatki (TransactionType.EXPENSE) w danej kategorii
            Double spent = transactionRepository.findByUserAndCategory(user, goal.getCategory())
                    .stream()
                    .filter(t -> t.getType() == TransactionType.EXPENSE)
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            goal.setCurrentAmount(spent);
        });

        return goals;
    }

    /**
     * Pobiera jeden cel po ID.
     */
    public SpendingGoal getById(Long id) {
        return spendingGoalRepository.findById(id).orElse(null);
    }

    /**
     * Zapisuje cel (nowy lub zaktualizowany).
     */
    public SpendingGoal save(SpendingGoal goal) {
        return spendingGoalRepository.save(goal);
    }

    /**
     * Usuwa cel po ID.
     */
    public void delete(Long id) {
        spendingGoalRepository.deleteById(id);
    }
}