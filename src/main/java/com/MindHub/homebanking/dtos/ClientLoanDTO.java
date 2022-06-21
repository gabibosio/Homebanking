package com.MindHub.homebanking.dtos;

import com.MindHub.homebanking.models.ClientLoan;
import com.MindHub.homebanking.models.Loan;

public class ClientLoanDTO {

    private long id;
    private long loanid;
    private Double amount;
    private int payments;
    private String name;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO (ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.loanid = clientLoan.getLoan().getId();
        this.payments = clientLoan.getPayments();
        this.amount = clientLoan.getAmount();
        this.name = clientLoan.getLoan().getName();
    }

    public long getId() {
        return id;
    }


    public long getLoanid() {
        return loanid;
    }

    public void setLoanid(long loanid) {
        this.loanid = loanid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
