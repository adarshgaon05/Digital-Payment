package com.adarsh.wallet.dto;

public class TransferRequest {

    private Long receiverId;
    private Double amount;

    // 🔹 GETTERS
    public Long getReceiverId() {
        return receiverId;
    }

    public Double getAmount() {
        return amount;
    }

    // 🔹 SETTERS (🔥 YOU WERE MISSING THIS)
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
