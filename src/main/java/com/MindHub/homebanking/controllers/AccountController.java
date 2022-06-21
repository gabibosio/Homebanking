package com.MindHub.homebanking.controllers;

import com.MindHub.homebanking.dtos.AccountDTO;
import com.MindHub.homebanking.models.*;
import com.MindHub.homebanking.repositories.AccountRepository;
import com.MindHub.homebanking.repositories.ClientRepository;
import com.MindHub.homebanking.services.AccountService;
import com.MindHub.homebanking.services.ClientService;
import com.MindHub.homebanking.services.TransactionService;
import com.MindHub.homebanking.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccountsDto();
    }

    @GetMapping("accounts/{id}")
    public AccountDTO getAccount (@PathVariable Long id,Authentication authentication) {
        return accountService.getAccountDto(id,authentication);
    }

    @PatchMapping(path = "/accounts/visibility/{id}")
    public ResponseEntity<Object> visibilityAccount(@PathVariable long id){
        Account account = accountService.findByID(id);
        if(account == null){
            return new ResponseEntity<>("no existe el id",HttpStatus.FORBIDDEN);
        }
        if(account.getBalance() > 0){
            return new ResponseEntity<>("balance mayor a 0",HttpStatus.FORBIDDEN);
        }

        Set<Transaction> transaction = account.getTransactions();
        transaction.forEach(transaction1 -> transaction1.setVisibility(false));
        transaction.forEach(transaction1 -> transactionService.TransactionSave(transaction1));
        account.setVisibility(false);
        accountService.AccountSave(account);
        return new ResponseEntity<>("Eliminada",HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> newAccount(@RequestParam AccountType accountType,Authentication authentication) {
        Client client = clientService.getClientCurrent(authentication);
        if(client.getAccounts().size() >= 3){
            return new ResponseEntity<>("ya tiene 3 cuentas", HttpStatus.FORBIDDEN);
        }
        else {
            Account account = new Account(accountType,true,client, accountService.accountNumber(), LocalDate.now(), 0);
            accountService.AccountSave(account);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

}
