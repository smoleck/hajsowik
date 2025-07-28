package pl.smoleck.hajsownik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.smoleck.hajsownik.model.User;
import pl.smoleck.hajsownik.repository.UserRepository;

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
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Inne metody związane z zarządzaniem użytkownikami
    // np. aktualizacja danych użytkownika, usuwanie użytkownika itp.
}