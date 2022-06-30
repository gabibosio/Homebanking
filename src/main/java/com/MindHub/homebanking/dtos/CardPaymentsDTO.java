package com.MindHub.homebanking.dtos;

import java.time.LocalDate;

public class CardPaymentsDTO {

    private String cardNumber;

    private int cvv;

    private long amountPayment;

    private String description;

    private String name;

    private LocalDate thruDate;

    public CardPaymentsDTO() {
    }

    public CardPaymentsDTO(String name,String cardNumber, int cvv, long amount, String description,LocalDate thruDate) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.amountPayment = amount;
        this.description = description;
        this.name = name;
        this.thruDate = thruDate;
    }

    public String  getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String  cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCvv() {
        return cvv;
    }
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public long getAmountPayment() {
        return amountPayment;
    }
    public void setAmountPayment(long amountPayment) {
        this.amountPayment = amountPayment;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }
}
