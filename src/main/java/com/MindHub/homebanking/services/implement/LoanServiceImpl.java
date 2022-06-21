package com.MindHub.homebanking.services.implement;

import com.MindHub.homebanking.dtos.LoanDTO;
import com.MindHub.homebanking.models.ClientLoan;
import com.MindHub.homebanking.models.Loan;
import com.MindHub.homebanking.repositories.ClientLoanRepository;
import com.MindHub.homebanking.repositories.LoanRepository;
import com.MindHub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Override
    public List<LoanDTO> getLoansDto() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Override
    public Loan findById(long id) {
        return loanRepository.findById(id);
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

}
