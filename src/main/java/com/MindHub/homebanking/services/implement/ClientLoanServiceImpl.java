package com.MindHub.homebanking.services.implement;

import com.MindHub.homebanking.models.ClientLoan;
import com.MindHub.homebanking.repositories.ClientLoanRepository;
import com.MindHub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImpl implements ClientLoanService {

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Override
    public void ClientLoanServiceSave(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
