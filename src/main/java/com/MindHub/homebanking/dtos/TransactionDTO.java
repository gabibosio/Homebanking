package com.MindHub.homebanking.dtos;


import com.MindHub.homebanking.models.Transaction;
import com.MindHub.homebanking.models.TransactionType;

import java.time.LocalDate;

public class TransactionDTO {
    private long id;

    private double amount;
    private String description;
    private LocalDate date;
    private TransactionType type;

    private boolean visibility;

    private double balance;

    public TransactionDTO() {}

    public TransactionDTO (Transaction transaction){
        this.id = transaction.getId();
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.date = transaction.getDate();
        this.visibility = transaction.isVisibility();
        this.balance = transaction.getBalance();
    }

    public long getId() {
        return id;
    }


    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }

    public boolean isVisibility() {
        return visibility;
    }
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
