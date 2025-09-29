package pl.smoleck.hajsownik.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.smoleck.hajsownik.model.SpendingGoal;
import pl.smoleck.hajsownik.model.User;
import pl.smoleck.hajsownik.service.SpendingGoalService;
import pl.smoleck.hajsownik.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/spending-goals")
@RequiredArgsConstructor
public class SpendingGoalController {

    private final SpendingGoalService spendingGoalService;
    private final UserService userService;

    /** Pobranie wszystkich celów użytkownika */
    @GetMapping
    public ResponseEntity<List<SpendingGoal>> getGoals(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<SpendingGoal> goals = spendingGoalService.getGoalsForUser(user);

        if (goals.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content jeśli brak celów
        }

        return ResponseEntity.ok(goals); // 200 OK
    }

    /** Pobranie jednego celu po ID */
    @GetMapping("/{id}")
    public ResponseEntity<SpendingGoal> getGoal(@PathVariable Long id,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        SpendingGoal goal = spendingGoalService.getById(id);
        if (goal == null) return ResponseEntity.notFound().build(); // 404 Not Found

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!goal.getUser().getId().equals(user.getId())) return ResponseEntity.status(403).build(); // 403 Forbidden

        return ResponseEntity.ok(goal); // 200 OK
    }

    /** Dodanie nowego celu */
    @PostMapping
    public ResponseEntity<SpendingGoal> addGoal(@RequestBody SpendingGoal goal,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
                SpendingGoal savedGoal = spendingGoalService.createGoalForUser(goal, user);

        // Zwracamy 201 Created i Location header
        return ResponseEntity
                .created(URI.create("/api/spending-goals/" + savedGoal.getId()))
                .body(savedGoal);
    }

    /** Aktualizacja istniejącego celu */
    @PutMapping("/{id}")
    public ResponseEntity<SpendingGoal> updateGoal(@PathVariable Long id,
                                                   @RequestBody SpendingGoal updatedGoal,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        SpendingGoal existingGoal = spendingGoalService.getById(id);
        if (existingGoal == null) return ResponseEntity.notFound().build(); // 404 Not Found

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));        if (!existingGoal.getUser().getId().equals(user.getId())) return ResponseEntity.status(403).build(); // 403 Forbidden

        existingGoal.setCategory(updatedGoal.getCategory());
        existingGoal.setLimitAmount(updatedGoal.getLimitAmount());
        SpendingGoal savedGoal = spendingGoalService.save(existingGoal);

        return ResponseEntity.ok(savedGoal); // 200 OK
    }

    /** Usunięcie celu */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        SpendingGoal existingGoal = spendingGoalService.getById(id);
        if (existingGoal == null) return ResponseEntity.notFound().build(); // 404 Not Found

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));        if (!existingGoal.getUser().getId().equals(user.getId())) return ResponseEntity.status(403).build(); // 403 Forbidden

        spendingGoalService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}