//package pl.smoleck.hajsownik.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import pl.smoleck.hajsownik.model.Account;
//import pl.smoleck.hajsownik.model.User;
//import pl.smoleck.hajsownik.repository.AccountRepository;
//import pl.smoleck.hajsownik.repository.UserRepository;
//
//@Service
//public class TransferService {
//
//    private final AccountRepository accountRepository;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public TransferService(AccountRepository accountRepository,
//                           UserRepository userRepository) {
//        this.accountRepository = accountRepository;
//        this.userRepository = userRepository;
//    }
//
//    @Transactional
//    public void transferBetweenOwnAccounts(Long fromId, Long toId, Double amount, String username) {
//        if (amount == null || amount <= 0) {
//            throw new IllegalArgumentException("Kwota musi być większa od zera");
//        }
//        if (fromId.equals(toId)) {
//            throw new IllegalArgumentException("Nie można przelać na to samo konto");
//        }
//
//        // Pobierz zalogowanego użytkownika
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));
//
//        // Załaduj konta i zweryfikuj własność
//        Account from = accountRepository.findByIdAndUser(fromId, user)
//                .orElseThrow(() -> new RuntimeException("Konto źródłowe nie znalezione lub nie należy do użytkownika"));
//
//        Account to = accountRepository.findByIdAndUser(toId, user)
//                .orElseThrow(() -> new RuntimeException("Konto docelowe nie znalezione lub nie należy do użytkownika"));
//
//        // 🚀 Pozwalamy zejść na minus, ale max do -100000
//        double newBalance = from.getBalance() - amount;
//        if (newBalance < -100000) {
//            throw new IllegalStateException("Przekroczono limit zadłużenia (-100000)");
//        }
//
//        from.setBalance(newBalance);
//        to.setBalance(to.getBalance() + amount);
//
//        accountRepository.save(from);
//        accountRepository.save(to);
//    }
//}