package com.MindHub.homebanking.services;

import com.MindHub.homebanking.dtos.AccountDTO;
import com.MindHub.homebanking.models.Account;
import com.MindHub.homebanking.repositories.AccountRepository;
import com.MindHub.homebanking.utils.Util;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<AccountDTO> getAccountsDto();
    AccountDTO getAccountDto(long id, Authentication authentication);
    void AccountSave(Account account);
    Account findByNumber(String number);

    String accountNumber();

    Account findByID(long id);
}
