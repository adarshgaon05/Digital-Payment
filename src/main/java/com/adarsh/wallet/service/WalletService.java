package com.adarsh.wallet.service;

import com.adarsh.wallet.dto.TransferRequest;
import com.adarsh.wallet.entity.Transaction;
import com.adarsh.wallet.entity.User;
import com.adarsh.wallet.repository.TransactionRepository;
import com.adarsh.wallet.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class WalletService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(UserRepository userRepository,
                         TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // ✅ ADD MONEY
    @Transactional
    public String addMoney(Long userId, double amount) {

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getWalletBalance() == null) {
            user.setWalletBalance(0.0);
        }

        user.setWalletBalance(user.getWalletBalance() + amount);
        userRepository.save(user);

        // 🔥 Save Transaction
        Transaction txn = new Transaction();
        txn.setSenderId(userId);
        txn.setReceiverId(userId);
        txn.setAmount(amount);
        txn.setStatus("CREDIT");
        txn.setTimestamp(LocalDateTime.now());

        transactionRepository.save(txn);

        return "Balance Added Successfully";
    }

    // ✅ TRANSFER MONEY
    @Transactional
    public String transfer(Long senderId, TransferRequest request) {

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getWalletBalance() == null ||
                sender.getWalletBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct from sender
        sender.setWalletBalance(sender.getWalletBalance() - request.getAmount());

        // Add to receiver
        if (receiver.getWalletBalance() == null) {
            receiver.setWalletBalance(0.0);
        }
        receiver.setWalletBalance(receiver.getWalletBalance() + request.getAmount());

        userRepository.save(sender);
        userRepository.save(receiver);

        // 🔥 Save Transaction
        Transaction txn = new Transaction();
        txn.setSenderId(senderId);
        txn.setReceiverId(request.getReceiverId());
        txn.setAmount(request.getAmount());
        txn.setStatus("DEBIT");
        txn.setTimestamp(LocalDateTime.now());

        transactionRepository.save(txn);

        return "Transfer Successful";
    }

    // ✅ GET BALANCE
    public Double getBalance(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getWalletBalance() != null ? user.getWalletBalance() : 0.0;
    }

    // ✅ GET USER TRANSACTION HISTORY
    public List<Transaction> getUserTransactions(Long userId) {

        return transactionRepository.findBySenderIdOrReceiverIdOrderByTimestampDesc(userId, userId);
    }

    // ✅ MONTHLY ANALYTICS (ADMIN)
    public Map<String, Object> getMonthlyAnalytics() {

        List<Transaction> transactions = transactionRepository.findAll();

        Map<String, Integer> monthlyCount = new LinkedHashMap<>();

        for (Month month : Month.values()) {
            String shortName = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            monthlyCount.put(shortName, 0);
        }

        for (Transaction tx : transactions) {
            if (tx.getTimestamp() != null) {

                Month month = tx.getTimestamp().getMonth();
                String shortName = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

                monthlyCount.put(shortName,
                        monthlyCount.get(shortName) + 1);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("months", new ArrayList<>(monthlyCount.keySet()));
        response.put("counts", new ArrayList<>(monthlyCount.values()));

        return response;
    }
}