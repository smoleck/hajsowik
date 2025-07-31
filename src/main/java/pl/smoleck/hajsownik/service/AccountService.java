package pl.smoleck.hajsownik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.smoleck.hajsownik.model.Account;
import pl.smoleck.hajsownik.model.User;
import pl.smoleck.hajsownik.repository.AccountRepository;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }
    public List<Account> getAccountsForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return accountRepository.findByUser(currentUser);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void createAccount(Account account) {
        User currentUser = userService.getCurrentUser();
        account.setUser(currentUser);
        accountRepository.save(account);
    }

    public void updateAccount(Long id, Account updated) {
        Account existing = getAccountById(id);
        validateOwnership(existing);
        existing.setName(updated.getName());
        existing.setBalance(updated.getBalance());
        accountRepository.save(existing);
    }

    public void deleteAccount(Long id) {
        Account account = getAccountById(id);
        validateOwnership(account);
        accountRepository.delete(account);
    }

    private void validateOwnership(Account account) {
        User currentUser = userService.getCurrentUser();
        if (!account.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Brak dostępu do tego konta");
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


}
