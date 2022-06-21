package com.MindHub.homebanking.services;

import com.MindHub.homebanking.dtos.LoanDTO;
import com.MindHub.homebanking.models.ClientLoan;
import com.MindHub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    List<LoanDTO> getLoansDto();
    Loan findById(long id);

    void saveLoan(Loan loan);
}
