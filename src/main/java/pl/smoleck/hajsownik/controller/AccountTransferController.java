//package pl.smoleck.hajsownik.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import pl.smoleck.hajsownik.model.Account;
//import pl.smoleck.hajsownik.model.TransferRequest;
//import pl.smoleck.hajsownik.model.User;
//import pl.smoleck.hajsownik.repository.AccountRepository;
//import pl.smoleck.hajsownik.service.TransferService;
//import pl.smoleck.hajsownik.service.UserService;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/accounts")
//public class AccountTransferController {
//
//    private final TransferService transferService;
//    private final AccountRepository accountRepository;
//    private final UserService userService; // zakładam, że ma getCurrentUser()
//
//    @Autowired
//    public AccountTransferController(TransferService transferService,
//                                     AccountRepository accountRepository,
//                                     UserService userService) {
//        this.transferService = transferService;
//        this.accountRepository = accountRepository;
//        this.userService = userService;
//    }
//
//    @GetMapping("/transfer")
//    public String showTransferForm(Model model) {
//        User current = userService.getCurrentUser();
//        List<Account> accounts = accountRepository.findByUser(current);
//        model.addAttribute("accounts", accounts);
//        model.addAttribute("transferRequest", new TransferRequest());
//        return "transfer-form";
//    }
//
//    @PostMapping("/transfer")
//    public String doTransfer(@ModelAttribute TransferRequest transferRequest,
//                             Authentication authentication,
//                             RedirectAttributes redirect) {
//        String username = authentication.getName();
//        try {
//            transferService.transferBetweenOwnAccounts(
//                    transferRequest.getFromAccountId(),
//                    transferRequest.getToAccountId(),
//                    transferRequest.getAmount(),
//                    username
//            );
//            redirect.addFlashAttribute("success", "Przelew wykonany pomyślnie");
//        } catch (Exception e) {
//            redirect.addFlashAttribute("error", e.getMessage());
//        }
//        return "redirect:/accounts/transfer";
//    }
//}