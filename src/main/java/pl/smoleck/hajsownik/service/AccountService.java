package pl.smoleck.hajsownik.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.smoleck.hajsownik.model.Account;
import pl.smoleck.hajsownik.model.User;
import pl.smoleck.hajsownik.repository.AccountRepository;
import pl.smoleck.hajsownik.repository.UserRepository;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account addAccount(Account account, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        account.setUser(user);
        account.setBalance(account.getBalance());
        return accountRepository.save(account);
    }

    public Account updateAccount(Long accountId, Account updatedAccount, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account existing = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!existing.getUser().equals(user)) {
            throw new AccessDeniedException("You do not have access to this account");
        }

        existing.setName(updatedAccount.getName());
        existing.setBalance(updatedAccount.getBalance());
        existing.setBank(updatedAccount.getBank());

        return accountRepository.save(existing);
    }
    public Account deleteAccount(Long accountId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!account.getUser().equals(user)) {
            throw new AccessDeniedException("You do not have access to this account");
        }

        accountRepository.delete(account);
        return account;
    }

    public List<Account> getAccountsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return accountRepository.findByUser(user);
    }

    public void transferBetweenAccounts(String username, Long fromId, Long toId, Double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        if (!from.getUser().equals(user) || !to.getUser().equals(user)) {
            throw new IllegalArgumentException("You can only transfer between your own accounts");
        }

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        accountRepository.save(from);
        accountRepository.save(to);
    }


}
//    public List<Account> getAccountsForCurrentUser() {
//        User currentUser = userService.getCurrentUser();
//        return accountRepository.findByUser(currentUser);
//    }
//
//    public Account createAccount(Account account) {
//        User currentUser = userService.getCurrentUser();
//        account.setUser(currentUser);
//        return accountRepository.save(account);
//    }
//
//    public Account updateAccount(Long accountId, Account updatedAccount) {
//        Account existing = accountRepository.findById(accountId)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        validateOwnership(existing);
//
//        existing.setName(updatedAccount.getName());
//        existing.setBalance(updatedAccount.getBalance());
//        existing.setBank(updatedAccount.getBank());
//
//        return accountRepository.save(existing);
//    }
//
//    public void deleteAccount(Long accountId) {
//        Account account = accountRepository.findById(accountId)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        validateOwnership(account);
//
//        accountRepository.delete(account);
//    }
//
//    private void validateOwnership(Account account) {
//        User currentUser = userService.getCurrentUser();
//
//        if (!account.getUser().getId().equals(currentUser.getId())) {
//            throw new AccessDeniedException("Nie masz dostępu do tego konta");
//        }
//    }



