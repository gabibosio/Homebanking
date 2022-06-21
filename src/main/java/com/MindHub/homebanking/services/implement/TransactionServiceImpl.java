package com.MindHub.homebanking.services.implement;

import com.MindHub.homebanking.models.Transaction;
import com.MindHub.homebanking.repositories.TransactionRepositoy;
import com.MindHub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepositoy transactionRepositoy;


    @Override
    public void TransactionSave(Transaction transaction) {
        transactionRepositoy.save(transaction);
    }
}
