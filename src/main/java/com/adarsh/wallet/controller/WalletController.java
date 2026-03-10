package com.adarsh.wallet.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.adarsh.wallet.dto.TransferRequest;
import com.adarsh.wallet.entity.Transaction;
import com.adarsh.wallet.entity.User;
import com.adarsh.wallet.repository.UserRepository;
import com.adarsh.wallet.service.WalletService;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin
public class WalletController {

    private final WalletService walletService;
    private final UserRepository userRepository;

    public WalletController(WalletService walletService,
                            UserRepository userRepository) {
        this.walletService = walletService;
        this.userRepository = userRepository;
    }

    // ✅ PROFILE (Name + Balance)
    @GetMapping("/profile")
    public Map<String, Object> getProfile(Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getName());
        response.put("balance", user.getWalletBalance());

        return response;
    }

    // ✅ Get Logged-in User Balance
    @GetMapping("/balance")
    public Double getBalance(Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return walletService.getBalance(user.getId());
    }

    // ✅ Add Money (Logged-in User)
    @PostMapping("/add")
    public String addMoney(@RequestParam double amount,
                           Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return walletService.addMoney(user.getId(), amount);
    }

    // ✅ Transfer Money (Logged-in User)
    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request,
                           Principal principal) {

        String email = principal.getName();
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return walletService.transfer(sender.getId(), request);
    }

    // ✅ Transaction History (Logged-in User)
    @GetMapping("/history")
    public List<Transaction> getHistory(Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return walletService.getUserTransactions(user.getId());
    }

    // ✅ Admin: Get All Users
    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}