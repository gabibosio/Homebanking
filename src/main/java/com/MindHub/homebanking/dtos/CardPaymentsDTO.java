package com.MindHub.homebanking.dtos;

import java.time.LocalDate;

public class CardPaymentsDTO {

    private String cardNumber;

    private int cvv;

    private long amountPayment;

    private String description;

    private String name;


    public CardPaymentsDTO() {
    }

    public CardPaymentsDTO(String name,String cardNumber, int cvv, long amount, String description) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.amountPayment = amount;
        this.description = description;
        this.name = name;
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

}
