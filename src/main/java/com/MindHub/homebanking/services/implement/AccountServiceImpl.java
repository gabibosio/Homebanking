package com.MindHub.homebanking.services.implement;

import com.MindHub.homebanking.dtos.AccountDTO;
import com.MindHub.homebanking.dtos.ClientDTO;
import com.MindHub.homebanking.models.Account;
import com.MindHub.homebanking.models.Client;
import com.MindHub.homebanking.repositories.AccountRepository;
import com.MindHub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.MindHub.homebanking.utils.Util.getRandomNumber;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<AccountDTO> getAccountsDto() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountDto(long id, Authentication authentication) {
        if(!authentication.getName().equals("admin@admin.com")){
            Account account = accountRepository.findById(id).orElse(null);
            if(account != null){
                account.setTransactions(account.getTransactions().stream().filter(transaction ->  transaction.isVisibility() == true).collect(Collectors.toSet()));
                return new AccountDTO(account);
            }
        }
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @Override
    public void AccountSave(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public String accountNumber() {
        String account = "VIN" + getRandomNumber(10000000,100000000);
        while(accountRepository.findByNumber(account) != null) {
            account = "VIN" + getRandomNumber(10000000,100000000);
        }
        return account;
    }

    @Override
    public Account findByID(long id) {
        return accountRepository.findById(id).orElse(null);
    }

}
