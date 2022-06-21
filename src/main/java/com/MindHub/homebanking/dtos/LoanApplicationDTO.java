package com.MindHub.homebanking.dtos;



public class LoanApplicationDTO {

    private long id;

    private Double amount;

    private int payments;

    private String accountDestiny;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(long id, Double amount, int payments, String accountdestiny) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.accountDestiny = accountdestiny;
    }


    public long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public String getAccountDestiny() {
        return accountDestiny;
    }

    public void setAccountDestiny(String accountDestiny) {
        this.accountDestiny = accountDestiny;
    }
}
