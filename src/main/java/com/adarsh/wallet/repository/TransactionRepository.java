package com.adarsh.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adarsh.wallet.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ✅ Get transactions where user is sender OR receiver
    List<Transaction> findBySenderIdOrReceiverIdOrderByTimestampDesc(Long senderId, Long receiverId);

}