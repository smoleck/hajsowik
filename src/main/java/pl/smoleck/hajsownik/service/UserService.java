package pl.smoleck.hajsownik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.smoleck.hajsownik.model.User;
import pl.smoleck.hajsownik.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Rejestracja użytkownika
    public void registerUser(User user) {
        // Szyfrowanie hasła przed zapisem do bazy danych
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Domyślna rola
        userRepository.save(user);
        userRepository.flush();
    }

    // Pobieranie użytkownika po nazwie użytkownika
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Brak uwierzytelnionego użytkownika");
        }

        String username = authentication.getName();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Nie znaleziono użytkownika: " + username);
        }

        return user.orElse(null);
    }

    // Inne metody związane z zarządzaniem użytkownikami
    // np. aktualizacja danych użytkownika, usuwanie użytkownika itp.
}